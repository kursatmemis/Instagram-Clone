package com.kursatmemis.instagram_clone.view.auth.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kursatmemis.instagram_clone.R
import com.kursatmemis.instagram_clone.databinding.FragmentAuthBinding
import com.kursatmemis.instagram_clone.util.closeKeyboard
import com.kursatmemis.instagram_clone.util.hideProgressBar
import com.kursatmemis.instagram_clone.util.showProgressBar
import com.kursatmemis.instagram_clone.util.showToastMessage
import com.kursatmemis.instagram_clone.view.BaseFragment
import com.kursatmemis.instagram_clone.view.main.activity.MainActivity
import com.kursatmemis.instagram_clone.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : BaseFragment<FragmentAuthBinding>() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun createBindingObject(inflater: LayoutInflater, parent: ViewGroup?): FragmentAuthBinding {
        return FragmentAuthBinding.inflate(inflater, parent, false)
    }

    override fun setupUI() {
        setupBtnSignUpClickListener()
        setupBtnSignInClickListener()
        observeLiveData()
    }

    private fun setupBtnSignUpClickListener() {
        binding.btnSignUp.setOnClickListener {
            closeKeyboard(requireActivity(), requireContext())
            showProgressBar(binding.progressBar)
            startAuthentication(true)
        }
    }

    private fun setupBtnSignInClickListener() {
        binding.btnSignIn.setOnClickListener {
            closeKeyboard(requireActivity(), requireContext())
            showProgressBar(binding.progressBar)
            startAuthentication(false)
        }
    }

    private fun startAuthentication(isSignUp: Boolean) {
        val authPair = getAuthPair()
        if (!isAuthPairValid(authPair)) {
            showToastMessage(requireContext(), "Email or password is empty.")
            hideProgressBar(binding.progressBar)
        } else {
            if (isSignUp) {
                authViewModel.signUp(authPair.first, authPair.second)
            } else {
                authViewModel.signIn(authPair.first, authPair.second)
            }
        }

    }

    private fun getAuthPair(): Pair<String, String> {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        return Pair(email, password)
    }

    private fun isAuthPairValid(authPair: Pair<String, String>) : Boolean {
        return authPair.first.isNotEmpty() && authPair.second.isNotEmpty()
    }

    private fun observeLiveData() {
        authViewModel.authResult.observe(viewLifecycleOwner) {
            val authResult = it
            val isSuccessful = authResult.isSuccessful

            if (isSuccessful) {
                hideProgressBar(binding.progressBar)
                navigateToMain()
            } else {
                val errorMessage = authResult.errorMessage
                errorMessage?.let {
                    val message = it
                    showToastMessage(requireContext(), message)
                }
                hideProgressBar(binding.progressBar)
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}