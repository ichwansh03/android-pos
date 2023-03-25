package com.jrektor.skripsi.product.cart

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaoCart {

    @Insert(onConflict = REPLACE)
    fun insert(data: CartItem)

    @Delete
    fun delete(data: CartItem)

    @Delete
    fun delete(data: List<CartItem>)

    @Update
    fun update(data: CartItem): Int

    @Query("SELECT * FROM cart ORDER BY id ASC")
    fun getAll(): List<CartItem>

    @Query("SELECT * FROM cart WHERE id = :id LIMIT 1")
    fun getProduct(id: Int): CartItem

    @Query("DELETE FROM cart WHERE id = :id")
    fun deleteById(id: String): Int

    @Query("DELETE FROM cart")
    fun deleteAll() : Int
}