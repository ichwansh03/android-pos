package com.jrektor.skripsi.outlet.shop

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.android.volley.*
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.GlobalData.Companion.CAMERA_REQUEST
import com.jrektor.skripsi.GlobalData.Companion.PICK_IMAGE_REQUEST
import com.jrektor.skripsi.GlobalData.Companion.REQUEST_PERMISSION
import com.jrektor.skripsi.R
import com.jrektor.skripsi.VolleyMultipartRequest
import com.jrektor.skripsi.verification.LoginActivity
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_add_outlet.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddOutletActivity : AppCompatActivity() {

    lateinit var filePath: String
    lateinit var image: Bitmap
    lateinit var addimage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_outlet)

        addimage = findViewById(R.id.add_img_outlet)
        addimage.setOnClickListener {
            if ((ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA),
                    REQUEST_PERMISSION
                )
            } else {
                val alert = AlertDialog.Builder(this)
                alert.setItems(arrayOf("Pilih Gambar", "Kamera")
                ) { _, which ->
                    if (which == 0) {
                        showFileChooser()
                    } else {
                        showCamera()
                    }
                }
                alert.show()
            }
        }

        btn_add_outlet.setOnClickListener {
            if (add_name_outlet.text.toString().isEmpty() && add_address_outlet.text.toString().isEmpty()) {
                Toast.makeText(this, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                insertOutlet()
            }
        }

    }
    private fun insertOutlet() {
        val queue = Volley.newRequestQueue(this)
        val request = object : VolleyMultipartRequest(
            Method.POST, GlobalData.BASE_URL+"outlet/addoutlet.php", Response.Listener { response: NetworkResponse ->
            try {
                val jsonObject = JSONObject(String(response.data))
                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()

                if (jsonObject.getString("status") == "OK")
                    finish()

            } catch (e: JSONException){
                Toast.makeText(this, "Berhasil Diupload", Toast.LENGTH_SHORT).show()
                finish()
            }
        },
            Response.ErrorListener { error: VolleyError ->
                Toast.makeText(this, "Masukkan Gambar", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): MutableMap<String, String> {
                val map: MutableMap<String, String> = kotlin.collections.HashMap()
                map["name"] = add_name_outlet.text.toString()
                map["address"] = add_address_outlet.text.toString()
                map["in_outlet"] = LoginActivity.OutletData.namaOutlet
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

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"),
            PICK_IMAGE_REQUEST
        )
    }

    //null pointer exception
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Argument must not be null
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            //NPE
            data?.let { intent ->
                Glide.with(this@AddOutletActivity).load(intent.data).into(addimage)
                try {
                    image = MediaStore.Images.Media.getBitmap(contentResolver, intent.data)
                    uploadBitmap(image)
                } catch (e: IOException){
                    Toast.makeText(this, "Error from Activity Result "+ e.message, Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            val file = File(filePath)
            val photoUri: Uri = FileProvider.getUriForFile(this, "com.jrektor.skripsi.fileprovider",file)
            Glide.with(this).load(photoUri).into(addimage)
            image = BitmapFactory.decodeFile(filePath)
            uploadBitmap(image)
        }
    }

    fun uploadBitmap(bitmap: Bitmap) {
        val queue = Volley.newRequestQueue(this)
        //add file add image for outlet
        val multipartRequest = object : VolleyMultipartRequest(Method.POST, GlobalData.BASE_URL+"product/addimageoutlet.php", Response.Listener<NetworkResponse> {
                response ->
            try {
                val jsonObject = JSONObject(String(response.data))
                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()

            } catch (e: JSONException) {
                Toast.makeText(this, "error jsonexception upload bitmap"+e.message, Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { _ ->
            Toast.makeText(this,"Berhasil mengupload gambar", Toast.LENGTH_SHORT).show()
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

}