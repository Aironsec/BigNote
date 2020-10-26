package ru.stplab.bignote.data

import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.data.provider.DataProvider
import ru.stplab.bignote.data.provider.FireStoreProvider

object Repository {

    private val provider: DataProvider = FireStoreProvider()

    fun getNotes() = provider.subscribeToAllNotes()
    fun saveNote(note: Note) = provider.saveNote(note)
    fun getNoteById(id: String) = provider.getNoteById(id)
    fun getCurrentUser() = provider.getCurrentUser()
}