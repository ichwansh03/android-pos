package com.jrektor.skripsi.verification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.MainActivity
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONException

class LoginActivity : AppCompatActivity() {

    private var outlet: String = ""
    private var address: String = ""

    object OutletData {
        var namaOutlet: String = ""
        var alamat: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txregister.setOnClickListener{
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }

        txlupapin.setOnClickListener {
            val intent = Intent(this, ForgotPinActivity::class.java)
            intent.putExtra("email",txemail_login.text.toString())
            startActivity(intent)
        }

        btn_login.setOnClickListener {
            val request: RequestQueue = Volley.newRequestQueue(applicationContext)
            val strRequest = StringRequest(Request.Method.GET, GlobalData.BASE_URL+"verif/login.php?email="+txemail_login.text.toString()+"&no_pin="+txpin_login.text.toString(),
                { response ->
                    if (response.equals("0")){
                        getNamaOutlet(txemail_login.text.toString())
                    } else {
                        Toast.makeText(applicationContext, "Email atau password salah", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Log.d("Error", error.toString())
                })
            request.add(strRequest)
        }
    }

    private fun getNamaOutlet(email : String) {
        val url : String = GlobalData.BASE_URL+"verif/register.php?email=$email"
        val queue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null, {
                response ->
                try {
                    val jsonArray = JSONArray(response.toString())

                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        outlet = jsonObject.getString("nama_usaha")
                        GlobalData.nameOutlet = outlet
                        OutletData.namaOutlet = outlet
                        address = jsonObject.getString("alamat_usaha")
                        GlobalData.addressOutlet = address
                        OutletData.alamat = address
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } catch (e: JSONException){
                    e.printStackTrace()
                }
            },
            {
                error ->
                error.printStackTrace()
            })
        queue.add(request)
    }

}