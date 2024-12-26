package com.example.leostoryapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leostoryapp.data.remote.retrofit.ApiService
import com.example.leostoryapp.data.remote.retrofit.RegisterRequest
import com.example.leostoryapp.utils.ResultState
import kotlinx.coroutines.launch

class SignupViewModel(private val apiService: ApiService) : ViewModel() {
    private val _registerResult = MutableLiveData<ResultState<String>>()
    val registerResult: LiveData<ResultState<String>> get() = _registerResult

    fun register(name: String, email: String, password: String) {
        _registerResult.value = ResultState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.register(RegisterRequest(name, email, password))
                if (response.isSuccessful && response.body()?.error == false) {
                    _registerResult.value = ResultState.Success(response.body()?.message ?: "Registration Successful")
                } else {
                    _registerResult.value = ResultState.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _registerResult.value = ResultState.Error("Registration Failed: ${e.message}")
            }
        }
    }
}
