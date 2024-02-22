package com.kursatmemis.instagram_clone.model

data class FirebaseResult(val isSuccessful: Boolean, val errorMessage: String?)

sealed class FetchDataResult {
    data class Successful(val postList: ArrayList<Post>) : FetchDataResult()
    data class Failure(val errorMessage: String) : FetchDataResult()
}