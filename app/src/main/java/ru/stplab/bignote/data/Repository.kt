package ru.stplab.bignote.data

import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.data.provider.DataProvider

class Repository (private val provider: DataProvider){

    fun getNotes() = provider.subscribeToAllNotes()
    suspend fun saveNote(note: Note) = provider.saveNote(note)
    suspend fun getNoteById(id: String) = provider.getNoteById(id)
    suspend fun getCurrentUser() = provider.getCurrentUser()
    suspend fun deleteNote(id: String) = provider.deleteNote(id)
}