package com.example.leostoryapp.data.remote.retrofit

import com.example.leostoryapp.data.remote.response.AddNewStoryResponse
import com.example.leostoryapp.data.remote.response.DetailStoryResponse
import com.example.leostoryapp.data.remote.response.LoginResponse
import com.example.leostoryapp.data.remote.response.RegisterResponse
import com.example.leostoryapp.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

data class RegisterRequest(val name: String, val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Response<AddNewStoryResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
        @Query("location") location: Int = 0
    ): Response<StoryResponse>

    @GET ("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<DetailStoryResponse>
}