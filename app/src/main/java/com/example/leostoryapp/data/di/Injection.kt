package com.example.leostoryapp.data.di

import com.example.leostoryapp.data.remote.retrofit.ApiConfig
import com.example.leostoryapp.data.repository.UploadRepository
import com.example.leostoryapp.ui.addstory.AddStoryViewModelFactory

object Injection {
    private fun provideUploadRepository(): UploadRepository {
        val apiService = ApiConfig.instance
        return UploadRepository(apiService)
    }
    fun AddStoryViewModelFactory(): AddStoryViewModelFactory {
        val repository = provideUploadRepository()
        return AddStoryViewModelFactory(repository)
    }
}
