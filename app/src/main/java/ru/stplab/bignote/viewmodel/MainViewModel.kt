package ru.stplab.bignote.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.stplab.bignote.data.Repository
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.data.model.NoteResult
import ru.stplab.bignote.ui.main.MainViewState
import ru.stplab.bignote.viewmodel.base.BaseViewModel

@ExperimentalCoroutinesApi
class MainViewModel(repository: Repository) : BaseViewModel<List<Note>?>() {

    private val repositoryNotes = repository.getNotes()

    init {
        launch {
            repositoryNotes.consumeEach { result ->
                when (result) {
                    is NoteResult.Success<*> -> setData(result.data as? List<Note>)
                    is NoteResult.Error -> setError(result.error)
                }
            }
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        super.onCleared()
        repositoryNotes.cancel()
    }
}