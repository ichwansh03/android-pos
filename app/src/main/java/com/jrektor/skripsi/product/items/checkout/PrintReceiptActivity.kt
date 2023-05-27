package com.jrektor.skripsi.product.items.checkout

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.cart.OrderItem
import com.jrektor.skripsi.verification.LoginActivity
import kotlinx.android.synthetic.main.activity_print_receipt.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PrintReceiptActivity : AppCompatActivity() {

    lateinit var adapter: ReceiptAdapter

    private val REQUEST_CODE: Int = 1232
    private var nameoutlet: String = ""
    private var address: String = ""
    private var total: String = ""
    private var list = ArrayList<OrderItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_print_receipt)

        nameoutlet = LoginActivity.OutletData.namaOutlet
        outlet_name.text = nameoutlet

        address = LoginActivity.OutletData.alamat
        outlet_address.text = address

        total = "Rp."+GlobalData.totalBayar.toString()
        total_price.text = total

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        val dateTime = currentDateTime.format(formatter)
        order_date.text = dateTime

        getOrder(LoginActivity.OutletData.namaOutlet)

        btn_print.setOnClickListener {
            if (isStoragePermissionGranted()) {
                convertXmltoPdf()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE
                )
            }
        }

        deleteItem()
    }

    private fun deleteItem() {
        val queue = Volley.newRequestQueue(this)
        val stringRequest =
            object : StringRequest(Method.DELETE, GlobalData.BASE_URL+"item/deleteitemall.php", Response.Listener { _ ->
                Toast.makeText(this, "Produk berhasil dihapus", Toast.LENGTH_SHORT).show()
            }, { _ ->
                Toast.makeText(this, "Terjadi kesalahan saat menghapus produk", Toast.LENGTH_SHORT)
                    .show()
            }) {}
        queue.add(stringRequest)
    }

    private fun isStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun convertXmltoPdf() {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(rv_order_receipt.width, rv_order_receipt.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply {
            color = (rv_order_receipt.background as? ColorDrawable)?.color ?: Color.WHITE
        }
        canvas.drawRect(0f, 0f, rv_order_receipt.width.toFloat(), rv_order_receipt.height.toFloat(), paint)

        rv_order_receipt.draw(canvas)

        pdfDocument.finishPage(page)

        val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(directoryPath, "receipt.pdf")
        try {
            val fileOutputStream = FileOutputStream(file)
            pdfDocument.writeTo(fileOutputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        pdfDocument.close()
    }

    private fun getOrder(outlet: String) {
        val queue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+"item/getitem.php?in_outlet=$outlet",null,
            { response ->
                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)
                    val id = obj.getInt("id")
                    val product = obj.getString("name")
                    val price = obj.getInt("price")
                    val quantity = obj.getInt("quantity")

                    list.add(OrderItem(id, product, price, quantity, false))

                    val layoutManager = LinearLayoutManager(this)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    adapter = ReceiptAdapter(this, list)
                    rv_order_receipt.adapter = adapter
                    rv_order_receipt.layoutManager = layoutManager
                }
            },
            { error ->
                Log.d("Error Receipt ",error.toString())
            })
        queue.add(request)
    }
}