package com.luisdam.projectecafeterialuisc.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.luisdam.projectecafeterialuisc.data.firebase.FirebaseAuthService
import com.luisdam.projectecafeterialuisc.databinding.ActivityLoginBinding
import com.luisdam.projectecafeterialuisc.ui.menu.MenuActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authService = FirebaseAuthService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (authService.isLoggedIn()) {
            navigateToMenu()
            return
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            loginUser()
        }

        binding.btnRegister.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun loginUser() {
        val email = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (validateInput(email, password)) {
            lifecycleScope.launch {
                binding.progressBar.visibility = android.view.View.VISIBLE

                val result = authService.login(email, password)

                binding.progressBar.visibility = android.view.View.GONE

                if (result.isSuccess) {
                    Toast.makeText(this@LoginActivity, "Login correcte", Toast.LENGTH_SHORT).show()
                    navigateToMenu()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error: ${result.exceptionOrNull()?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etUsername.error = "Introdueix el correu"
            return false
        }
        if (password.isEmpty()) {
            binding.etPassword.error = "Introdueix la contrasenya"
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etUsername.error = "Correu electrònic invàlid"
            return false
        }
        return true
    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}