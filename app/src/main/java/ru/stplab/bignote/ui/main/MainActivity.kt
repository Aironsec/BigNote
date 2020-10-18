package ru.stplab.bignote.ui.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.stplab.bignote.R
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.ui.base.BaseActivity
import ru.stplab.bignote.ui.note.NoteActivity
import ru.stplab.bignote.viewmodel.MainViewModel

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    override val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override val layoutRes = R.layout.activity_main
    lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = MainAdapter {
            NoteActivity.startIntent(this, it.id)
        }

        note_recycle.adapter = adapter
        fab.setOnClickListener {
            NoteActivity.startIntent(this)
        }
    }

    override fun renderData(data: List<Note>?) {
        data?.let { adapter.notes = it }
    }
}