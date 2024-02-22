package com.kursatmemis.instagram_clone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.kursatmemis.instagram_clone.R
import com.kursatmemis.instagram_clone.databinding.PostItemBinding
import com.kursatmemis.instagram_clone.model.Post
import com.kursatmemis.instagram_clone.util.buildProgressDrawable
import com.kursatmemis.instagram_clone.util.downloadFromUrl
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class PostAdapter @Inject constructor(
    @ActivityContext context: Context,
    private val postList: ArrayList<Post>
) : ArrayAdapter<Post>(context, R.layout.post_item, postList) {

    private lateinit var binding: PostItemBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            binding = PostItemBinding.inflate(layoutInflater, parent, false)
        } else {
            binding = PostItemBinding.bind(convertView)
        }

        val post = postList[position]
        binding.userEmailTextView.text = post.userEmail
        binding.commentTextView.text = post.comment
        binding.dateTextView.text = post.timestamp?.toDate().toString()

        val imageUrl = post.postImageUrl
        binding.imageView.downloadFromUrl(imageUrl, buildProgressDrawable(context))

        return binding.root
    }

    fun updateAdapter(newPostList: ArrayList<Post>) {
        clear()
        addAll(newPostList)
        notifyDataSetChanged()
    }

}