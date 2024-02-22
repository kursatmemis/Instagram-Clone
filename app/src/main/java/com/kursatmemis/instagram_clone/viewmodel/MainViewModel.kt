package com.kursatmemis.instagram_clone.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kursatmemis.instagram_clone.model.FetchDataResult
import com.kursatmemis.instagram_clone.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStore: FirebaseFirestore
) : ViewModel() {

    private val _fetchDataResult = MutableLiveData<FetchDataResult>()
    val fetchDataResult get() = _fetchDataResult

    fun fetchData() {

        firebaseStore.collection("posts").get()
            .addOnSuccessListener {
                val postList = ArrayList<Post>()

                val documents = it.documents
                for (document in documents) {
                    val post = document.toObject(Post::class.java)
                    post?.let {
                        postList.add(post)
                    }
                }
                _fetchDataResult.value = FetchDataResult.Successful(postList)
            }
            .addOnFailureListener {
                val errorMessage = it.localizedMessage
                _fetchDataResult.value = FetchDataResult.Failure(errorMessage ?: "Unknown Error!")
            }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

}