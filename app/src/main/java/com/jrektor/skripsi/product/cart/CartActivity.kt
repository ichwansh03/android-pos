package com.jrektor.skripsi.product.cart

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.items.ModelProduct
import com.jrektor.skripsi.product.items.checkout.PayOptionActivity
import com.midtrans.sdk.uikit.api.model.*
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.cart_item.*
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class CartActivity : AppCompatActivity() {

    lateinit var cartDB: CartDB
    lateinit var sharedPrefs: SharedPrefs
    lateinit var adapter: CartAdapter

    var currentDate = LocalDate.now()
    var list = ArrayList<ModelProduct>()
    var count = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartDB = CartDB.getInstance(this)!!
        sharedPrefs = SharedPrefs(this)

        showProduct()

        btn_bayar.setOnClickListener {
            insertOrder()
        }

        cb_select_all.setOnClickListener {
            for (i in list.indices){
                val product = list[i]
                product.selected = cb_select_all.isChecked
                list[i] = product
            }
        }
    }

    private fun insertOrder() {
        val orderUrl = GlobalData.BASE_URL+"order/addorder.php"
        if(nama_pelanggan.text.toString().isEmpty() || nohp_pelanggan.text.toString().isEmpty() || totalCount.equals("0")){
            Toast.makeText(applicationContext, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
        } else {
            val request = Volley.newRequestQueue(applicationContext)
            val stringRequest = StringRequest(Request.Method.GET, orderUrl+"?name="+nama_pelanggan.text.toString()+"&nohp="+nohp_pelanggan.text.toString()
                    +"&total="+count+"&notes="+txcatatan.text.toString()+"&dates="+currentDate.dayOfMonth+"-"+currentDate.monthValue+"-"+currentDate.year
                +"&status=Lunas",
                { response ->
                    if (response.equals("1"))
                        startActivity(Intent(this, PayOptionActivity::class.java))
                },
                { error ->
                    Log.d("Error Order", error.toString())
                })
            request.add(stringRequest)
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

