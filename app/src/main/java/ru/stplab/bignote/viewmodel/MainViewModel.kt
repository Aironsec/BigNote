package ru.stplab.bignote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.stplab.bignote.data.Repository
import ru.stplab.bignote.ui.main.MainViewState

class MainViewModel: ViewModel() {

    private val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        Repository.getNotes().observeForever {
            viewStateLiveData.value = viewStateLiveData.value?.copy(notes = it) ?: MainViewState(it)
        }
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData
}