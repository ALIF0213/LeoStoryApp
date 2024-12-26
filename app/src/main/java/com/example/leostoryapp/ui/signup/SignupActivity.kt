package com.example.leostoryapp.ui.signup

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.leostoryapp.data.remote.retrofit.ApiConfig
import com.example.leostoryapp.utils.ResultState
import com.example.leostoryapp.databinding.ActivitySignupBinding
import com.example.leostoryapp.ui.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ObjectAnimator.ofFloat(binding.edRegisterNameLayout, "translationY", 300f, 0f).apply {
            duration = 800
            start()
        }
        ObjectAnimator.ofFloat(binding.edRegisterEmailLayout, "translationY", 300f, 0f).apply {
            duration = 800
            start()
        }
        ObjectAnimator.ofFloat(binding.edRegisterPasswordLayout, "translationY", 300f, 0f).apply {
            duration = 800
            start()
        }
        ObjectAnimator.ofFloat(binding.btnRegister, "alpha", 0f, 1f).apply {
            duration = 1000
            start()
        }

        val footerText = binding.footerText

        footerText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        viewModel = SignupViewModel(ApiConfig.instance)

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.length >= 8) {
                viewModel.register(name, email, password)
                binding.progressBar.visibility = View.VISIBLE
                Toast.makeText(this, "Signup Success!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.registerResult.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnRegister.isEnabled = false
                }
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(this, result.data, Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
