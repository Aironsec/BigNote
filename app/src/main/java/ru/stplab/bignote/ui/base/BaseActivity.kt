package ru.stplab.bignote.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.stplab.bignote.R
import ru.stplab.bignote.viewmodel.base.BaseViewModel

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {
    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        viewModel.getViewState().observe(this, { it ->
            it ?: return@observe
            it.error?.let {
                renderError(it)
                return@observe
            }
            renderData(it.data)
        })
    }

    protected fun renderError(error: Throwable) {
        error.message?.let {
            showError(it)
        }
    }

    protected fun showError(text: String) {
        val snackbar = Snackbar.make(note_recycle, text, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.ok_bth_title) { snackbar.dismiss() }
            .show()
    }

    abstract fun renderData(data: T)
}