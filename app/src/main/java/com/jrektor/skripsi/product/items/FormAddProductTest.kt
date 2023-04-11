package com.jrektor.skripsi.product.items

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.VolleyMultipartRequest
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.item_add_product.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FormAddProductTest : AppCompatActivity() {

    companion object {
        const val REQUEST_PERMISSION = 100
        const val PICK_IMAGE_REQUEST = 1
        const val CAMERA_REQUEST = 2
    }

    lateinit var filePath: String
    lateinit var image: Bitmap
    var listCategory: MutableList<String> = ArrayList()
    //lateinit property spinkategori has not been initialized
    lateinit var spinkategori: String
    lateinit var  spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        spinner = findViewById(R.id.spin_kategori)
        getListCategory()

        btn_add_product.setOnClickListener {
            if (add_name_product.text.toString().isEmpty() && add_price_product.text.toString().isEmpty() && add_stock_product.text.toString().isEmpty() && add_merk_product.text.toString().isEmpty()){
                Toast.makeText(this, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                insertProduct()
            }
        }

        add_img_product.setOnClickListener {
            if ((ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA), REQUEST_PERMISSION)
            } else {
                val alert = AlertDialog.Builder(this)
                alert.setItems(arrayOf("Pilih Gambar", "Kamera"),DialogInterface.OnClickListener {dialogInterface, which ->
                    if (which == 0){
                        showFileChooser()
                    } else {
                        showCamera()
                    }
                })
                alert.show()
            }
        }
    }


    private fun getListCategory() {
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

                            val catAdapter = ArrayAdapter(this@FormAddProductTest, R.layout.spinner_item, listCategory)
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
                        Toast.makeText(this@FormAddProductTest, "Gagal menampilkan data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(anError: ANError?) {
                    Toast.makeText(this@FormAddProductTest, "Tidak ada jaringan internet", Toast.LENGTH_SHORT).show()
                }
            })
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun showCamera() {
        val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePicIntent.resolveActivity(packageManager) != null){
            var photoFile: File? = null

            try {
                photoFile = createImageFile()
            } catch (e: IOException){
                Toast.makeText(this, "Error "+e.message, Toast.LENGTH_SHORT).show()
            }

            if (photoFile != null){
                val photoUri = FileProvider.getUriForFile(this, "com.jrektor.skripsi.fileprovider", photoFile)
                takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePicIntent, CAMERA_REQUEST)
            }
        }
    }

    @kotlin.jvm.Throws(IOException::class)
    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_"+timestamp+"_"
        val storeDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storeDir)
        filePath = image.absolutePath

        return image
    }

    private fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray {
        val byteStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteStream)

        return byteStream.toByteArray()
    }

    fun uploadBitmap(bitmap: Bitmap) {
        val queue = Volley.newRequestQueue(this)
        val multipartRequest = object : VolleyMultipartRequest(Request.Method.POST, GlobalData.BASE_URL+"product/addimageproduct.php", Response.Listener<NetworkResponse> {
            response ->
            try {
                val jsonObject = JSONObject(String(response.data))
                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()

            } catch (e: JSONException) {
                Toast.makeText(this, "error "+e.message, Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener {
                error ->
                Toast.makeText(this, "error "+error.message, Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val parameter: MutableMap<String, String> = HashMap()
                parameter["id"] = intent.extras!!.getString("id","")
                return parameter
            }

            override fun getByteData(): Map<String, DataPart> {
                val data: MutableMap<String, DataPart> = HashMap()
                data["image"] = DataPart("image"+System.currentTimeMillis()+".jpeg", getFileDataFromDrawable(bitmap))
                return data
            }
        }
        queue.add(multipartRequest)
    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            //NPE
            Glide.with(this).load(data!!.data).into(img_add_item)
            try {
                image = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
                uploadBitmap(image)
            } catch (e: IOException){
                Toast.makeText(this, "Error "+ e.message, Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            val file = File(filePath)
            val photoUri: Uri = FileProvider.getUriForFile(this, "com.jrektor.skripsi.fileprovider",file)
            Glide.with(this).load(photoUri).into(img_add_item)
            image = BitmapFactory.decodeFile(filePath)
            uploadBitmap(image)
        }
    }

    private fun insertProduct() {
        val queue = Volley.newRequestQueue(this)
        val request = object : VolleyMultipartRequest(Request.Method.POST, GlobalData.BASE_URL+"product/addproduct.php", Response.Listener { response: NetworkResponse ->
            try {
                val jsonObject = JSONObject(String(response.data))
                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()

                if (jsonObject.getString("status") == "OK")
                    finish()

            } catch (e: JSONException){
                Toast.makeText(this, "error "+e.message, Toast.LENGTH_SHORT).show()
            }
        },
        Response.ErrorListener { error: VolleyError ->
            Toast.makeText(this, ""+error.message, Toast.LENGTH_SHORT).show()
        }){
            override fun getParams(): MutableMap<String, String> {
                val map: MutableMap<String, String> = kotlin.collections.HashMap()
                map["name"] = add_name_product.text.toString()
                map["price"] = add_price_product.text.toString()
                map["merk"] = add_merk_product.text.toString()
                map["stock"] = add_stock_product.text.toString()
                map["cat_product"] = spinkategori
                map["description"] = add_desc_product.text.toString()
                return map
            }

            override fun getByteData(): Map<String, DataPart> {
                val datas: MutableMap<String, DataPart> = kotlin.collections.HashMap()
                datas["image"] = DataPart(
                    System.currentTimeMillis().toString()+".jpeg", getFileDataFromDrawable(image))
                return datas
            }
        }

        queue.add(request)
    }
}