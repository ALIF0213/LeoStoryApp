package com.example.leostoryapp.ui.addstory

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.leostoryapp.R
import com.example.leostoryapp.data.di.Injection
import com.example.leostoryapp.data.pref.UserPreference
import com.example.leostoryapp.utils.ResultState
import com.example.leostoryapp.databinding.ActivityAddStoryBinding
import com.example.leostoryapp.utils.getImageUri
import com.example.leostoryapp.utils.reduceFileImage
import com.example.leostoryapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<AddStoryViewModel> {
        Injection.AddStoryViewModelFactory()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                showToast(getString(R.string.permission_denied_message))
            }
        }

    private fun allPermissionsGranted(): Boolean =
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Tambahkan cerita Anda"

        ObjectAnimator.ofFloat(binding.ivPreviewImage, "scaleX", 0.5f, 1f).apply {
            duration = 1000
            start()
        }

        ObjectAnimator.ofFloat(binding.ivPreviewImage, "scaleY", 0.5f, 1f).apply {
            duration = 1000
            start()
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        setupListeners()

        viewModel.storyUploadResult.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.buttonAdd.isEnabled = false
                }
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.buttonAdd.isEnabled = true
                    showToast(result.data.message)
                    setResult(RESULT_OK)
                    finish()
                }
                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.buttonAdd.isEnabled = true
                    showToast(result.error)
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnChooseFromGallery.setOnClickListener { openGallery() }
        binding.btnTakeFromCamera.setOnClickListener { openCamera() }
        binding.buttonAdd.setOnClickListener { uploadImage() }
    }

    private fun openGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            currentImageUri = it
            showImage()
        } ?: Log.d("PhotoPicker", "No image selected")
    }

    private fun openCamera() {
        val imageUri = getImageUri(this)
        currentImageUri = imageUri
        imageUri.let {
            launcherCamera.launch(it)
        }
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            showToast(getString(R.string.error_camera))
            currentImageUri = null
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivPreviewImage.setImageURI(it)
        }
    }

    private fun uploadImage() {
        val description = binding.edAddDescription.text.toString().trim()

        if (currentImageUri == null || description.isEmpty()) {
            showToast(getString(R.string.empty_fields_warning))
            return
        }

        try {
            val file = uriToFile(currentImageUri!!, this).reduceFileImage()
            Log.d("UploadFile", "File path: ${file.absolutePath}")

            val descriptionBody = description.toRequestBody("text/plain".toMediaType())
            val imageBody = file.asRequestBody("image/jpeg".toMediaType())
            val imagePart = MultipartBody.Part.createFormData("photo", file.name, imageBody)

            val userPreference = UserPreference(this)
            val token = userPreference.getToken()

            if (token.isNullOrEmpty()) {
                showToast("merekam token")
                Log.e("UploadError", "Token is missing!")
                return
            }

            viewModel.addStory("Bearer $token", descriptionBody, imagePart)
        } catch (e: Exception) {
            showToast("Error: ${e.message}")
            Log.e("UploadException", "Error: ${e.message}")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
