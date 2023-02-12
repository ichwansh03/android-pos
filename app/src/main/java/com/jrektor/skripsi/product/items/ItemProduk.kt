package com.jrektor.skripsi.product.items

class ItemProduk {

    var id: Int
    var name: String
    var price: Int
    var merk: String
    var stock: Int
    var image: String
    var description: String

    constructor(
        id: Int,
        name: String,
        price: Int,
        merk: String,
        stock: Int,
        image: String,
        description: String
    ) {
        this.id = id
        this.name = name
        this.price = price
        this.merk = merk
        this.stock = stock
        this.image = image
        this.description = description
    }
}