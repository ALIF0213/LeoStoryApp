package com.example.leostoryapp.ui.login

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.leostoryapp.data.pref.UserPreference
import com.example.leostoryapp.data.remote.retrofit.ApiConfig
import com.example.leostoryapp.utils.ResultState
import com.example.leostoryapp.databinding.ActivityLoginBinding
import com.example.leostoryapp.ui.main.MainActivity
import com.example.leostoryapp.ui.signup.SignupActivity

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ObjectAnimator.ofFloat(binding.edLoginEmailLayout, "translationX", 300f, 0f).apply {
            duration = 1600
            start()
        }
        ObjectAnimator.ofFloat(binding.edLoginPasswordLayout, "translationY", 300f, 0f).apply {
            duration = 1600
            start()
        }

        ObjectAnimator.ofFloat(binding.loginButton, "alpha", 0f, 1f).apply {
            duration = 1600
            start()
        }

        viewModel = LoginViewModel(ApiConfig.instance)

        val footerText = binding.footerText

        footerText.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.loginButton.isEnabled = false
                }
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.isEnabled = true
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                    val userPreference = UserPreference(this)
                    userPreference.saveSession(result.data)

                    val intent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(intent)
                    finish()
                }
                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.isEnabled = true
                    Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}
