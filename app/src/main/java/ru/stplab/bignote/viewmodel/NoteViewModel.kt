package ru.stplab.bignote.viewmodel

import androidx.lifecycle.ViewModel
import ru.stplab.bignote.data.Repository
import ru.stplab.bignote.data.model.Note

class NoteViewModel: ViewModel() {
    private var pendingNote: Note? = null

    fun save(note: Note){
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let {
            Repository.saveNote(it)
        }
    }
}