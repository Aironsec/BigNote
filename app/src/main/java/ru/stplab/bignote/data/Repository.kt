package ru.stplab.bignote.data

import androidx.lifecycle.MutableLiveData
import ru.stplab.bignote.data.model.Color
import ru.stplab.bignote.data.model.Note
import java.util.*

object Repository {

    private val notesLiveData = MutableLiveData<List<Note>>()

    private val notes = mutableListOf(
        Note(UUID.randomUUID().toString(), "Заметка 1", "Техт заметки 1", Color.WHITE),
        Note(
            UUID.randomUUID().toString(),
            "Заметка 2",
            "Техт заметки 2. Как прекрасен этот мир посмотри",
            Color.RED
        ),
        Note(UUID.randomUUID().toString(), "Заметка 3", "Техт заметки 3", Color.VIOLET),
        Note(UUID.randomUUID().toString(), "Заметка 4", "Техт заметки 4", Color.BLUE),
        Note(UUID.randomUUID().toString(), "Заметка 5", "Техт заметки 5", Color.GREEN),
        Note(UUID.randomUUID().toString(), "Заметка 6", "Техт заметки 6", Color.YELLOW),
        Note(UUID.randomUUID().toString(), "Заметка 7", "Техт заметки 7", Color.WHITE)
    )

    init {
        notesLiveData.value = notes
    }

    fun saveNote(note: Note) {
        addOrReplace(note)
        notesLiveData.value = notes
    }

    fun addOrReplace(note: Note) {
        for (i in notes.indices) {
            if (notes[i] == note) {
                notes[i] = note
                return
            }
        }
        notes.add(note)
    }

    fun getNotes() = notesLiveData
}