package com.jrektor.skripsi.product.cart

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.jrektor.skripsi.product.items.ModelProduct

@Dao
interface DaoCart {

    @Insert(onConflict = REPLACE)
    fun insert(data: ModelProduct)

    @Delete
    fun delete(data: ModelProduct)

    @Delete
    fun delete(data: List<ModelProduct>)

    @Update
    fun update(data: ModelProduct): Int

    @Query("SELECT * FROM cart ORDER BY id ASC")
    fun getAll(): List<ModelProduct>

    @Query("SELECT * FROM cart WHERE id = :id LIMIT 1")
    fun getProduct(id: Int): ModelProduct

    @Query("DELETE FROM cart WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("DELETE FROM cart")
    fun deleteAll() : Int
}