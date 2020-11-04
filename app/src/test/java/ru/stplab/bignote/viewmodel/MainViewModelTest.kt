package ru.stplab.bignote.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.stplab.bignote.data.Repository
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.data.model.NoteResult

class MainViewModelTest {
    @get:Rule
    val testExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()
    private val notesLiveData = MutableLiveData<NoteResult>()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup(){
        clearAllMocks()
        every { mockRepository.getNotes() } returns notesLiveData
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should getNotes once`(){
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `should return Notes`(){
        var result: List<Note>? = null
        val testData = listOf(Note("1"), Note("2"))
        viewModel.viewStateLiveData.observeForever {
            result = it.data
        }
        notesLiveData.value = NoteResult.Success(testData)
        Assert.assertEquals(testData, result)
    }

    @Test
    fun `should return error`(){
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.viewStateLiveData.observeForever {
            result = it.error
        }
        notesLiveData.value = NoteResult.Error(error = testData)
        Assert.assertEquals(testData, result)
    }


    @Test
    fun `should remove observer`(){
        viewModel.onCleared()
        Assert.assertFalse(notesLiveData.hasObservers())
    }
}