package com.example.leostoryapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leostoryapp.data.remote.response.Story
import com.example.leostoryapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _storyDetail = MutableLiveData<Story?>()
    val storyDetail: LiveData<Story?> get() = _storyDetail

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchStoryDetail(storyId: String, token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.instance.getDetailStory(storyId, "Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    _storyDetail.value = response.body()?.story
                    _isLoading.value = false
                    _error.value = null
                } else {
                    _isLoading.value = false
                    _error.value = "Gagal memuat detail cerita."
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = "Terjadi kesalahan: ${e.message}"
            }
        }
    }
}

