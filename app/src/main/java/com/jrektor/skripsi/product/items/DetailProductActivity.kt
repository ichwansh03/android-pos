package com.jrektor.skripsi.product.items

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.cart.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.activity_register.*

class DetailProductActivity : AppCompatActivity() {

    lateinit var cartDB: CartDB
    lateinit var cartItem: ModelProduct
    var itemList = ArrayList<OrderItem>()
    var list = ArrayList<ModelProduct>()

    lateinit var adapter: CartAdapter
    lateinit var orderAdapter: OrderItemAdapter
    private var quantities: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        cartDB = CartDB.getInstance(this)!!

        list = cartDB.daoCart().getAll() as ArrayList

        orderAdapter = OrderItemAdapter(this, itemList, object : OrderItemAdapter.ItemListener{
            override fun onUpdate() {
            }
            override fun onDelete(position: Int) {
            }
        })

        tx_count_item.text = orderAdapter.getItemCountData().toString()

        btn_add_count.setOnClickListener {
            quantities++
            tx_count.text = quantities.toString()
        }

        btn_min_count.setOnClickListener {
            if (tx_count.text.toString() > "0") {
                quantities--
                tx_count.text = quantities.toString()
            }
        }

        detailProduct()

        btn_checkout.setOnClickListener {

            insertItem()

            val data = cartDB.daoCart().getProduct(cartItem.id)

            if (data == null) {
                insertData()
            } else {
                data.quantity += 1
                updateData(data)
            }
        }

        btn_to_cart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun insertItem() {
        val url = GlobalData.BASE_URL+"item/additem.php"
        val queue = Volley.newRequestQueue(this)
        val request = object : StringRequest(Method.POST, url, Response.Listener { _ ->
            Toast.makeText(this, "Berhasil menambahkan ke keranjang", Toast.LENGTH_SHORT).show()
        },
        Response.ErrorListener {
            error ->
            Log.d("error ",error.message.toString())
        }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["name"] = name_product_detail.text.toString()
                params["price"] = price_product_detail.text.toString()
                params["quantity"] = quantities.toString()
                return params
            }
        }
        queue.add(request)
    }

    @SuppressLint("SetTextI18n")
    private fun detailProduct() {
        val data = intent.getStringExtra("extra")
        cartItem = Gson().fromJson(data, ModelProduct::class.java)

        Glide.with(this@DetailProductActivity).load(cartItem.image).into(img_product_detail)
        price_product_detail.text = "Rp. ${cartItem.price}"
        name_product_detail.text = cartItem.name
        tx_count.text = quantities.toString()
        merk_product.text = "Merk : " + GlobalData.merkProduct
        stock_product.text = "Stok Tersedia : " + GlobalData.stockProduct.toString()
        desc_product.text = GlobalData.descProduct
    }

    private fun priceStringToInt(str: String): Int {
        val regex = Regex("[^0-9]") // regular expression untuk mencari karakter selain angka
        val cleanStr = regex.replace(str, "") // menghapus karakter selain angka pada string
        return cleanStr.toInt() // mengembalikan nilai integer
    }

    private fun updateData(data: ModelProduct) {
        CompositeDisposable().add(io.reactivex.Observable.fromCallable {
            cartDB.daoCart().update(data)
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("respons", "data inserted")
                Toast.makeText(this, "Berhasil menambahkan ke keranjang", Toast.LENGTH_SHORT).show()
            })
    }

    private fun insertData() {
        CompositeDisposable().add(io.reactivex.Observable.fromCallable {
            cartDB.daoCart().insert(cartItem)
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("respons", "data inserted")
                Toast.makeText(this, "Berhasil menambahkan ke keranjang", Toast.LENGTH_SHORT).show()
            })
    }

}