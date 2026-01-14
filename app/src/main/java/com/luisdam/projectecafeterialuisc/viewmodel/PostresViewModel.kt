package com.luisdam.projectecafeterialuisc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisdam.projectecafeterialuisc.data.repository.ProducteRepository
import com.luisdam.projectecafeterialuisc.data.db.ProducteEntity
import com.luisdam.projectecafeterialuisc.model.CategoriaProducte
import kotlinx.coroutines.launch

class PostresViewModel(private val repository: ProducteRepository) : ViewModel() {

    val postresProductes: LiveData<List<ProducteEntity>> = repository.getProductesByCategoria(CategoriaProducte.POSTRES)

    init {
        viewModelScope.launch {
            repository.insertInitialProductes()
        }
    }
}