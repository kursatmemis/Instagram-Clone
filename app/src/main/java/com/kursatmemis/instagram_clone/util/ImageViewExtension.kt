package com.kursatmemis.instagram_clone.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kursatmemis.instagram_clone.R

fun ImageView.downloadFromUrl(url: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.ic_launcher_background)

    progressDrawable.start()

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .listener(object : RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                progressDrawable.stop()
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                progressDrawable.stop()
                return false
            }

        })
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)

}

fun buildProgressDrawable(context: Context) : CircularProgressDrawable {
    val progressDrawable = CircularProgressDrawable(context).apply {
        strokeWidth = 7f
        centerRadius = 40f
    }
    return progressDrawable
}