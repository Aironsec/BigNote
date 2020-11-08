package ru.stplab.bignote.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import ru.stplab.bignote.R
import ru.stplab.bignote.data.errors.NoAuthException
import ru.stplab.bignote.viewmodel.base.BaseViewModel
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity<T> : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Main + Job() }
    private lateinit var dataJob: Job
    private lateinit var errorJob: Job
    abstract val viewModel: BaseViewModel<T>
    abstract val layoutRes: Int?

    companion object {
        private const val RC_SIGN_IN = 1111
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let {
            setContentView(it)
        }
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        dataJob = launch {
            viewModel.getViewState().consumeEach {
                renderData(it)
            }
        }

        errorJob = launch {
            viewModel.getErrorChannel().consumeEach {
                renderError(it)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        dataJob.cancel()
        errorJob.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }

    private fun renderError(error: Throwable) {
        when (error) {
            is NoAuthException -> startLogin()
            else -> error.message?.let {
                showError(it)
            }
        }
    }

    private fun startLogin() {
        val providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val intent = AuthUI.getInstance().createSignInIntentBuilder()
            .setLogo(R.drawable.ic_notes)
            .setTheme(R.style.LoginTheme)
            .setAvailableProviders(providers)
            .build()

        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK) {
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showError(text: String) {
        Snackbar.make(note_recycle, text, Snackbar.LENGTH_INDEFINITE).apply {
            setAction(R.string.ok_bth_title) { dismiss() }
            show()
        }

    }

    abstract fun renderData(data: T)
}