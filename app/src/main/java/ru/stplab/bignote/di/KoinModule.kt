package ru.stplab.bignote.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.stplab.bignote.data.Repository
import ru.stplab.bignote.data.provider.DataProvider
import ru.stplab.bignote.data.provider.FireStoreProvider
import ru.stplab.bignote.viewmodel.MainViewModel
import ru.stplab.bignote.viewmodel.NoteViewModel
import ru.stplab.bignote.viewmodel.SplashViewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind DataProvider::class
    single { Repository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}