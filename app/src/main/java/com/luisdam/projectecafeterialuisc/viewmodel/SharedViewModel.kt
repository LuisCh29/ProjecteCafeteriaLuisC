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

    private val _usuariId = MutableLiveData<String>("")
    val usuariId: LiveData<String> = _usuariId

    private val _usuariEmail = MutableLiveData<String>("")
    val usuariEmail: LiveData<String> = _usuariEmail

    fun afegirACistella(producte: Producte) {
        val currentList = _cistellaItems.value ?: mutableListOf()
        currentList.add(producte)
        _cistellaItems.value = currentList
    }

    fun buidarCistella() {
        _cistellaItems.value = mutableListOf()
    }

    fun setUsuariActual(nom: String, id: String, email: String = "") {
        _usuariActual.value = nom
        _usuariId.value = id
        _usuariEmail.value = email
    }

    fun setUsuariId(id: String) {
        _usuariId.value = id
    }

    fun setUsuariNom(nom: String) {
        _usuariActual.value = nom
    }

    fun getPreuTotal(): Double {
        return _cistellaItems.value?.sumOf { it.preu } ?: 0.0
    }

    fun getQuantitatTotal(): Int {
        return _cistellaItems.value?.size ?: 0
    }

    fun logout() {
        _usuariActual.value = null
        _usuariId.value = ""
        _usuariEmail.value = ""
        buidarCistella()
    }
}