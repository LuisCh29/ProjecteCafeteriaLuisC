package com.luisdam.projectecafeterialuisc.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.luisdam.projectecafeterialuisc.data.firebase.FirebaseAuthService
import com.luisdam.projectecafeterialuisc.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authService = FirebaseAuthService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun registerUser() {
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        if (validateInput(username, email, password, confirmPassword)) {
            lifecycleScope.launch {
                binding.progressBar.visibility = android.view.View.VISIBLE

                val result = authService.register(email, password, username)

                binding.progressBar.visibility = android.view.View.GONE

                if (result.isSuccess) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Usuari registrat correctament",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Error: ${result.exceptionOrNull()?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun validateInput(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true

        if (username.isEmpty()) {
            binding.etUsername.error = "Introdueix un nom d'usuari"
            isValid = false
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "Introdueix el correu electrònic"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Correu electrònic invàlid"
            isValid = false
        }

        if (password.length < 6) {
            binding.etPassword.error = "Mínim 6 caràcters"
            isValid = false
        }

        if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Les contrasenyes no coincideixen"
            isValid = false
        }

        return isValid
    }
}