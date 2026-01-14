package com.luisdam.projectecafeterialuisc.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luisdam.projectecafeterialuisc.model.CategoriaProducte

@Entity(tableName = "productes")
data class ProducteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nom: String,
    val descripcio: String,
    val preu: Double,
    val categoria: CategoriaProducte,
    val imageResId: Int? = 0
)