package ru.stplab.bignote.viewmodel

import ru.stplab.bignote.data.Repository
import ru.stplab.bignote.data.errors.NoAuthException
import ru.stplab.bignote.ui.splash.SplashViewState
import ru.stplab.bignote.viewmodel.base.BaseViewModel

class SplashViewModel(private val repository: Repository) : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        repository.getCurrentUser().observeForever {
            viewStateLiveData.value = it?.let { SplashViewState(true) } ?: SplashViewState(error = NoAuthException())
        }
    }
}