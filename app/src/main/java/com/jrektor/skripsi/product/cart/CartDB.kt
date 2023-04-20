package com.jrektor.skripsi.product.cart

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jrektor.skripsi.product.items.ModelProduct

@Database(entities = [ModelProduct::class], version = 1)
abstract class CartDB : RoomDatabase() {
    abstract fun daoCart(): DaoCart
    companion object {
        private var INSTANCE: CartDB? = null

        fun getInstance(context: Context): CartDB? {
            if (INSTANCE == null) {
                synchronized(CartDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CartDB::class.java, "CartDB" // Database Name
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}