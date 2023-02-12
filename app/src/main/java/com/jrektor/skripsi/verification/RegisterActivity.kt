package com.jrektor.skripsi.verification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val registerUrl: String = "http://10.0.2.2:5554/pos/verif/register.php"

        btnregister.setOnClickListener {
            if (txnama_usaha.text.toString().isEmpty() || txkategori_usaha.text.toString().isEmpty() || txalamat_usaha.text.toString().isEmpty()
                || txnama_user.text.toString().isEmpty() || txnohp.text.toString().isEmpty() || txemail.text.toString().isEmpty() || txnopin.text.toString().isEmpty()){
                Toast.makeText(applicationContext, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                var request: RequestQueue = Volley.newRequestQueue(applicationContext)
                var strRequest = StringRequest(Request.Method.GET, registerUrl+"?nama_usaha="+txnama_usaha.text.toString()+"&kat_usaha="+txkategori_usaha.text.toString()+"&alamat_usaha="+txalamat_usaha.text.toString()
                +"&nama="+txnama_user.text.toString()+"&no_hp="+txnohp.text.toString()+"&jabatan="+txjabatan.text.toString()+"&email="+txemail.text.toString()+"&no_pin="+txnopin.text.toString(),
                    { response ->

                        if (response.equals("1")){
                            var i = Intent(this, LoginActivity::class.java)
                            startActivity(i)
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
}