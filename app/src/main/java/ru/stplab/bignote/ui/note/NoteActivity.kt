package ru.stplab.bignote.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import ru.stplab.bignote.R
import ru.stplab.bignote.common.getColorInt
import ru.stplab.bignote.data.model.Color
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.ui.base.BaseActivity
import ru.stplab.bignote.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : BaseActivity<NoteData>() {

    companion object {
        private const val EXTRA_NOTE = "extraNote"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun startIntent(context: Context, noteId: String? = null) =
            Intent(context, NoteActivity::class.java).apply {
                putExtra(EXTRA_NOTE, noteId)
                context.startActivity(this)
            }
    }

    override val viewModel: NoteViewModel by viewModel()
    override val layoutRes = R.layout.activity_note
    private var note: Note? = null
    var color = Color.WHITE

    override fun renderData(data: NoteData) {
        if (data.isDelete) finish()

        this.note = data.note
        supportActionBar?.title = note?.lastChanged?.let {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(it)
        } ?: getString(R.string.new_note_title)

        initView()
    }

    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) = saveNote()
    }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar_note)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)

        noteId?.let {
            viewModel.loadNote(it)
        } ?: let {
            supportActionBar?.title = getString(R.string.new_note_title)
        }

        et_title.addTextChangedListener(textChangeListener)
        et_body.addTextChangedListener(textChangeListener)

        initView()

    }

    private fun initView() {
        note?.let {
            et_title.setText(it.title)
            et_body.setText(it.text)

            toolbar_note.setBackgroundColor(it.color.getColorInt(this))
            this.color = it.color
        }
        colorPicker.onColorClickListener = {
            toolbar_note.setBackgroundColor(it.getColorInt(this))
            color = it
            saveNote()
        }
    }

    private fun saveNote() {
        if (et_title.text == null) return

        note = note?.copy(
            title = et_title.text.toString(),
            text = et_body.text.toString(),
            lastChanged = Date(),
            color = color
        ) ?: Note(UUID.randomUUID().toString(), et_title.text.toString(), et_body.text.toString(), color = color)

        note?.let { viewModel.save(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return menuInflater.inflate(R.menu.note_menu, menu).let { true }
    }

    @ExperimentalCoroutinesApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> onBackPressed().let { true }
        R.id.palette -> togglePalette().let { true }
        R.id.delete -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun togglePalette() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    @ExperimentalCoroutinesApi
    private fun deleteNote() {
        AlertDialog.Builder(this)
            .setTitle("Удалить заметку")
            .setMessage("Вы уверены?")
            .setPositiveButton("Да") { _, _ -> viewModel.deleteNote() }
            .setNegativeButton("Нет") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}