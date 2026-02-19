package com.luisdam.projectecafeterialuisc.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.luisdam.projectecafeterialuisc.model.CategoriaProducte

@Dao
interface ProducteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducte(producte: ProducteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProductes(productes: List<ProducteEntity>)

    @Query("SELECT * FROM productes WHERE categoria = :categoria")
    fun getProductesByCategoria(categoria: CategoriaProducte): LiveData<List<ProducteEntity>>

    @Query("SELECT * FROM productes")
    fun getAllProductes(): LiveData<List<ProducteEntity>>

    @Query("SELECT * FROM productes")
    suspend fun getAllProductesNonLive(): List<ProducteEntity>

    @Query("DELETE FROM productes")
    suspend fun deleteAllProductes()

    @Query("SELECT * FROM productes WHERE id = :id")
    suspend fun getProducteById(id: Int): ProducteEntity?
}