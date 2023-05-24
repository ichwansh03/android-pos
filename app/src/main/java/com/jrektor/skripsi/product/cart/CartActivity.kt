package com.jrektor.skripsi.product.cart

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.items.checkout.PayOptionActivity
import com.jrektor.skripsi.verification.LoginActivity
import com.midtrans.sdk.uikit.api.model.*
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.cart_item.*
import kotlinx.android.synthetic.main.fragment_item.*
import java.util.*
import kotlin.collections.ArrayList

class CartActivity : AppCompatActivity() {

    lateinit var adapter: OrderItemAdapter

    private var currentDate = Calendar.getInstance()
    private var listItem = ArrayList<OrderItem>()
    private var count = 0
    private var quantity = 0
    private var name_item: String = ""
    private var price_item: Int = 0
    lateinit var namaPelanggan: String
    lateinit var nohpPelanggan: String
    lateinit var catatan: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        namaPelanggan = nama_pelanggan.text.toString()
        nohpPelanggan = nohp_pelanggan.text.toString()
        catatan = txcatatan.text.toString()

        showItem(LoginActivity.OutletData.namaOutlet)

        btn_bayar.setOnClickListener {
            insertOrder()
        }

        cb_select_all.setOnClickListener {
            for (i in listItem.indices){
                val product = listItem[i]
                product.selected = cb_select_all.isChecked
                listItem[i] = product
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun showItem(outlet: String) {
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+"item/getitem.php?in_outlet=$outlet", null,
            { response ->
                for (s in 0 until response.length()) {
                    val jObject = response.getJSONObject(s)
                    val id = jObject.getInt("id")
                    name_item = jObject.getString("name")
                    price_item = jObject.getInt("price")
                    quantity = jObject.getInt("quantity")

                    listItem.add(OrderItem(id, name_item, price_item, quantity, false))

                    val layoutManager = LinearLayoutManager(this)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    adapter = OrderItemAdapter(this, listItem, object : OrderItemAdapter.ItemListener{
                        override fun onUpdate() {
                            countTotal()
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        override fun onDelete(position: Int) {
                            if (position >= 0 && position < listItem.size){
                                listItem.removeAt(position)
                                adapter.notifyItemRemoved(position)
                                countTotal()
                                pb_cart.visibility = View.GONE
                            }

                        }
                    })
                    rv_cart.adapter = adapter
                    rv_cart.layoutManager = layoutManager

                }
                pb_cart.visibility = View.GONE
            },
            { error ->
                Log.d("Error", error.toString())
                pb_cart.visibility = View.GONE
            })
        queue.add(request)
        pb_cart.visibility = View.VISIBLE
    }

    private fun insertOrder() {
        val orderUrl = GlobalData.BASE_URL+"order/addorder.php"
        val request = Volley.newRequestQueue(applicationContext)
        val defaultName = "Pelanggan"
        val defaultPhone = "000"
        val defaultNotes = "-"
        val name = nama_pelanggan.text.toString().ifEmpty { defaultName }
        val phone = nohp_pelanggan.text.toString().ifEmpty { defaultPhone }
        val notes = txcatatan.text.toString().ifEmpty { defaultNotes }
        if(totalCount.equals("0")){
            Toast.makeText(applicationContext, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
        } else {
            val stringRequest = StringRequest(Request.Method.GET, orderUrl+"?name="+name+"&nohp="+phone+"&quantity="+quantity
                    +"&total="+count+"&notes="+notes+"&dates="+(currentDate.get(Calendar.YEAR)).toString()+"-"+(currentDate.get(Calendar.MONTH)+1).toString()+"-"+(currentDate.get(Calendar.DAY_OF_MONTH)).toString()
                    +"&status=Lunas"+"&in_outlet="+GlobalData.nameOutlet,
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
    }

    private fun countTotal() {
        var isSelectedAll = true
        count = 0
        for(product in listItem){
            if (product.selected){
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

