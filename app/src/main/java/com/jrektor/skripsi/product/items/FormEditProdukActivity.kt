package com.jrektor.skripsi.product.items

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_detail_product.*

class FormEditProdukActivity : AppCompatActivity() {

    val updateProductUrl = GlobalData.BASE_URL+"product/updateproduct_app.php?id=${GlobalData.ids}"
    val deleteProductUrl = GlobalData.BASE_URL+"product/deleteproduct_app.php?id=${GlobalData.ids}"

    //Failed to find layer (com.jrektor.skripsi/com.jrektor.skripsi.product.items.AddProductActivity#0) in layer parent (no-parent).
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        tx_add_image.text = "Ubah Gambar"
        tx_btn_add_product.text = "Ubah Product"
        btn_del_product.visibility = View.VISIBLE

        Glide.with(this@FormEditProdukActivity).load(GlobalData.imageProduct).into(add_img_product)
        add_price_product.setText("Rp. "+GlobalData.priceProduct.toString())
        add_name_product.setText(GlobalData.nameProduct)
        //Unable to start activity, Not found exception string resource id
        add_merk_product.setText(GlobalData.merkProduct)
        add_stock_product.setText(GlobalData.stockProduct)
        add_desc_product.setText(GlobalData.descProduct)

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
            override fun getParams(): MutableMap<String, String>? {
                val map = HashMap<String, String>()
                map["id"] = idProduct.toString()
                map["name"] = GlobalData.nameProduct
                map["price"] = GlobalData.priceProduct.toString()
                map["merk"] = GlobalData.merkProduct
                map["stock"] = GlobalData.stockProduct.toString()
                map["cat_product"] = GlobalData.nameCategory
                map["image"] = GlobalData.imageProduct
                map["description"] = GlobalData.descProduct
                return super.getParams()
            }
        }
        queue.add(stringRequest)
    }

}