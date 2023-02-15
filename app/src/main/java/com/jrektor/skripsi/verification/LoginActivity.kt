package com.jrektor.skripsi.verification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.MainActivity
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txregister.setOnClickListener{
            var i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }

        var url:String = "http://192.168.43.8/pos/verif/login.php"
        btn_login.setOnClickListener {
            var request: RequestQueue = Volley.newRequestQueue(applicationContext)
            var strRequest = StringRequest(Request.Method.GET, url+"?email="+txemail_login.text.toString()+"&no_pin="+txpin_login.text.toString(),
                { response ->

                    if (response.equals("0")){
                        var intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "Gagal Login", Toast.LENGTH_SHORT).show()
                    }

                },
                { error ->
                    Log.d("Error", error.toString())
                })
            request.add(strRequest)
        }
    }
}