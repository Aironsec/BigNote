package ru.stplab.bignote.ui.note

import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.ui.base.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)