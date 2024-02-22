package com.kursatmemis.instagram_clone.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat

fun showToastMessage(context: Context, message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(context, message, duration).show()
}

fun showProgressBar(progressBar: ProgressBar) {
    progressBar.visibility = View.VISIBLE
}

fun hideProgressBar(progressBar: ProgressBar) {
    progressBar.visibility = View.INVISIBLE
}

fun closeKeyboard(activity: Activity, context: Context) {
    val view: View? = activity.currentFocus
    if (view != null) {
        val imm = ContextCompat.getSystemService(context, InputMethodManager::class.java)
        imm!!.hideSoftInputFromWindow(view.windowToken, 0)
    }
}