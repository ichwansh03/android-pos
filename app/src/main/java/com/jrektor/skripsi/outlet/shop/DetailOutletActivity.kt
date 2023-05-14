package com.jrektor.skripsi.outlet.shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.outlet.employee.AdapterPegawai
import com.jrektor.skripsi.outlet.employee.ItemPegawai
import kotlinx.android.synthetic.main.activity_detail_outlet.*
import kotlinx.android.synthetic.main.fragment_list_employee.*

class DetailOutletActivity : AppCompatActivity() {

    val list = ArrayList<ItemPegawai>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_outlet)

        Glide.with(this@DetailOutletActivity).load(GlobalData.imageOutlet).into(img_detail_outlet)
        name_detail_outlet.text = GlobalData.nameOutlet
        place_detail_outlet.text = GlobalData.addressOutlet

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            handler.post {
                getEmployee()
                pb_detail_outlet.visibility = View.GONE
            }
        }, 5000)

        val btnEdit = findViewById<FloatingActionButton>(R.id.fab_edit_outlet)
        btnEdit.setOnClickListener {
            val intent = Intent(this, EditOutletActivity::class.java)
            intent.putExtra("id",GlobalData.idOutlet)
            intent.putExtra("name",name_detail_outlet.text)
            intent.putExtra("address",place_detail_outlet.text)
            startActivity(intent)
        }
    }

    fun getEmployee() {
        val queue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, GlobalData.BASE_URL+ "employee/apiemployee.php", null,
            { response ->
                if (response.length() == 0){
                    tx_empty_detail_outlet.visibility = View.VISIBLE
                }
                else {
                    tx_empty_detail_outlet.visibility = View.GONE

                    for (i in 0 until response.length()){
                        val jsonObject = response.getJSONObject(i)
                        val id = jsonObject.getInt("id")
                        val name = jsonObject.getString("name")
                        val job = jsonObject.getString("job")
                        val phone = jsonObject.getString("phone")
                        val email = jsonObject.getString("email")
                        val no_pin = jsonObject.getInt("no_pin")
                        val image = jsonObject.getString("email")
                        val in_outlet = jsonObject.getString("in_outlet")

                        list.add(ItemPegawai(id, name, job, phone, email, no_pin, image, in_outlet))
                        val adapterPegawai = AdapterPegawai(this, list)
                        rv_detail_employee.layoutManager = LinearLayoutManager(this)
                        rv_detail_employee.adapter = adapterPegawai
                    }
                }

            }, {error ->
                Log.d("error ", error.toString())

            })
        queue.add(request)
    }

}