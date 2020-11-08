package ru.stplab.bignote.ui.note

import ru.stplab.bignote.data.model.Note

data class NoteData(val note: Note? = null, val isDelete: Boolean = false)