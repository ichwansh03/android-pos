package com.jrektor.skripsi.product.items

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class FormAddProdukActivity : AppCompatActivity() {

    var listCategory: MutableList<String> = ArrayList()
    lateinit var bitmap: Bitmap
    lateinit var imageByteArray: ByteArray
    var uploadProductUrl = GlobalData.BASE_URL+"product/addproduct_app.php/"
    lateinit var spinkategori: String
    lateinit var  spinner: Spinner

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            try {
                val inputStream = contentResolver.openInputStream(it)
                bitmap = BitmapFactory.decodeStream(inputStream)
                add_img_product.setImageBitmap(bitmap)

                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                imageByteArray = outputStream.toByteArray()
            } catch (exception : Exception){
                exception.printStackTrace()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        spinner = findViewById(R.id.spin_kategori)
        getListCategory()
        add_img_product.setOnClickListener {
            Dexter.withContext(this@FormAddProdukActivity)
                .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        pickImage.launch("image/*")

                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                }).check()
        }

        btn_add_product.setOnClickListener { uploadProduct() }
    }

    private fun uploadProduct() {
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val stringRequest = object : StringRequest(Method.POST, uploadProductUrl, Response.Listener { _ ->
            Toast.makeText(applicationContext, "Produk Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
        }, {
            error ->
            Log.d("Error upload product ",error.toString())
        }) {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["name"] = add_name_product.text.toString()
                map["price"] = add_price_product.text.toString()
                map["merk"] = add_merk_product.text.toString()
                map["stock"] = add_stock_product.text.toString()
                map["cat_product"] = spinkategori
                map["image"] = GlobalData.BASE_URL+"image/"
                map["description"] = add_desc_product.text.toString()
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

                            val catAdapter = ArrayAdapter(this@FormAddProdukActivity, R.layout.spinner_item, listCategory)
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
                        Toast.makeText(this@FormAddProdukActivity, "Gagal menampilkan data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(anError: ANError?) {
                    Toast.makeText(this@FormAddProdukActivity, "Tidak ada jaringan internet", Toast.LENGTH_SHORT).show()
                }
            })
    }
}