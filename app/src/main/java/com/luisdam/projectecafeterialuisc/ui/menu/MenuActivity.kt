package com.luisdam.projectecafeterialuisc.ui.menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.luisdam.projectecafeterialuisc.R
import com.luisdam.projectecafeterialuisc.databinding.ActivityMenuBinding
import com.luisdam.projectecafeterialuisc.viewmodel.SharedViewModel

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("USERNAME") ?: "Usuari"

        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        sharedViewModel.setUsuariActual(username)

        setupNavigation()

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
                else -> false
            }
        }

        navView.selectedItemId = R.id.nav_menjar
    }
}