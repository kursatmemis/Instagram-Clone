package com.kursatmemis.instagram_clone.di.module

import com.kursatmemis.instagram_clone.model.Post
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
class AdapterModule {

    @Provides
    @FragmentScoped
    fun provideEmptyPostList() : ArrayList<Post> {
        return ArrayList()
    }

}