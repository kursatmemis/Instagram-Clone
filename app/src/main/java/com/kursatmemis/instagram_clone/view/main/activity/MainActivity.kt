package com.kursatmemis.instagram_clone.view.main.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.navigation.fragment.findNavController
import com.kursatmemis.instagram_clone.R
import com.kursatmemis.instagram_clone.databinding.ActivityMainBinding
import com.kursatmemis.instagram_clone.view.auth.activity.AuthenticationActivity
import com.kursatmemis.instagram_clone.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.main_act_menu)
        binding.toolbar.title = "Main Activity"
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_share_post -> {
                    navigateToSharePostFragment()
                    true
                }

                R.id.menu_sign_out -> {
                    signOut()
                    navigateToAuth()
                    true
                }

                else -> {false}
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_act_menu, menu)
        return true
    }

    private fun signOut() {
        mainViewModel.signOut()
    }

    private fun navigateToAuth() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToSharePostFragment() {
        val navController = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2)?.findNavController()
        navController?.navigate(R.id.action_mainFragment_to_sharePostFragment)
    }

}