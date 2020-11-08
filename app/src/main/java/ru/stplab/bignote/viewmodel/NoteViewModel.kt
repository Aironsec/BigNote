package ru.stplab.bignote.viewmodel

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.stplab.bignote.data.Repository
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.ui.note.NoteData
import ru.stplab.bignote.viewmodel.base.BaseViewModel

class NoteViewModel(private val repository: Repository) : BaseViewModel<NoteData>() {

    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    @ExperimentalCoroutinesApi
    @VisibleForTesting
    public override fun onCleared() {
        launch {
            pendingNote?.let {
                repository.saveNote(it)
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun loadNote(id: String) = launch {
        try {
            repository.getNoteById(id).let {
                pendingNote = it
                setData(NoteData(note = it))
            }
        } catch (e: Throwable) {
            setError(e)
        }
    }

    @ExperimentalCoroutinesApi
    fun deleteNote() = launch {
        try {
            pendingNote?.let { repository.deleteNote(it.id) }
            pendingNote = null
            setData(NoteData(isDelete = true))
        } catch (e: Throwable) {
            setError(e)
        }
    }
}