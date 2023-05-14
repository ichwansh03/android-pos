package com.jrektor.skripsi.product.cart

data class OrderItem (  var id: Int,
                        var name: String,
                        var price: Int,
                        var quantity: Int,
                        var selected: Boolean = true)