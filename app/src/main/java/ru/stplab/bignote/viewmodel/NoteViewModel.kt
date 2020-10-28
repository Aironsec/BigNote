package ru.stplab.bignote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import ru.stplab.bignote.data.Repository
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.data.model.NoteResult
import ru.stplab.bignote.ui.note.NoteViewState
import ru.stplab.bignote.viewmodel.base.BaseViewModel

class NoteViewModel(val repository: Repository) : BaseViewModel<NoteViewState.Data, NoteViewState>() {

    init {
        viewStateLiveData.value = NoteViewState()
    }

    private var pendingNote: Note? = null
    private var noteLiveData: LiveData<NoteResult>? = null
    private var deleteLiveData: LiveData<NoteResult>? = null

    private val deleteObserver = object : Observer<NoteResult> {
        override fun onChanged(result: NoteResult?) {
            result ?: return
            when (result) {
                is NoteResult.Success<*> -> {
                    viewStateLiveData.value = NoteViewState(NoteViewState.Data(isDelete = true))
                    pendingNote = null
                }
                is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
            }
            deleteLiveData?.removeObserver(this)
        }

    }

    private val noteObserver = object : Observer<NoteResult> {
        override fun onChanged(result: NoteResult?) {
            result ?: return
            when (result) {
                is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(NoteViewState.Data(result.data as? Note))
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
            repository.saveNote(it)
        }
        noteLiveData?.removeObserver(noteObserver)
    }

    fun loadNote(id: String) {
        noteLiveData = repository.getNoteById(id)
        noteLiveData?.observeForever(noteObserver)
    }

    fun deleteNote() {
        pendingNote?.let {
            deleteLiveData = repository.deleteNote(it.id)
            deleteLiveData?.observeForever(deleteObserver)
        }
    }
}