package com.kursatmemis.instagram_clone.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.kursatmemis.instagram_clone.model.FirebaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _firebaseResult = MutableLiveData<FirebaseResult>()
    val authResult get() = _firebaseResult

    private val _isUserSignedIn = MutableLiveData<Boolean>()
    val isUserSignedIn get() = _isUserSignedIn

    fun signUp(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _firebaseResult.value = FirebaseResult(true, null)
            }
            .addOnFailureListener {
                val errorMessage = it.localizedMessage
                _firebaseResult.value = FirebaseResult(false, errorMessage)
            }
    }

    fun signIn(email: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _firebaseResult.value = FirebaseResult(true, null)
            }
            .addOnFailureListener {
                val errorMessage = it.localizedMessage
                _firebaseResult.value = FirebaseResult(false, errorMessage)
            }
    }

    fun isUserSignedIn() {
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser
        _isUserSignedIn.value = (currentUser != null)
    }

}