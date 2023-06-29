package com.jrektor.skripsi.verification

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_forgot_pin.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class ForgotPinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pin)

        txemail_pin.setText(intent.getStringExtra("email"))

        txpin_new.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (txpin_new.equals(txpin_new_confirm) && text.length == 6){
                    updatePIN()
                } else {
                    Toast.makeText(applicationContext,"PIN Tidak sama atau harus 6 angka", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun updatePIN() {

        if (txpin_new_confirm.text.toString() == txpin_new.text.toString()) {
            val queue = Volley.newRequestQueue(this)
            val request = object : StringRequest(Method.POST, GlobalData.BASE_URL+"verif/update_pin.php", Response.Listener {
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
            }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["email"] = txemail_pin.text.toString()
                    params["no_pin"] = txpin_new.text.toString()
                    return params
                }
            }
            queue.add(request)
        } else {
            Toast.makeText(this, "PIN harus sama", Toast.LENGTH_SHORT).show()
        }
    }
}