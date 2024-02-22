package com.kursatmemis.instagram_clone.view.auth.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.kursatmemis.instagram_clone.R
import com.kursatmemis.instagram_clone.view.main.activity.MainActivity
import com.kursatmemis.instagram_clone.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
    }

    public override fun onStart() {
        super.onStart()
        authViewModel.isUserSignedIn()
        observeLiveData()
    }

    private fun observeLiveData() {
        authViewModel.isUserSignedIn.observe(this) {
            val isUserSignedIn = it
            if (isUserSignedIn) {
                navigateToMain()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}