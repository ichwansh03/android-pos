package com.jrektor.skripsi.outlet.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_add_outlet.*

class AddOutletActivity : AppCompatActivity() {

    val urlUploadOutlet = GlobalData.BASE_URL+"outlet/addoutlet.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_outlet)

        btn_add_outlet.setOnClickListener { uploadOutlet() }
    }

    private fun uploadOutlet() {
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val stringRequest = object : StringRequest(Method.POST, urlUploadOutlet, Response.Listener { _ ->
            Toast.makeText(applicationContext, "Outlet Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
        }, {
                error ->
            Log.d("Error upload product ",error.toString())
        }) {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["name"] = add_name_outlet.text.toString()
                map["address"] = add_address_outlet.text.toString()
                //gambar nanti
                return map
            }
        }
        queue.add(stringRequest)
    }
}