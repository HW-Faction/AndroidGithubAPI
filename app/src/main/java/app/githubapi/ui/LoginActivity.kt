package app.githubapi.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.githubapi.R
import app.githubapi.repository.MainRepository
import kotlinx.android.synthetic.main.activity_login.*
import app.githubapi.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private val TAG = "LoginActivity.kt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        progress_circular_login.visibility = View.VISIBLE
        btnLogin.visibility = View.GONE
        viewModel.login(this, inputEmail.text.toString())
            .observe(this, {
                Log.d(TAG, "authResult:: name: " + it.user?.displayName)
                Intent(this, MainActivity::class.java).apply {
                    startActivity(this)
                }
            })
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}