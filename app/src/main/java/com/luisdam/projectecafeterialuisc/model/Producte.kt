package com.luisdam.projectecafeterialuisc.model

enum class CategoriaProducte {
    MENJAR, BEGUDES, POSTRES
}

data class Producte(
    val id: Int = 0,
    val nom: String,
    val descripcio: String,
    val preu: Double,
    val categoria: CategoriaProducte,
    val imageResId: Int? = 0
)