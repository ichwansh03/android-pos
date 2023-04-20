package com.jrektor.skripsi.product.items

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_detail_product.*
import org.json.JSONException
import org.json.JSONObject

class FormEditProdukActivity : AppCompatActivity() {

    val updateProductUrl = GlobalData.BASE_URL+"product/updateproduct_app.php?id=${GlobalData.ids}"
    val deleteProductUrl = GlobalData.BASE_URL+"product/deleteproduct_app.php?id=${GlobalData.ids}"
    lateinit var spinkategori: String
    lateinit var  spinner: Spinner
    var listCategory: MutableList<String> = ArrayList()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val txaddimage = findViewById<TextView>(R.id.tx_add_image)
        txaddimage.text = "Ubah Gambar"

        val txbtnadd = findViewById<TextView>(R.id.tx_btn_add_product)
        txbtnadd.text = "Ubah Product"
        btn_del_product.visibility = View.VISIBLE

        val addprice = findViewById<EditText>(R.id.add_price_product)
        val addname = findViewById<EditText>(R.id.add_name_product)
        val addmerk = findViewById<EditText>(R.id.add_merk_product)
        val addstock = findViewById<EditText>(R.id.add_stock_product)
        val adddesc = findViewById<EditText>(R.id.add_desc_product)

        Glide.with(this@FormEditProdukActivity).load(GlobalData.imageProduct).into(add_img_product)
        addprice.setText("Rp. "+GlobalData.priceProduct.toString())
        addname.setText(GlobalData.nameProduct)
        addmerk.setText(GlobalData.merkProduct)
        addstock.setText(GlobalData.stockProduct.toString())
        adddesc.setText(GlobalData.descProduct)

        spinner = findViewById(R.id.spin_kategori)
        getListCategory()

        btn_add_product.setOnClickListener {
            updateProduct(GlobalData.ids)
        }

        btn_del_product.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi")
            builder.setMessage("Apakah anda yakin ingin menghapus produk?")
            builder.setPositiveButton("Ya") {dialog, which ->
                deleteProduct()
            }
            builder.setNegativeButton("Tidak") {dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        }
    }

    private fun deleteProduct() {
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Method.DELETE, deleteProductUrl, Response.Listener { response ->
            Toast.makeText(this,"Produk berhasil dihapus", Toast.LENGTH_SHORT).show()
        }, {
            error ->
            Toast.makeText(this,"Terjadi kesalahan saat menghapus produk", Toast.LENGTH_SHORT).show()
        }) {}
        queue.add(stringRequest)
    }

    private fun updateProduct(idProduct: Int) {
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val stringRequest = object : StringRequest(Method.PUT, updateProductUrl, Response.Listener {
            response ->
            Toast.makeText(applicationContext, "Produk Berhasil Diperbarui", Toast.LENGTH_SHORT).show()
        }, {
            error ->
            Log.d("Error update product ", error.toString())
        }) {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["id"] = idProduct.toString()
                map["name"] = GlobalData.nameProduct
                map["price"] = GlobalData.priceProduct.toString()
                map["merk"] = GlobalData.merkProduct
                map["stock"] = GlobalData.stockProduct.toString()
                map["cat_product"] = GlobalData.nameCategory
                map["image"] = GlobalData.imageProduct
                map["description"] = GlobalData.descProduct
                return map
            }
        }
        queue.add(stringRequest)
    }

    fun getListCategory() {
        AndroidNetworking.get(GlobalData.BASE_URL+"category/get_cat_app.php/")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        val jsonArray = response.getJSONArray("server_response")
                        for (i in 0 until jsonArray.length()){
                            val jsonObject = jsonArray.getJSONObject(i)
                            listCategory.add(jsonObject.getString("name"))

                            val catAdapter = ArrayAdapter(this@FormEditProdukActivity, R.layout.spinner_item, listCategory)
                            catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = catAdapter

                            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                                    spinkategori = parent?.getItemAtPosition(pos).toString()
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                    TODO("Not yet implemented")
                                }
                            }

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this@FormEditProdukActivity, "Gagal menampilkan data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(anError: ANError?) {
                    Toast.makeText(this@FormEditProdukActivity, "Tidak ada jaringan internet", Toast.LENGTH_SHORT).show()
                }
            })
    }

}