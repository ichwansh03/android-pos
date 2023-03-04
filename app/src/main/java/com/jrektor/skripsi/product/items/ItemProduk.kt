package com.jrektor.skripsi.product.items

data class ItemProduk(var id: Int,
                      var name: String,
                      var price: Int,
                      var merk: String,
                      var stock: Int,
                      var image: String,
                      var description: String)