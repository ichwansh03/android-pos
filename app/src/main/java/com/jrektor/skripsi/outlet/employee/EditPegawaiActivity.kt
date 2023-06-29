package com.jrektor.skripsi.outlet.employee

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.VolleyMultipartRequest
import com.jrektor.skripsi.verification.LoginActivity
import kotlinx.android.synthetic.main.activity_add_pegawai.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class EditPegawaiActivity : AppCompatActivity() {

    val updatePegawaiUrl = GlobalData.BASE_URL + "employee/updateemployee.php"
    val deletePegawaiUrl = GlobalData.BASE_URL + "employee/deleteemployee.php?id=${GlobalData.idPegawai}"

    lateinit var filePath: String
    lateinit var image: Bitmap
    lateinit var spinerJob: Spinner
    lateinit var spinnerOutlet: Spinner
    lateinit var imageView: ImageView
    lateinit var name: EditText
    lateinit var phone: EditText
    lateinit var mail: EditText
    lateinit var pin: EditText

    private var spinjobEmployee = ""
    private var spinOutlet = ""
    private var id: Int = 0
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pegawai)

        getDataEmployee()

        val tximg = findViewById<TextView>(R.id.tximgempl)
        tximg.text = "Ubah Gambar"

        val txadd = findViewById<TextView>(R.id.txaddempl)
        txadd.text = "Ubah Pegawai"

        imageView = findViewById(R.id.add_img_pegawai)
        name = findViewById(R.id.add_name_pegawai)
        phone = findViewById(R.id.add_phone_pegawai)
        mail = findViewById(R.id.add_email_pegawai)
        pin = findViewById(R.id.add_pin_pegawai)
        spinerJob = findViewById(R.id.spin_job_employee)
        getSpinJob()
        spinnerOutlet = findViewById(R.id.spin_in_outlet)
        getSpinOutlet(LoginActivity.OutletData.namaOutlet)

        btn_add_pegawai.setOnClickListener {
            updatePegawai()
        }

        btn_del_pegawai.visibility = View.VISIBLE
        btn_del_pegawai.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi")
            builder.setMessage("Apakah anda yakin ingin menghapus pegawai?")
            builder.setPositiveButton("Ya") { _, _ ->
                deletePegawai()
            }
            builder.setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        imageView.setOnClickListener {
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

    private fun createImageFile(): File {
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

    private fun deletePegawai() {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(Method.DELETE, deletePegawaiUrl, Response.Listener { _ ->
            Toast.makeText(this, "Pegawai berhasil dihapus", Toast.LENGTH_SHORT).show()
        }, { _ ->
            Toast.makeText(this, "Pegawai gagal dihapus", Toast.LENGTH_SHORT).show()
        }) {}
        queue.add(stringRequest)
    }

    private fun updatePegawai() {
        val queue = Volley.newRequestQueue(this)
        val request = object : StringRequest(Method.POST, updatePegawaiUrl, Response.Listener {
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
            finish()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = id.toString()
                params["name"] = name.text.toString()
                params["phone"] = phone.text.toString()
                params["job"] = spinjobEmployee
                params["email"] = mail.text.toString()
                params["no_pin"] = pin.text.toString()
                params["branch"] = spinOutlet

                return params
            }
        }

        queue.add(request)
    }

    private fun getSpinOutlet(outlet: String) {
        val queue = Volley.newRequestQueue(this)
        val url = GlobalData.BASE_URL + "outlet/outletbyoutlet.php?=in_outlet=$outlet"
        val listOutlet = mutableListOf<String>()
        val stringRequest = StringRequest(
            Request.Method.GET, url,
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

    private fun getSpinJob() {
        val jobList = arrayOf("Admin", "Pelayan", "Kasir")

        val catAdapter = ArrayAdapter(this, R.layout.spinner_item, jobList)
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinerJob.adapter = catAdapter

        spinerJob.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                spinjobEmployee = parent?.getItemAtPosition(pos).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Argument must not be null
        if (requestCode == GlobalData.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            //NPE
            data?.let { intent ->
                Glide.with(this@EditPegawaiActivity).load(intent.data).into(imageView)
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
            Glide.with(this).load(photoUri).into(imageView)
            image = BitmapFactory.decodeFile(filePath)
            uploadBitmap(image)
        }
    }

    private fun uploadBitmap(image: Bitmap) {
        val queue = Volley.newRequestQueue(this)
        val multipartRequest = object : VolleyMultipartRequest(
            Method.POST,
            GlobalData.BASE_URL + "employee/addimageemployee.php",
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(String(response.data))
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()

                } catch (e: JSONException) {
                    Toast.makeText(this, "error " + e.message, Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { _ ->
                Toast.makeText(this, "Berhasil mengupload gambar", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val parameter: MutableMap<String, String> = java.util.HashMap()
                //null
                //parameter["id"] = intent.extras!!.getString("id", "")
                parameter["id"] = id.toString()
                return parameter
            }

            override fun getByteData(): Map<String, DataPart> {
                val data: MutableMap<String, DataPart> = java.util.HashMap()
                data["image"] = DataPart(
                    "image" + System.currentTimeMillis() + ".jpeg",
                    getFileDataFromDrawable(image)
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

    private fun getDataEmployee() {
        id = intent.getIntExtra("id", 0)
        if (id != 0) { // tambahkan pengecekan apakah id bukan 0
            val queue: RequestQueue = Volley.newRequestQueue(this)
            val request = StringRequest(
                Request.Method.GET,
                GlobalData.BASE_URL + "employee/apiemployeebyid?id=$id",
                { response ->
                    val jsonArray = JSONArray(response)
                    if (jsonArray.length() > 0) { // tambahkan pengecekan apakah ada data yang didapat dari server
                        val jsonObject = jsonArray.getJSONObject(0) //ambil data pertama
                        val names = jsonObject.getString("name")
                        val phones = jsonObject.getString("phone")
                        val emails = jsonObject.getString("email")
                        val nopins = jsonObject.getString("no_pin")
                        val images = jsonObject.getString("image")

                        name.setText(names.toString())
                        phone.setText(phones.toString())
                        mail.setText(emails.toString())
                        pin.setText(nopins.toString())
                        Glide.with(this).load(images).into(imageView)
                    }
                },
                { error ->
                    Log.e("API Error", error.toString())
                })
            queue.add(request)
        }
    }

}