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
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.VolleyMultipartRequest
import kotlinx.android.synthetic.main.activity_add_outlet.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditOutletActivity : AppCompatActivity() {

    lateinit var filePath: String
    lateinit var bitmap: Bitmap
    var id: Int = 0
    lateinit var name: EditText
    lateinit var address: EditText
    lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_outlet)

        val txtimg = findViewById<TextView>(R.id.tximgoutlet)
        txtimg.text = "Ubah Gambar"

        val txtadd = findViewById<TextView>(R.id.txaddoutlet)
        txtadd.text = "Ubah Outlet"

        image = findViewById(R.id.add_img_outlet)
        name = findViewById(R.id.add_name_outlet)
        address = findViewById(R.id.add_address_outlet)

        id = intent.getIntExtra("id",0)
        name.setText(intent.getStringExtra("name"))
        address.setText(intent.getStringExtra("address"))

        btn_add_outlet.setOnClickListener {
            updateOutlet()
        }

        btn_del_outlet.visibility = View.VISIBLE
        btn_del_outlet.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi")
            builder.setMessage("Apakah anda yakin ingin menghapus outlet?")
            builder.setPositiveButton("Ya") { _, _ ->
                deleteOutlet()
            }
            builder.setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        image.setOnClickListener {
            if ((ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED)
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA
                    ),
                    GlobalData.REQUEST_PERMISSION
                )
            } else {
                val alert = AlertDialog.Builder(this)
                alert.setItems(arrayOf("Pilih Gambar", "Kamera")) { _, which ->
                    if (which == 0) {
                        showFileChooser()
                    } else {
                        showCamera()
                    }
                }
                alert.show()
            }
        }
    }

    private fun showCamera() {
        val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePicIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null

            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                Toast.makeText(this, "Error " + e.message, Toast.LENGTH_SHORT).show()
            }

            if (photoFile != null) {
                val photoUri =
                    FileProvider.getUriForFile(this, "com.jrektor.skripsi.fileprovider", photoFile)
                takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePicIntent, GlobalData.CAMERA_REQUEST)
            }
        }
    }

    private fun createImageFile(): File? {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timestamp + "_"
        val storeDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storeDir)
        filePath = image.absolutePath

        return image
    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Pilih Gambar"),
            GlobalData.PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Argument must not be null
        if (requestCode == GlobalData.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            //NPE
            data?.let { intent ->
                Glide.with(this@EditOutletActivity).load(intent.data).into(image)
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, intent.data)
                    uploadBitmap(bitmap)
                } catch (e: IOException) {
                    Toast.makeText(this, "Error " + e.message, Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == GlobalData.CAMERA_REQUEST && resultCode == RESULT_OK) {
            val file = File(filePath)
            val photoUri: Uri =
                FileProvider.getUriForFile(this, "com.jrektor.skripsi.fileprovider", file)
            Glide.with(this).load(photoUri).into(image)
            bitmap = BitmapFactory.decodeFile(filePath)
            uploadBitmap(bitmap)
        }
    }

    private fun uploadBitmap(bitmap: Bitmap) {
        val queue = Volley.newRequestQueue(this)
        val multipartRequest = object : VolleyMultipartRequest(
            Method.POST,
            GlobalData.BASE_URL + "outlet/addimageoutlet.php",
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(String(response.data))
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()

                } catch (e: JSONException) {
                    Toast.makeText(this, "error " + e.message, Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Berhasil mengupload gambar", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val parameter: MutableMap<String, String> = HashMap()
                //null
                //parameter["id"] = intent.extras!!.getString("id", "")
                parameter["id"] = id.toString()
                return parameter
            }

            override fun getByteData(): Map<String, DataPart> {
                val data: MutableMap<String, DataPart> = HashMap()
                data["image"] = DataPart(
                    "image" + System.currentTimeMillis() + ".jpeg",
                    getFileDataFromDrawable(bitmap)
                )
                return data
            }
        }
        queue.add(multipartRequest)
    }

    private fun getFileDataFromDrawable(image: Bitmap): ByteArray {
        val byteStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 80, byteStream)

        return byteStream.toByteArray()
    }

    private fun deleteOutlet() {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(Method.DELETE, GlobalData.BASE_URL+"outlet/deleteoutlet.php?id=${GlobalData.idOutlet}", Response.Listener { _ ->
            Toast.makeText(this, "Pegawai berhasil dihapus", Toast.LENGTH_SHORT).show()
        }, { _ ->
            Toast.makeText(this, "Pegawai gagal dihapus", Toast.LENGTH_SHORT).show()
        }) {}
        queue.add(stringRequest)
    }

    private fun updateOutlet() {
        val queue = Volley.newRequestQueue(this)
        val request = object : StringRequest(Method.POST, GlobalData.BASE_URL+"outlet/updateoutlet.php", Response.Listener {
                response ->
            try {
                val json = JSONObject(response)
                Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show()

                if (json.getString("status") == "OK") {
                    finish()
                }
            } catch (e: JSONException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }, {
                error ->
            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = id.toString()
                params["name"] = name.text.toString()
                params["address"] = address.text.toString()

                return params
            }
        }
        queue.add(request)
    }
}