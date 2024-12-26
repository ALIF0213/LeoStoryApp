package com.example.leostoryapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leostoryapp.data.remote.retrofit.ApiService
import com.example.leostoryapp.data.remote.retrofit.LoginRequest
import com.example.leostoryapp.utils.ResultState
import kotlinx.coroutines.launch

class LoginViewModel(private val apiService: ApiService) : ViewModel() {

    private val _loginResult = MutableLiveData<ResultState<String>>()
    val loginResult: LiveData<ResultState<String>> get() = _loginResult

    fun login(email: String, password: String) {
        _loginResult.value = ResultState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    val token = response.body()?.loginResult?.token
                    _loginResult.value = ResultState.Success(token ?: "Login Successful, but token is missing")
                } else {
                    _loginResult.value = ResultState.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _loginResult.value = ResultState.Error("Login Failed: ${e.message}")
            }
        }
    }
}
