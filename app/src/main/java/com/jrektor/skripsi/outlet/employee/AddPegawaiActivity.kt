package com.jrektor.skripsi.outlet.employee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_add_outlet.*
import kotlinx.android.synthetic.main.activity_add_pegawai.*
import kotlinx.android.synthetic.main.fragment_list_employee.*

class AddPegawaiActivity : AppCompatActivity() {

    val urlUploadEmployee = GlobalData.BASE_URL+"employee/addemployee.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pegawai)

        getListOutlet()
        btn_add_employee.setOnClickListener { uploadEmployee() }
    }

    private fun getListOutlet() {

    }

    private fun uploadEmployee() {
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val stringRequest = object : StringRequest(Method.POST, urlUploadEmployee, Response.Listener { _ ->
            Toast.makeText(applicationContext, "Employee Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
        }, {
                error ->
            Log.d("Error upload product ",error.toString())
        }) {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["name"] = add_name_pegawai.text.toString()
                map["phone"] = add_phone_pegawai.text.toString()
                map["job"] = add_job_pegawai.text.toString()
                map["email"] = add_email_pegawai.text.toString()
                map["no_pin"] = add_pin_pegawai.text.toString()

                //gambar nanti
                return map
            }
        }
        queue.add(stringRequest)
    }
}