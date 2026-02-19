package com.luisdam.projectecafeterialuisc.model

import java.util.Date

data class Comanda(
    val id: String = "",  // Firebase document ID
    val usuariId: String,
    val usuariNom: String,
    val total: Double,
    val data: Date = Date(),
    val estat: String = "Pendent",  // Pendent, Completada, Cancel·lada
    val productes: List<ProducteComanda> = emptyList()
)

data class ProducteComanda(
    val nom: String,
    val preu: Double,
    val quantitat: Int = 1
)