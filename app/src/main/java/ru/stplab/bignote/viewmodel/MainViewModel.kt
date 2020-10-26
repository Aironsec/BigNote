package ru.stplab.bignote.viewmodel

import androidx.lifecycle.Observer
import ru.stplab.bignote.data.Repository
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.data.model.NoteResult
import ru.stplab.bignote.ui.main.MainViewState
import ru.stplab.bignote.viewmodel.base.BaseViewModel

class MainViewModel: BaseViewModel<List<Note>?, MainViewState>()  {

    private val repositoryNotes = Repository.getNotes()
    private val notesObserver = object : Observer<NoteResult?> {
        override fun onChanged(result: NoteResult?) {
            result ?: return
            when (result) {
                is NoteResult.Success<*> -> viewStateLiveData.value = MainViewState(result.data as? List<Note>)
                is NoteResult.Error -> viewStateLiveData.value = MainViewState(error = result.error)
            }
            // TODO: 26.10.2020 при удалении слушателя заметки не обновляются
//            repositoryNotes.removeObserver(this)
        }
    }

    init {
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        super.onCleared()
        repositoryNotes.removeObserver(notesObserver)
    }
}