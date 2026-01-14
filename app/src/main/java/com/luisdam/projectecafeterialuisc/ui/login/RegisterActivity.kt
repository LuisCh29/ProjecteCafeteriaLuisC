package com.luisdam.projectecafeterialuisc.ui.login

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.luisdam.projectecafeterialuisc.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    companion object {
        private const val PREFS_NAME = "CafeteriaPrefs"
        private const val KEY_PASSWORD_PREFIX = "password_"
    }

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
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        if (validateInput(username, password, confirmPassword)) {
            if (registerUserInPrefs(username, password)) {
                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(username: String, password: String, confirmPassword: String): Boolean {
        var isValid = true

        if (username.isEmpty()) {
            binding.etUsername.error = "Introduce un usuario"
            isValid = false
        } else {
            binding.etUsername.error = null
        }

        if (password.length < 4) {
            binding.etPassword.error = "Mínimo 4 caracteres"
            isValid = false
        } else {
            binding.etPassword.error = null
        }

        if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Las contraseñas no coinciden"
            isValid = false
        } else {
            binding.etConfirmPassword.error = null
        }

        return isValid
    }

    private fun registerUserInPrefs(username: String, password: String): Boolean {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val existingPassword = prefs.getString("${KEY_PASSWORD_PREFIX}$username", null)
        if (existingPassword != null) {
            return false
        }

        prefs.edit()
            .putString("${KEY_PASSWORD_PREFIX}$username", password)
            .apply()

        return true
    }
}