package ru.stplab.bignote.viewmodel

import androidx.lifecycle.Observer
import ru.stplab.bignote.data.Repository
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.data.model.NoteResult
import ru.stplab.bignote.ui.main.MainViewState
import ru.stplab.bignote.viewmodel.base.BaseViewModel

class MainViewModel: BaseViewModel<List<Note>?, MainViewState>()  {

    private val notesObserver = Observer { result: NoteResult? ->
        result ?: return@Observer
        when (result) {
            is NoteResult.Success<*> -> viewStateLiveData.value = MainViewState(result.data as? List<Note>)
            is NoteResult.Error -> viewStateLiveData.value = MainViewState(error = result.error)
        }
    }

    private val repositoryNotes = Repository.getNotes()

    init {
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        super.onCleared()
        repositoryNotes.removeObserver(notesObserver)
    }
}