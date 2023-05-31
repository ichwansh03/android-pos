package com.jrektor.skripsi.product.items

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
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.VolleyMultipartRequest
import com.jrektor.skripsi.verification.LoginActivity
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_detail_product.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FormEditProdukActivity : AppCompatActivity() {

    val updateProductUrl =
        GlobalData.BASE_URL + "product/updateproduct_app.php"
    val deleteProductUrl =
        GlobalData.BASE_URL + "product/deleteproduct_app.php?id=${GlobalData.ids}"
    private var spinkategori: String = ""
    lateinit var spinner: Spinner
    lateinit var filePath: String
    lateinit var image: Bitmap
    lateinit var updateImage: ImageView

    lateinit var addprice: EditText
    lateinit var addname: EditText
    lateinit var addmerk: EditText
    lateinit var addstock: EditText
    lateinit var adddesc: EditText

    private var id: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val txaddimage = findViewById<TextView>(R.id.tx_add_image)
        txaddimage.text = "Ubah Gambar"

        val txbtnadd = findViewById<TextView>(R.id.tx_btn_add_product)
        txbtnadd.text = "Ubah Product"
        btn_del_product.visibility = View.VISIBLE

        addprice = findViewById(R.id.add_price_product)
        addname = findViewById(R.id.add_name_product)
        addmerk = findViewById(R.id.add_merk_product)
        addstock = findViewById(R.id.add_stock_product)
        adddesc = findViewById(R.id.add_desc_product)
        updateImage = findViewById(R.id.add_img_product)

        getData()

        spinner = findViewById(R.id.spin_kategori)
        getCategories(LoginActivity.OutletData.namaOutlet)

        btn_add_product.setOnClickListener {
            updateProduct()
        }

        updateImage.setOnClickListener {
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
        btn_del_product.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi")
            builder.setMessage("Apakah anda yakin ingin menghapus produk?")
            builder.setPositiveButton("Ya") { _, _ ->
                deleteProduct()
            }
            builder.setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }
    }

    private fun getData() {
        id = intent.getIntExtra("id",0)
        Glide.with(this@FormEditProdukActivity).load(GlobalData.imageProduct).into(updateImage)
        addprice.setText(GlobalData.priceProduct.toString())
        addname.setText(GlobalData.nameProduct)
        addmerk.setText(GlobalData.merkProduct)
        addstock.setText(GlobalData.stockProduct.toString())
        adddesc.setText(GlobalData.descProduct)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Argument must not be null
        if (requestCode == GlobalData.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            //NPE
            data?.let { intent ->
                Glide.with(this@FormEditProdukActivity).load(intent.data).into(updateImage)
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
            Glide.with(this).load(photoUri).into(updateImage)
            image = BitmapFactory.decodeFile(filePath)
            uploadBitmap(image)
        }
    }

    private fun uploadBitmap(image: Bitmap) {
        val queue = Volley.newRequestQueue(this)
        val multipartRequest = object : VolleyMultipartRequest(
            Method.POST,
            GlobalData.BASE_URL + "product/addimageproduct.php",
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
                    getFileDataFromDrawable(image)
                )
                return data
            }
        }
        queue.add(multipartRequest)
    }

    private fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray {
        val byteStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteStream)

        return byteStream.toByteArray()
    }


    private fun deleteProduct() {
        val queue = Volley.newRequestQueue(this)

        val stringRequest =
            object : StringRequest(Method.DELETE, deleteProductUrl, Response.Listener { _ ->
                Toast.makeText(this, "Produk berhasil dihapus", Toast.LENGTH_SHORT).show()
                finish()
            }, { _ ->
                Toast.makeText(this, "Terjadi kesalahan saat menghapus produk", Toast.LENGTH_SHORT)
                    .show()
            }) {}
        queue.add(stringRequest)
    }

    private fun updateProduct() {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(Method.POST,
            updateProductUrl,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    Toast.makeText(
                        this, jsonObject.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()

                    if (jsonObject.getString("status") == "OK") {
                        finish()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val parameter = HashMap<String, String>()
                parameter["id"] = id.toString()
                parameter["name"] = addname.text.toString()
                parameter["price"] = addprice.text.toString()
                parameter["merk"] = addmerk.text.toString()
                parameter["stock"] = addstock.text.toString()
                parameter["cat_product"] = spinkategori
                parameter["description"] = adddesc.text.toString()

                return parameter
            }
        }
        requestQueue.add(stringRequest)
    }

    private fun getCategories(outlet: String) {
        val queue = Volley.newRequestQueue(this)
        val url = GlobalData.BASE_URL + "category/get_cat_app.php?in_outlet=$outlet"
        val listCategory = mutableListOf<String>()
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    val data = jsonArray.getJSONObject(i).getString("name")
                    listCategory.add(data)
                }

                val catAdapter = ArrayAdapter(this, R.layout.spinner_item, listCategory)
                catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = catAdapter

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        pos: Int,
                        id: Long
                    ) {
                        spinkategori = parent?.getItemAtPosition(pos).toString()
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