package ru.stplab.bignote.ui.note

import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.ui.base.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null) : BaseViewState<NoteViewState.Data>(data, error) {
    class Data(val note: Note? = null, val isDelete: Boolean = false)
}