package com.jrektor.skripsi.verification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.MainActivity
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private var spinKatUsaha: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val catList = arrayOf("Kedai Kopi", "Restoran", "Cafe", "Toko Kelontong", "Lainnya")

        val catAdapter = ArrayAdapter(this, R.layout.spinner_item, catList)
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin_katusaha.adapter = catAdapter

        spin_katusaha.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                spinKatUsaha = parent?.getItemAtPosition(pos).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        txlogin.setOnClickListener {
            toLogin()
        }

        btnregister.setOnClickListener {
            val registerUrl: String = GlobalData.BASE_URL+"verif/register.php"

            if (txnama_usaha.text.toString().isEmpty() || spinKatUsaha.isEmpty() || txalamat_usaha.text.toString().isEmpty()
                || txnama_user.text.toString().isEmpty() || txnohp.text.toString().isEmpty() || txemail.text.toString().isEmpty() || txnopin.text.toString().isEmpty()){
                Toast.makeText(applicationContext, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                val request: RequestQueue = Volley.newRequestQueue(applicationContext)
                val strRequest = StringRequest(Request.Method.GET, registerUrl+"?nama_usaha="+txnama_usaha.text.toString()+"&kat_usaha="+spinKatUsaha+"&alamat_usaha="+txalamat_usaha.text.toString()
                +"&nama="+txnama_user.text.toString()+"&no_hp="+txnohp.text.toString()+"&jabatan="+txjabatan.text.toString()+"&email="+txemail.text.toString()+"&no_pin="+txnopin.text.toString(),
                    { response ->

                        if (response.equals("1")){
                            toLogin()
                        } else {
                            Toast.makeText(applicationContext,"Email sudah digunakan", Toast.LENGTH_SHORT).show()
                        }

                    },
                    { error ->
                        Log.d("Error", error.toString())
                    })
                request.add(strRequest)
            }
        }
    }
    private fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}