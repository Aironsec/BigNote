package ru.stplab.bignote.ui.main

import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.ui.base.BaseViewState

class MainViewState(notes: List<Note>? = null, error: Throwable? = null) :
    BaseViewState<List<Note>?>(notes, error)