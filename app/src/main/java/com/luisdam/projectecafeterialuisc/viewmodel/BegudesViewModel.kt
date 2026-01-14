package com.luisdam.projectecafeterialuisc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisdam.projectecafeterialuisc.data.repository.ProducteRepository
import com.luisdam.projectecafeterialuisc.data.db.ProducteEntity
import com.luisdam.projectecafeterialuisc.model.CategoriaProducte
import kotlinx.coroutines.launch

class BegudesViewModel(private val repository: ProducteRepository) : ViewModel() {

    val begudesProductes: LiveData<List<ProducteEntity>> = repository.getProductesByCategoria(CategoriaProducte.BEGUDES)

    init {
        viewModelScope.launch {
            repository.insertInitialProductes()
        }
    }
}