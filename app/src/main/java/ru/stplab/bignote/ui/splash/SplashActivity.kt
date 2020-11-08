package ru.stplab.bignote.ui.splash

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import ru.stplab.bignote.ui.base.BaseActivity
import ru.stplab.bignote.ui.main.MainActivity
import ru.stplab.bignote.viewmodel.SplashViewModel

class SplashActivity : BaseActivity<Boolean>() {

    companion object {
        fun start(context: Context) = Intent(context, SplashActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    override val viewModel: SplashViewModel by viewModel()
    override val layoutRes = null

    @ExperimentalCoroutinesApi
    override fun onResume() {
        super.onResume()
        viewModel.requestUser()
    }

    override fun renderData(data: Boolean) {
        data.takeIf { it }?.let {
            startMainActivity()
        }
    }

    private fun startMainActivity(){
        MainActivity.start(this)
        finish()
    }
}