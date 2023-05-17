package com.jrektor.skripsi.product.items

class ModelProduct(
    var id: Int,
    var name: String,
    var price: Int,
    var merk: String = "Tanpa Merk",
    var stock: Int = 100,
    var catProduct: String,
    var image: String,
    var description: String = "Tidak ada",
    var quantity: Int,
    var selected: Boolean = true
)