package com.kursatmemis.instagram_clone.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kursatmemis.instagram_clone.model.FirebaseResult
import com.kursatmemis.instagram_clone.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SharePostViewModel @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStore: FirebaseFirestore,
) : ViewModel() {

    private val _firebaseResult = MutableLiveData<FirebaseResult>()
    val firebaseResult get() = _firebaseResult

    fun sharePost(imageUri: Uri, comment: String) {

        val userEmail = firebaseAuth.currentUser!!.email!!
        val timestamp = Timestamp.now()
        val callback: (String) -> Unit = {
            val post = Post(userEmail, comment, timestamp, it)
            firebaseStore.collection("posts")
                .add(post)
                .addOnSuccessListener {
                    _firebaseResult.value = FirebaseResult(true, null)
                }
                .addOnFailureListener {
                    val errorMessage = it.localizedMessage
                    _firebaseResult.value = FirebaseResult(false, errorMessage)
                }
        }
        uploadImage(imageUri, callback)
    }

    private fun uploadImage(uri: Uri, callback: (downloadUrl: String) -> Unit) {
        val uuid = UUID.randomUUID()
        val storageRef = firebaseStorage.reference
        val imagesRef = storageRef.child("images/${uuid}.jpeg")

        imagesRef.putFile(uri)
            .addOnSuccessListener {
                    imagesRef.downloadUrl.addOnSuccessListener {
                        val downloadUrl = it.toString()
                        callback(downloadUrl)
                    }
            }
            .addOnFailureListener {
                val errorMessage = it.localizedMessage
                _firebaseResult.value = FirebaseResult(false, errorMessage)
            }
    }

}