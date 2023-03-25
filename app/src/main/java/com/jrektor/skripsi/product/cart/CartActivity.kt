package com.jrektor.skripsi.product.cart

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.items.ModelProduct
import com.jrektor.skripsi.product.items.checkout.PayOptionActivity
import com.midtrans.sdk.uikit.api.model.*
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.cart_item.*
import java.util.*
import kotlin.collections.ArrayList

class CartActivity : AppCompatActivity() {

    lateinit var cartDB: CartDB
    lateinit var sharedPrefs: SharedPrefs
    lateinit var adapter: CartAdapter

    var list = ArrayList<CartItem>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartDB = CartDB.getInstance(this)!!
        sharedPrefs = SharedPrefs(this)

        showProduct()

        btn_bayar.setOnClickListener {
            val intent = Intent(this, PayOptionActivity::class.java)
            startActivity(intent)
        }

        cb_select_all.setOnClickListener {
            for (i in list.indices){
                val product = list[i]
                product.selected = cb_select_all.isChecked
                list[i] = product
            }
        }
    }

    private fun showProduct() {
        list = cartDB.daoCart().getAll() as kotlin.collections.ArrayList
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        adapter = CartAdapter(this, list, object : CartAdapter.Listeners{
            override fun onUpdate() {
                countTotal()
            }

            override fun onDelete(position: Int) {
                list.removeAt(position)
                adapter.notifyDataSetChanged()
                countTotal()
            }
        })

        rv_cart.adapter = adapter
        rv_cart.layoutManager = layoutManager
    }

    private fun countTotal() {
        var count = 0
        val cartList = cartDB.daoCart().getAll() as kotlin.collections.ArrayList
        var isSelectedAll = true

        for(product in cartList){
            if (product.selected){
                val prices = Integer.valueOf(product.price)
                count += (prices * product.quantity)
            } else {
                isSelectedAll = false
            }
        }
        cb_select_all.isChecked = isSelectedAll
        totalCount.text = "Rp. $count"
    }

}

