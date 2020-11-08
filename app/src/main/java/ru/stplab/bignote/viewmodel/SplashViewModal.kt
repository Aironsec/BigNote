package ru.stplab.bignote.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.stplab.bignote.data.Repository
import ru.stplab.bignote.data.errors.NoAuthException
import ru.stplab.bignote.viewmodel.base.BaseViewModel

class SplashViewModel(private val repository: Repository) : BaseViewModel<Boolean>() {

    @ExperimentalCoroutinesApi
    fun requestUser() = launch {
        repository.getCurrentUser()?.let {
            setData(true)
        } ?: setError(NoAuthException())
    }
}