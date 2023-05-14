package com.jrektor.skripsi.product.cart

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.items.ModelProduct
import com.jrektor.skripsi.product.items.checkout.PayOptionActivity
import com.midtrans.sdk.uikit.api.model.*
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.cart_item.*
import kotlinx.android.synthetic.main.fragment_item.*
import java.util.*
import kotlin.collections.ArrayList

class CartActivity : AppCompatActivity() {

    lateinit var cartDB: CartDB
    lateinit var sharedPrefs: SharedPrefs
    lateinit var adapter: CartAdapter

    private var currentDate = Calendar.getInstance()
    private var list = ArrayList<ModelProduct>()
    private var listItem = ArrayList<OrderItem>()
    private var count = 0
    private var quantity = 0
    private var name_item: String = ""
    private var price_item: Int = 0
    private var namaPelanggan: String = "Default"
    private var nohpPelanggan: String = "000"
    private var catatan: String = "-"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartDB = CartDB.getInstance(this)!!
        sharedPrefs = SharedPrefs(this)

        namaPelanggan = nama_pelanggan.text.toString()
        nohpPelanggan = nohp_pelanggan.text.toString()
        catatan = txcatatan.text.toString()

        //from roomdb
        showProduct()
        //from sql
        showItem()

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

    private fun showItem() {
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+"item/getitem.php", null,
            { response ->
                for (s in 0 until response.length()) {
                    val jObject = response.getJSONObject(s)
                    val id = jObject.getInt("id")
                    name_item = jObject.getString("name")
                    price_item = jObject.getInt("price")
                    quantity = jObject.getInt("quantity")

                    listItem.add(OrderItem(id, name_item, price_item, quantity, true))

                    val layoutManager = LinearLayoutManager(this)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    val orderAdapter = OrderItemAdapter(this, listItem, object : OrderItemAdapter.ItemListener{
                        override fun onUpdate() {
                            countTotal()
                        }

                        override fun onDelete(position: Int) {
                            list.removeAt(position)
                            adapter.notifyDataSetChanged()
                            countTotal()
                        }
                    })
                    rv_cart.adapter = orderAdapter
                    rv_cart.layoutManager = layoutManager

                    var isSelectedAll = true
                    for(product in listItem){
                        if (product.selected){
                            count = 0
                            count += (price_item * product.quantity)
                            quantity = product.quantity
                        } else {
                            isSelectedAll = false
                        }
                    }
                    cb_select_all.isChecked = isSelectedAll
                    totalCount.text = "Rp. $count"
                }

            },
            { error ->
                Log.d("Error", error.toString())
            })
        queue.add(request)
    }

    private fun insertOrder() {
        val orderUrl = GlobalData.BASE_URL+"order/addorder.php"
        val request = Volley.newRequestQueue(applicationContext)
        val stringRequest = StringRequest(Request.Method.GET, orderUrl+"?name="+namaPelanggan+"&nohp="+nohpPelanggan+"&quantity="+quantity
                +"&total="+count+"&notes="+catatan+"&dates="+(currentDate.get(Calendar.YEAR)).toString()+"-"+(currentDate.get(Calendar.MONTH)+1).toString()+"-"+(currentDate.get(Calendar.DAY_OF_MONTH)).toString()
                +"&status=Lunas",
            { response ->
                if (response.equals("1")){
                    val intent = Intent(this, PayOptionActivity::class.java)
                    intent.putExtra("name", namaPelanggan)
                    intent.putExtra("phone", nohpPelanggan)
                    startActivity(intent)
                }
            },
            { error ->
                Log.d("Error Order", error.toString())
            })
        request.add(stringRequest)
    }

    private fun showProduct() {
        list = cartDB.daoCart().getAll() as ArrayList

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
    }

    private fun countTotal() {
        val cartList = cartDB.daoCart().getAll() as ArrayList
        var isSelectedAll = true

        for(product in cartList){
            if (product.selected){
                count = 0
                val prices = Integer.valueOf(product.price)
                count += (prices * product.quantity)
                quantity = product.quantity
            } else {
                isSelectedAll = false
            }
        }
        cb_select_all.isChecked = isSelectedAll
        totalCount.text = "Rp. $count"
        GlobalData.totalBayar = count
    }

}

