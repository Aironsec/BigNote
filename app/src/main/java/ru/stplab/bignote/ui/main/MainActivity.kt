package ru.stplab.bignote.ui.main

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.stplab.bignote.R
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.ui.base.BaseActivity
import ru.stplab.bignote.ui.note.NoteActivity
import ru.stplab.bignote.ui.splash.SplashActivity
import ru.stplab.bignote.viewmodel.MainViewModel

class MainActivity : BaseActivity<List<Note>?>() {

    companion object {
        fun start(context: Context) = Intent(context, MainActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    override val viewModel: MainViewModel by viewModel()
    override val layoutRes = R.layout.activity_main
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar_main)
        adapter = MainAdapter {
            NoteActivity.startIntent(this, it.id)
        }

        note_recycle.adapter = adapter
        fab.setOnClickListener {
            NoteActivity.startIntent(this)
        }
    }

    override fun renderData(data: List<Note>?) {
        data?.let { adapter.notes = it }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
        MenuInflater(this).inflate(R.menu.main_menu, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.logout -> showLogoutDialog().let { true }
            else -> false
        }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.logout_title)
            .setMessage(R.string.logout_message)
            .setPositiveButton(R.string.logout_ok) { dialog, which ->
                logout()
            }
            .setNegativeButton(R.string.logout_cancel) { dialog, which -> dialog.dismiss() }
            .show()
    }

    fun logout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                SplashActivity.start(this)
                finish()
            }
    }
}