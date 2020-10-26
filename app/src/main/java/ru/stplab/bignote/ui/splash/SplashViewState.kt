package ru.stplab.bignote.ui.splash

import ru.stplab.bignote.ui.base.BaseViewState

class SplashViewState(authenticated: Boolean? = null, error: Throwable? = null) : BaseViewState<Boolean?>(authenticated, error)