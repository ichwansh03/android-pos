package com.jrektor.skripsi.product.cart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jrektor.skripsi.product.items.ModelProduct

@Entity(tableName = "cart")
class CartItem (
                @PrimaryKey(autoGenerate = true)
                @ColumnInfo(name = "idTbl")
                var idTbl: Int,
                var id: Int,
                var name: String,
                var price: Int,
                var image: String,
                var quantity: Int,
                var created_at: String,
                var updated_at: String,
                var selected: Boolean = true
)