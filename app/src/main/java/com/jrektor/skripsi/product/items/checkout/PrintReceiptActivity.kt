package com.jrektor.skripsi.product.items.checkout

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
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
import com.jrektor.skripsi.product.items.cart.OrderItem
import com.jrektor.skripsi.verification.LoginActivity
import kotlinx.android.synthetic.main.activity_print_receipt.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class PrintReceiptActivity : AppCompatActivity() {

    lateinit var adapter: ReceiptAdapter

    private val REQUEST_CODE: Int = 1232
    private var nameoutlet: String = ""
    private var address: String = ""
    private var name: String = ""
    private var phone: String = ""
    private var total: String = ""
    private var jumlah: String = ""
    private var kembalian: String = ""
    private var list = ArrayList<OrderItem>()
    private var currentDate = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_print_receipt)

        nameoutlet = LoginActivity.OutletData.namaOutlet
        outlet_name.text = nameoutlet

        address = LoginActivity.OutletData.alamat
        outlet_address.text = address

        name = "Nama Pelanggan: "+GlobalData.namaPelanggan
        customer_name.text = name

        phone = "Nomor HP: "+GlobalData.nohpPelanggan
        customer_phone.text = phone

        total = "Total Bayar : Rp."+GlobalData.totalBayar.toString()
        total_price.text = total

        jumlah = "Jumlah Bayar : Rp."+GlobalData.jmlBayarUser.toString()
        amount.text = jumlah

        kembalian = "Kembalian : Rp."+(GlobalData.jmlBayarUser - GlobalData.totalBayar)
        change.text = kembalian

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
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
                //success
            }, { _ ->
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT)
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
        val scaleBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_teknokrat)
        val pageWidth = 1200
        val pdfDocument = PdfDocument()
        val paint = Paint()

        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, 2010, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        canvas.drawBitmap(scaleBitmap, 0f, 0f, paint)

        paint.color = Color.WHITE
        paint.textSize = 30f
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(nameoutlet, 1160f, 40f, paint)
        canvas.drawText(address, 1160f, 80f, paint)
        canvas.drawText("Tanggal :", 20f, 690f, paint)
        canvas.drawText(order_date.text.toString(), 250f, 690f, paint)

        rv_order_receipt.draw(canvas)

        paint.textAlign = Paint.Align.LEFT
        paint.color = Color.rgb(247, 147, 30)
        canvas.drawText("Total Bayar", 700f, 1350f, paint)
        canvas.drawText(":", 900f, 1350f, paint)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(total, pageWidth - 40f, 1350f, paint)
        paint.textAlign = Paint.Align.LEFT
        paint.color = Color.BLACK

        canvas.drawText("Jumlah Bayar", 700f, 1400f, paint)
        canvas.drawText(":", 900f, 1400f, paint)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(jumlah, pageWidth - 40f, 1400f, paint)
        paint.textAlign = Paint.Align.LEFT

        canvas.drawText("Kembalian", 700f, 1450f, paint)
        canvas.drawText(":", 900f, 1450f, paint)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(kembalian, pageWidth - 40f, 1450f, paint)
        paint.textAlign = Paint.Align.LEFT

        canvas.drawLine(40f, 1650f, pageWidth - 20f, 1650f, paint)
        canvas.drawText("Terima Kasih", (pageWidth / 2).toFloat(), 1720f, paint)
        canvas.drawText("Atas Kunjungan Anda", (pageWidth / 2).toFloat(), 1770f, paint)

        pdfDocument.finishPage(page)

        val filePath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(filePath, "Receipt.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(this, "PDF Created", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "PDF Creation Failed", Toast.LENGTH_SHORT).show()
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