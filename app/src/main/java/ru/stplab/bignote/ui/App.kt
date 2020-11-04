package ru.stplab.bignote.ui

import android.app.Application
import org.koin.android.ext.android.startKoin
import ru.stplab.bignote.di.appModule
import ru.stplab.bignote.di.mainModule
import ru.stplab.bignote.di.noteModule
import ru.stplab.bignote.di.splashModule

class App: Application() {

    companion object {
        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }

}