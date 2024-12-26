package com.example.leostoryapp.ui.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leostoryapp.data.remote.response.AddNewStoryResponse
import com.example.leostoryapp.data.repository.UploadRepository
import com.example.leostoryapp.utils.ResultState
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: UploadRepository) : ViewModel() {


    private val _storyUploadResult = MutableLiveData<ResultState<AddNewStoryResponse>>()
    val storyUploadResult: LiveData<ResultState<AddNewStoryResponse>> get() = _storyUploadResult


    fun addStory(token: String, description: RequestBody, image: MultipartBody.Part) {
         viewModelScope.launch {
            _storyUploadResult.value = ResultState.Loading
            try {
                val result = repository.addStory(token, description, image)
                _storyUploadResult.value = result
            } catch (e: Exception) {
                _storyUploadResult.value = ResultState.Error(e.message ?: "Error uploading story")
            }
        }
    }
}
