package com.luisdam.projectecafeterialuisc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luisdam.projectecafeterialuisc.model.Producte

class SharedViewModel : ViewModel() {

    private val _cistellaItems = MutableLiveData<MutableList<Producte>>(mutableListOf())
    val cistellaItems: LiveData<MutableList<Producte>> = _cistellaItems

    private val _usuariActual = MutableLiveData<String?>()
    val usuariActual: LiveData<String?> = _usuariActual

    fun afegirACistella(producte: Producte) {
        val currentList = _cistellaItems.value ?: mutableListOf()
        currentList.add(producte)
        _cistellaItems.value = currentList
    }

    fun buidarCistella() {
        _cistellaItems.value = mutableListOf()
    }

    fun setUsuariActual(username: String) {
        _usuariActual.value = username
    }

    fun getPreuTotal(): Double {
        return _cistellaItems.value?.sumOf { it.preu } ?: 0.0
    }

    fun getQuantitatTotal(): Int {
        return _cistellaItems.value?.size ?: 0
    }
}