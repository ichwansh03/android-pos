package com.jrektor.skripsi.product.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "cart")
class ModelProduct(@PrimaryKey(autoGenerate = true)
                   @ColumnInfo(name = "id")
                   var id: Int,
                   var name: String,
                   var price: Int,
                   var merk: String,
                   var stock: Int,
                   var catProduct: String,
                   var image: String,
                   var description: String,
                   var quantity: Int,
                   var selected: Boolean = true)