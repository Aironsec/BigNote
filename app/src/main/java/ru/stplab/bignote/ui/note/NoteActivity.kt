package ru.stplab.bignote.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_note.*
import ru.stplab.bignote.R
import ru.stplab.bignote.data.model.Color
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_NOTE = "extraNote"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun startIntent(context: Context, note: Note? = null) =
            Intent(context, NoteActivity::class.java).apply {
                putExtra(EXTRA_NOTE, note)
                context.startActivity(this)
            }
    }

    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) = saveNote()
    }

    private var note: Note? = null
    lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar_note)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        note = intent.getParcelableExtra<Note>(EXTRA_NOTE)
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        supportActionBar?.title = note?.lastChanged?.let {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(it)
        } ?: "Новая заметка"

        initView()
    }

    private fun initView() {
        note?.let {
            et_title.setText(it.title)
            et_body.setText(it.note)

            val color = when (it.color) {
                Color.WHITE -> R.color.white
                Color.YELLOW -> R.color.yellow
                Color.GREEN -> R.color.green
                Color.BLUE -> R.color.blue
                Color.RED -> R.color.red
                Color.VIOLET -> R.color.violet
            }

            toolbar_note.setBackgroundColor(ResourcesCompat.getColor(resources, color, null))
        }

        et_title.addTextChangedListener(textChangeListener)
        et_body.addTextChangedListener(textChangeListener)
    }

    private fun saveNote() {
        if (et_title.text == null) return

        note = note?.copy(
            title = et_title.text.toString(),
            note = et_body.text.toString(),
            lastChanged = Date()
        ) ?: Note(UUID.randomUUID().toString(), et_title.text.toString(), et_body.text.toString())

        note?.let { viewModel.save(it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}