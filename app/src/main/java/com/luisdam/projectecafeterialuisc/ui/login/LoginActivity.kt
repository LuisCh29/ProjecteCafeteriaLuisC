package com.luisdam.projectecafeterialuisc.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.luisdam.projectecafeterialuisc.databinding.ActivityLoginBinding
import com.luisdam.projectecafeterialuisc.ui.menu.MenuActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        private const val PREFS_NAME = "CafeteriaPrefs"
        private const val KEY_PASSWORD_PREFIX = "password_"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (validateInput(username, password)) {
            if (authenticateUser(username, password)) {
                saveCurrentUser(username)
                navigateToMenu(username)
            } else {
                Toast.makeText(this, "Usuari o contrasenya incorrectes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            binding.etUsername.error = "Introdueix l'usuari"
            return false
        }
        if (password.isEmpty()) {
            binding.etPassword.error = "Introdueix la contrasenya"
            return false
        }
        return true
    }

    private fun authenticateUser(username: String, password: String): Boolean {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        if (username == "admin" && password == "1234") {
            return true
        }

        val savedPassword = prefs.getString("${KEY_PASSWORD_PREFIX}$username", null)
        return savedPassword == password
    }

    private fun saveCurrentUser(username: String) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString("currentUser", username).apply()
    }

    private fun navigateToMenu(username: String) {
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("USERNAME", username)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}