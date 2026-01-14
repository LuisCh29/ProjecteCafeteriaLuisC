package com.luisdam.projectecafeterialuisc.data.repository

import com.luisdam.projectecafeterialuisc.R
import com.luisdam.projectecafeterialuisc.data.db.AppDatabase
import com.luisdam.projectecafeterialuisc.data.db.ProducteEntity
import com.luisdam.projectecafeterialuisc.model.CategoriaProducte
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProducteRepository(
    private val database: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun insertProducte(producte: ProducteEntity) = withContext(ioDispatcher) {
        database.producteDao().insertProducte(producte)
    }

    suspend fun insertInitialProductes() = withContext(ioDispatcher) {
        val initialProductes = listOf(
            // MENJAR
            ProducteEntity(
                nom = "Entrepà de pernil",
                descripcio = "amb tomàquet",
                preu = 4.50,
                categoria = CategoriaProducte.MENJAR,
                imageResId = R.drawable.menjar
            ),
            ProducteEntity(
                nom = "Ensalada Cèsar",
                descripcio = "amb pollastre",
                preu = 6.00,
                categoria = CategoriaProducte.MENJAR,
                imageResId = R.drawable.menjar
            ),
            ProducteEntity(
                nom = "Hamburguesa",
                descripcio = "amb formatge i bacon",
                preu = 8.50,
                categoria = CategoriaProducte.MENJAR,
                imageResId = R.drawable.menjar
            ),
            ProducteEntity(
                nom = "Pizza margarita",
                descripcio = "Mitjana",
                preu = 9.00,
                categoria = CategoriaProducte.MENJAR,
                imageResId = R.drawable.menjar
            ),

            // BEGUDES
            ProducteEntity(
                nom = "Cafè",
                descripcio = "Americano",
                preu = 1.50,
                categoria = CategoriaProducte.BEGUDES,
                imageResId = R.drawable.beguda
            ),
            ProducteEntity(
                nom = "Cafè amb llet",
                descripcio = "Gran",
                preu = 2.00,
                categoria = CategoriaProducte.BEGUDES,
                imageResId = R.drawable.beguda
            ),
            ProducteEntity(
                nom = "Suc de taronja",
                descripcio = "Natural",
                preu = 3.00,
                categoria = CategoriaProducte.BEGUDES,
                imageResId = R.drawable.beguda
            ),
            ProducteEntity(
                nom = "Refresc",
                descripcio = "Coca-Cola, Fanta, etc...",
                preu = 2.50,
                categoria = CategoriaProducte.BEGUDES,
                imageResId = R.drawable.beguda
            ),

            // POSTRES
            ProducteEntity(
                nom = "Pastís de formatge",
                descripcio = "Amb fruita",
                preu = 4.50,
                categoria = CategoriaProducte.POSTRES,
                imageResId = R.drawable.postres
            ),
            ProducteEntity(
                nom = "Gelat",
                descripcio = "Menta, Maduixa, etc...",
                preu = 3.50,
                categoria = CategoriaProducte.POSTRES,
                imageResId = R.drawable.postres
            ),
            ProducteEntity(
                nom = "Flan",
                descripcio = "Casolà",
                preu = 3.00,
                categoria = CategoriaProducte.POSTRES,
                imageResId = R.drawable.postres
            ),
            ProducteEntity(
                nom = "Fruita",
                descripcio = "Fresca",
                preu = 2.50,
                categoria = CategoriaProducte.POSTRES,
                imageResId = R.drawable.postres
            )
        )
        database.producteDao().insertAllProductes(initialProductes)
    }

    fun getProductesByCategoria(categoria: CategoriaProducte) =
        database.producteDao().getProductesByCategoria(categoria)

    fun getAllProductes() = database.producteDao().getAllProductes()
}