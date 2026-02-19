package com.luisdam.projectecafeterialuisc.ui.menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.luisdam.projectecafeterialuisc.R
import com.luisdam.projectecafeterialuisc.data.firebase.FirebaseAuthService
import com.luisdam.projectecafeterialuisc.databinding.ActivityMenuBinding
import com.luisdam.projectecafeterialuisc.viewmodel.SharedViewModel

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var sharedViewModel: SharedViewModel
    private val authService = FirebaseAuthService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar SharedViewModel
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        // Configurar usuario desde Firebase Authentication
        val currentUser = authService.currentUser
        currentUser?.let { user ->
            val nom = user.displayName ?: user.email?.split("@")?.first() ?: "Usuari"
            val id = user.uid
            val email = user.email ?: ""

            sharedViewModel.setUsuariActual(nom, id, email)
        }

        // Configurar navegación
        setupNavigation()

        // Cargar primer fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MenjarFragment())
                .commit()
        }
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.bottomNavigation

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_menjar -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, MenjarFragment())
                        .commit()
                    true
                }
                R.id.nav_begudes -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, BegudesFragment())
                        .commit()
                    true
                }
                R.id.nav_postres -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, PostresFragment())
                        .commit()
                    true
                }
                R.id.nav_cistella -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, CistellaFragment())
                        .commit()
                    true
                }
                R.id.nav_historial -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, HistorialFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }

        navView.selectedItemId = R.id.nav_menjar
    }
}