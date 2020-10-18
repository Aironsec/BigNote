package ru.stplab.bignote.data.provider

import androidx.lifecycle.LiveData
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.data.model.NoteResult

interface DataProvider {

    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note) : LiveData<NoteResult>

}
