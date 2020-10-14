package ru.stplab.bignote.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import ru.stplab.bignote.R
import ru.stplab.bignote.ui.note.NoteActivity
import ru.stplab.bignote.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val adapter = MainAdapter {
            NoteActivity.startIntent(this, it)
        }

        note_recycle.adapter = adapter

        viewModel.viewState().observe(this, { viewStateLiveData ->
            viewStateLiveData?.let { adapter.notes = it.notes }
        })

        fab.setOnClickListener {
            NoteActivity.startIntent(this)
        }
    }
}