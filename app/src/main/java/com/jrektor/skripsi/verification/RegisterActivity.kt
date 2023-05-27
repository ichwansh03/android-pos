package com.jrektor.skripsi.verification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private var spinKatUsaha: String = ""
    private var spinJabatan: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        spinKategoriUsaha()
        spinJabatan()

        txlogin.setOnClickListener {
            finish()
        }

        btnregister.setOnClickListener {

            if (txnama_usaha.text.toString().isEmpty() || spinKatUsaha.isEmpty() || txalamat_usaha.text.toString().isEmpty()
                || txnama_user.text.toString().isEmpty() || txnohp.text.toString().isEmpty() || txemail.text.toString().isEmpty() || txnopin.text.toString().isEmpty()){
                Toast.makeText(applicationContext, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                if (txnopin.text.toString().length == 6){
                    val queue = Volley.newRequestQueue(applicationContext)
                    val request = object : StringRequest(Method.POST, GlobalData.BASE_URL+"verif/register_akun.php", Response.Listener {
                            _ ->
                        Toast.makeText(applicationContext, "Berhasil Menambahkan Akun", Toast.LENGTH_SHORT).show()
                        finish()
                    },
                        Response.ErrorListener {
                                error ->
                            Log.d("error", error.message.toString())

                        }) {
                        override fun getParams(): MutableMap<String, String> {
                            val params = HashMap<String, String>()
                            params["nama_usaha"] = txnama_usaha.text.toString()
                            params["kat_usaha"] = spinKatUsaha
                            params["alamat_usaha"] = txalamat_usaha.text.toString()
                            params["nama"] = txnama_user.text.toString()
                            params["no_hp"] = txnohp.text.toString()
                            params["jabatan"] = spinJabatan
                            params["email"] = txemail.text.toString()
                            params["no_pin"] = txnopin.text.toString()
                            return params
                        }
                    }
                    queue.add(request)
                } else {
                    Toast.makeText(applicationContext, "PIN harus 6 angka", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun spinJabatan() {
        val jabatanList = arrayOf("Owner", "Pegawai", "Admin")

        val jabatanAdapter = ArrayAdapter(this, R.layout.spinner_item, jabatanList)
        jabatanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin_jabatan.adapter = jabatanAdapter

        spin_jabatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinJabatan = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun spinKategoriUsaha() {
        val catList = arrayOf("Kuliner", "Fashion", "Otomotif", "Teknologi Internet", "Agribisnis", "Produk Kreatif", "Furniture", "Lainnya")

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

    }

}