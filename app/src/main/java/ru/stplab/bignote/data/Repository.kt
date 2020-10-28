package ru.stplab.bignote.data

import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.data.provider.DataProvider

class Repository (private val provider: DataProvider){

    fun getNotes() = provider.subscribeToAllNotes()
    fun saveNote(note: Note) = provider.saveNote(note)
    fun getNoteById(id: String) = provider.getNoteById(id)
    fun getCurrentUser() = provider.getCurrentUser()
    fun deleteNote(id: String) = provider.deleteNote(id)
}