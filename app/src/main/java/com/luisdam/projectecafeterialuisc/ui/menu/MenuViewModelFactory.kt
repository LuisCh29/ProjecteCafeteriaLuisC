package com.luisdam.projectecafeterialuisc.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luisdam.projectecafeterialuisc.data.repository.ProducteRepository
import com.luisdam.projectecafeterialuisc.viewmodel.BegudesViewModel
import com.luisdam.projectecafeterialuisc.viewmodel.MenjarViewModel
import com.luisdam.projectecafeterialuisc.viewmodel.PostresViewModel

class MenuViewModelFactory(private val repository: ProducteRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MenjarViewModel::class.java) ->
                MenjarViewModel(repository) as T
            modelClass.isAssignableFrom(BegudesViewModel::class.java) ->
                BegudesViewModel(repository) as T
            modelClass.isAssignableFrom(PostresViewModel::class.java) ->
                PostresViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}