package ru.stplab.bignote.ui.note

import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import io.mockk.*
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import ru.stplab.bignote.R
import ru.stplab.bignote.common.getColorInt
import ru.stplab.bignote.data.model.Color
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.viewmodel.NoteViewModel

class NoteActivityTest {
    @get : Rule
    val activityTestRule = ActivityTestRule(NoteActivity::class.java, true, false)

    private val viewModel: NoteViewModel = mockk(relaxed = true)
    private val viewStateLiveData = MutableLiveData<NoteViewState>()
    private val testNote = Note("1", "title", "body")

    @Before
    fun setup() {
        loadKoinModules(
            listOf(
                module {
                    viewModel { viewModel }
                }
            )
        )
        every { viewModel.getViewState() } returns viewStateLiveData
        every { viewModel.loadNote(any()) } just runs
        every { viewModel.save(any()) } just runs
        every { viewModel.deleteNote() } just runs

        activityTestRule.launchActivity(null)
    }

    @Test
    fun should_show_color_picker() {
        onView(withId(R.id.palette)).perform(click())
        onView(withId(R.id.colorPicker)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun should_hide_color_picker() {
        onView(withId(R.id.palette)).perform(click()).perform(click())
        onView(withId(R.id.colorPicker)).check(matches(not(isDisplayed())))
    }

    @Test
    fun should_set_toolbar_color() {
        onView(withId(R.id.palette)).perform(click())
        onView(withTagValue(`is`(Color.BLUE))).perform(click())

        val colorInt = Color.BLUE.getColorInt(activityTestRule.activity)

        onView(withId(R.id.toolbar_note)).check { view, _ ->
            assertTrue("toolbar background color does not match",
                (view.background as? ColorDrawable)?.color == colorInt)
        }
    }

    @Test
    fun should_show_note() {
        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(NoteViewState(NoteViewState.Data(note = testNote)))

        onView(withId(R.id.et_title)).check(matches(withText(testNote.title)))
        onView(withId(R.id.et_body)).check(matches(withText(testNote.text)))
    }

    @Test
    fun should_call_saveNote() {
        onView(withId(R.id.et_title)).perform(typeText(testNote.title))
        verify(timeout = 1000) { viewModel.save(any()) }
    }

    @Test
    fun should_call_deleteNote() {
        openActionBarOverflowOrOptionsMenu(activityTestRule.activity)
        onView(withText(R.string.delete_menu_title)).perform(click())
        onView(withText(R.string.ok_bth_title)).perform(click())
        verify { viewModel.deleteNote() }
    }
}