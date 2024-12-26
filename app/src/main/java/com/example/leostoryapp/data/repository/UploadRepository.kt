package com.example.leostoryapp.data.repository

import com.example.leostoryapp.data.remote.response.AddNewStoryResponse
import com.example.leostoryapp.data.remote.retrofit.ApiService
import com.example.leostoryapp.utils.ResultState
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadRepository(private val apiService: ApiService) {

    suspend fun addStory(
        token: String,
        description: RequestBody,
        photo: MultipartBody.Part
    ): ResultState<AddNewStoryResponse> {
        return try {
            val response = apiService.addStory(description, photo, token)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    ResultState.Success(responseBody)
                } else {
                    ResultState.Error("Response body is null")
                }
            } else {
                ResultState.Error("Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Terjadi kesalahan jaringan")
        }
    }
}
