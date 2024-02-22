package com.kursatmemis.instagram_clone.model

import com.google.firebase.Timestamp

data class Post(
    val userEmail: String? = null,
    val comment: String? = null,
    val timestamp: Timestamp? = null,
    val postImageUrl: String? = null
)
