package ru.stplab.bignote.data.provider

import androidx.lifecycle.LiveData
import kotlinx.coroutines.channels.ReceiveChannel
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.data.model.NoteResult
import ru.stplab.bignote.data.model.User

interface DataProvider {

    fun subscribeToAllNotes(): ReceiveChannel<NoteResult>
    suspend fun getNoteById(id: String): Note
    suspend fun saveNote(note: Note) : Note
    suspend fun getCurrentUser() : User?
    suspend fun deleteNote(id: String)
}
