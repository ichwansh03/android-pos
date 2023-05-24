package com.jrektor.skripsi.outlet.employee

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.VolleyMultipartRequest
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_add_outlet.*
import kotlinx.android.synthetic.main.activity_add_pegawai.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_list_employee.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddPegawaiActivity : AppCompatActivity() {

    lateinit var filePath: String
    lateinit var image: Bitmap
    lateinit var addimage: ImageView

    private var spinjobEmployee = ""
    private var spinOutlet = ""

    val urlUploadEmployee = GlobalData.BASE_URL+"employee/addemployee.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pegawai)

        addimage = findViewById(R.id.add_img_pegawai)
        spinJob()
        getListOutlet()

        addimage.setOnClickListener {
            if ((ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA),
                    GlobalData.REQUEST_PERMISSION
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
        //NPE FAB
        val btnAddEmployee = findViewById<CardView>(R.id.btn_add_pegawai)
        btnAddEmployee.setOnClickListener {
            if (add_name_pegawai.text.toString().isEmpty() && add_email_pegawai.text.toString().isEmpty() && add_phone_pegawai.text.toString().isEmpty() && add_pin_pegawai.text.toString().isEmpty()){
                Toast.makeText(this, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                insertPegawai()
                insertPegawaiToRegister()
            }
        }
    }

    private fun insertPegawaiToRegister() {
        val registerUrl: String = GlobalData.BASE_URL+"verif/register.php"

        val request: RequestQueue = Volley.newRequestQueue(applicationContext)
        val strRequest = StringRequest(Request.Method.GET, registerUrl+"?nama_usaha="+GlobalData.nameOutlet+"&kat_usaha=Cabang"+"&alamat_usaha="+GlobalData.addressOutlet
                +"&nama="+add_name_pegawai.text.toString()+"&no_hp="+add_phone_pegawai.text.toString()+"&jabatan="+spinjobEmployee+"&email="+add_email_pegawai.text.toString()+"&no_pin="+add_pin_pegawai.text.toString(),
            { response ->

                if (response.equals("1")){
                    finish()
                } else {
                    Toast.makeText(applicationContext,"Email sudah digunakan", Toast.LENGTH_SHORT).show()
                }

            },
            { error ->
                Log.d("Error", error.toString())
            })
        request.add(strRequest)
    }

    private fun spinJob() {
        val jobList = arrayOf("Admin", "Pelayan", "Kasir")

        val catAdapter = ArrayAdapter(this, R.layout.spinner_item, jobList)
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin_job_employee.adapter = catAdapter

        spin_job_employee.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                spinjobEmployee = parent?.getItemAtPosition(pos).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun insertPegawai() {
        val queue = Volley.newRequestQueue(this)
        val request = object : VolleyMultipartRequest(
            Method.POST, urlUploadEmployee, Response.Listener { response: NetworkResponse ->
            try {
                val jsonObject = JSONObject(String(response.data))
                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()

                if (jsonObject.getString("status") == "OK")
                    finish()

            } catch (e: JSONException){
                Toast.makeText(this, "Berhasil diupload", Toast.LENGTH_SHORT).show()
            }
        },
            Response.ErrorListener { error: VolleyError ->
                Toast.makeText(this, "error listener volley"+error.message, Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): MutableMap<String, String> {
                val map: MutableMap<String, String> = kotlin.collections.HashMap()
                map["name"] = add_name_pegawai.text.toString()
                map["job"] = spinjobEmployee
                map["phone"] = add_phone_pegawai.text.toString()
                map["in_outlet"] = spinOutlet
                map["email"] = add_email_pegawai.text.toString()
                map["no_pin"] = add_pin_pegawai.text.toString()
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
                startActivityForResult(takePicIntent, GlobalData.CAMERA_REQUEST)
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
            GlobalData.PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Argument must not be null
        if (requestCode == GlobalData.PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            //NPE
            data?.let { intent ->
                Glide.with(this@AddPegawaiActivity).load(intent.data).into(addimage)
                try {
                    image = MediaStore.Images.Media.getBitmap(contentResolver, intent.data)
                    uploadBitmap(image)
                } catch (e: IOException){
                    Toast.makeText(this, "Error from Activity Result "+ e.message, Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == GlobalData.CAMERA_REQUEST && resultCode == RESULT_OK){
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
        val multipartRequest = object : VolleyMultipartRequest(Method.POST, GlobalData.BASE_URL+"employee/addimageemployee.php", Response.Listener<NetworkResponse> {
                response ->
            try {
                val jsonObject = JSONObject(String(response.data))
                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()

            } catch (e: JSONException) {
                Toast.makeText(this, "error jsonexception upload bitmap"+e.message, Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { _ ->
            Toast.makeText(this, "Berhasil mengupload gambar", Toast.LENGTH_SHORT).show()
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

    private fun getListOutlet() {
        val queue = Volley.newRequestQueue(this)
        val url = GlobalData.BASE_URL + "outlet/apioutlet.php/"
        val listOutlet = mutableListOf<String>()
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    val data = jsonArray.getJSONObject(i).getString("name")
                    listOutlet.add(data)
                }

                val outletAdapter = ArrayAdapter(this, R.layout.spinner_item, listOutlet)
                outletAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spin_in_outlet.adapter = outletAdapter

                spin_in_outlet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                        spinOutlet = parent?.getItemAtPosition(pos).toString()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        // kode jika tidak ada item yang dipilih
                    }
                }
            },
            { error ->
                Log.d("error ", error.toString())
            }
        )

        queue.add(stringRequest)

    }
}