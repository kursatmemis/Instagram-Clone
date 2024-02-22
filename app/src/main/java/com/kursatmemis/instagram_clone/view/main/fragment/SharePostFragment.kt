package com.kursatmemis.instagram_clone.view.main.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kursatmemis.instagram_clone.databinding.FragmentSharePostBinding
import com.kursatmemis.instagram_clone.model.SelectImageResult
import com.kursatmemis.instagram_clone.util.UploadImageFromGalleryUtil
import com.kursatmemis.instagram_clone.util.closeKeyboard
import com.kursatmemis.instagram_clone.util.hideProgressBar
import com.kursatmemis.instagram_clone.util.showProgressBar
import com.kursatmemis.instagram_clone.util.showToastMessage
import com.kursatmemis.instagram_clone.view.BaseFragment
import com.kursatmemis.instagram_clone.viewmodel.SharePostViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SharePostFragment : BaseFragment<FragmentSharePostBinding>() {

    @Inject
    lateinit var selectImageFromGalleryUtil: UploadImageFromGalleryUtil
    private val sharePostViewModel: SharePostViewModel by viewModels()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
    }

    override fun createBindingObject(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FragmentSharePostBinding {
        return FragmentSharePostBinding.inflate(inflater, parent, false)
    }

    override fun setupUI() {
        setupPostImageViewClickListener()
        setupShareButtonClickListener()
        observeLiveData()
    }

    private fun setupPostImageViewClickListener() {
        binding.postImageImageView.setOnClickListener {
            val selectImageResult = selectImageFromGalleryUtil.selectImage(requireActivity(), requireContext())

            when (selectImageResult) {
                SelectImageResult.PERMISSION_DENIED_WITH_RATIONALE -> {
                    showPermissionSnackbar()
                }
                SelectImageResult.PERMISSION_DENIED_WITHOUT_RATIONALE -> {
                    requestExternalStoragePermission()
                }
                SelectImageResult.PERMISSION_GRANTED -> {
                    launchGalleryIntent()
                }
            }
        }
    }

    private fun setupShareButtonClickListener() {
        binding.shareButton.setOnClickListener {
            closeKeyboard(requireActivity(), requireContext())
            if (imageUri == null) {
                showToastMessage(requireContext(), "Please select an image from gallery.")
            } else {
                showProgressBar(binding.progressBar2)
                val comment = binding.commentEditText.text.toString()
                imageUri?.let {
                    sharePostViewModel.sharePost(it, comment)
                }
            }

        }
    }

    private fun observeLiveData() {
        sharePostViewModel.firebaseResult.observe(viewLifecycleOwner) {
            hideProgressBar(binding.progressBar2)
            val firebaseResult = it
            if (firebaseResult.isSuccessful) {
                showToastMessage(requireContext(), "The post shared!")
                findNavController().popBackStack()
            } else {
                val errorMessage = firebaseResult.errorMessage
                errorMessage?.let {
                    showToastMessage(requireContext(), it)
                }
            }
        }
    }

    private fun showPermissionSnackbar() {
        Snackbar.make(
            binding.root,
            "You have to give a permission to select an image from your gallery.",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Give Permission") {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }.show()
    }

    private fun requestExternalStoragePermission() {
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun launchGalleryIntent() {
        val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intentToGallery)
    }

    private fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    imageUri = intentFromResult.data
                    binding.postImageImageView.setImageURI(imageUri)
                }
            }
        }

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { result ->
            if (result) {
                // Izin verilmiş.
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                // Izin verilmemiş.
                showToastMessage(requireContext(), "You have to give a permission.")
            }
        }
    }

}
