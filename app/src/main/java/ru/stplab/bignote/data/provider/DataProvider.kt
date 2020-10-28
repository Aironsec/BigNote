package ru.stplab.bignote.data.provider

import androidx.lifecycle.LiveData
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.data.model.NoteResult
import ru.stplab.bignote.data.model.User

interface DataProvider {

    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note) : LiveData<NoteResult>
    fun getCurrentUser() : LiveData<User?>
    fun deleteNote(id: String) : LiveData<NoteResult>
}
