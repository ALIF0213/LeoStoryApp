package com.example.leostoryapp.ui.addstory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.leostoryapp.data.repository.UploadRepository

@Suppress("UNCHECKED_CAST")
class AddStoryViewModelFactory(private val repository: UploadRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            AddStoryViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

