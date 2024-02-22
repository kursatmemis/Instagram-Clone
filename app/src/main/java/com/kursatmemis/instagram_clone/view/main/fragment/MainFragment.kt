package com.kursatmemis.instagram_clone.view.main.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kursatmemis.instagram_clone.adapter.PostAdapter
import com.kursatmemis.instagram_clone.databinding.FragmentMainBinding
import com.kursatmemis.instagram_clone.model.FetchDataResult
import com.kursatmemis.instagram_clone.util.hideProgressBar
import com.kursatmemis.instagram_clone.util.showProgressBar
import com.kursatmemis.instagram_clone.util.showToastMessage
import com.kursatmemis.instagram_clone.view.BaseFragment
import com.kursatmemis.instagram_clone.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    @Inject
    lateinit var postAdapter: PostAdapter
    private val mainViewModel: MainViewModel by viewModels()


    override fun createBindingObject(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, parent, false)
    }

    override fun setupUI() {
        binding.postListView.adapter = postAdapter
        observeLiveData()
    }

    override fun onResume() {
        super.onResume()
        showProgressBar(binding.progressBar3)
        mainViewModel.fetchData()
    }

    private fun observeLiveData() {
        mainViewModel.fetchDataResult.observe(viewLifecycleOwner) {
            val fetchDataResult = it
            hideProgressBar(binding.progressBar3)
            when (fetchDataResult) {
                is FetchDataResult.Successful -> {
                    val newPostList = fetchDataResult.postList
                    postAdapter.updateAdapter(newPostList)
                }

                is FetchDataResult.Failure -> {
                    val errorMessage = fetchDataResult.errorMessage
                    showToastMessage(requireContext(), errorMessage)
                }
            }
        }
    }

}