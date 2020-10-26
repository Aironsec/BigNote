package ru.stplab.bignote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import ru.stplab.bignote.data.Repository
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.data.model.NoteResult
import ru.stplab.bignote.ui.note.NoteViewState
import ru.stplab.bignote.viewmodel.base.BaseViewModel

class NoteViewModel : BaseViewModel<Note?, NoteViewState>() {
    init {
        viewStateLiveData.value = NoteViewState()
    }

    private var pendingNote: Note? = null
    private var noteLiveData: LiveData<NoteResult>? = null

    private val noteObserver = object : Observer<NoteResult> {
        override fun onChanged(result: NoteResult?) {
            result ?: return
            when (result) {
                is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(result.data as? Note)
                is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
            }
            noteLiveData?.removeObserver(this)
        }

    }

    fun save(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let {
            Repository.saveNote(it)
        }
        noteLiveData?.removeObserver(noteObserver)
    }

    fun loadNote(id: String) {
        noteLiveData = Repository.getNoteById(id)
        noteLiveData?.observeForever(noteObserver)
    }
}