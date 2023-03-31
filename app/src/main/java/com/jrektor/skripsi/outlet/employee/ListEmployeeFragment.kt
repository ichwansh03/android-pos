package com.jrektor.skripsi.outlet.employee

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.fragment_list_employee.*
import kotlinx.android.synthetic.main.fragment_list_outlet.*

class ListEmployeeFragment : Fragment() {

    val list = ArrayList<ItemPegawai>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_list_employee, container, false)

        btn_add_employee.setOnClickListener {
            startActivity(Intent(activity, AddPegawaiActivity::class.java))
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            handler.post {
                getEmployee()
                pb_employee.visibility = View.GONE
            }
        }, 5000)
        return view
    }

    fun getEmployee() {
        val queue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+ "/employee/apiemployee.php", null,
            { response ->
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
                    val adapterPegawai = AdapterPegawai(requireContext(), list)
                    rv_employee.layoutManager = LinearLayoutManager(requireContext())
                    rv_employee.adapter = adapterPegawai
                }

            }, {error ->
                Log.d("error ", error.toString())

            })
        queue.add(request)
    }

}