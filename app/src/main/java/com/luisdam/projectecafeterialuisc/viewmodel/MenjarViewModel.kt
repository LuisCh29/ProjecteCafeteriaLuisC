package com.luisdam.projectecafeterialuisc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisdam.projectecafeterialuisc.data.repository.ProducteRepository
import com.luisdam.projectecafeterialuisc.data.db.ProducteEntity
import com.luisdam.projectecafeterialuisc.model.CategoriaProducte
import com.luisdam.projectecafeterialuisc.model.Producte
import kotlinx.coroutines.launch

class MenjarViewModel(private val repository: ProducteRepository) : ViewModel() {

    val menjarProductes: LiveData<List<ProducteEntity>> = repository.getProductesByCategoria(CategoriaProducte.MENJAR)

    init {
        viewModelScope.launch {
            repository.insertInitialProductes()
        }
    }
}