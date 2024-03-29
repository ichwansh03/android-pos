package com.jrektor.skripsi.outlet.shop

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.verification.LoginActivity
import kotlinx.android.synthetic.main.fragment_list_outlet.*

class ListOutletFragment : Fragment() {

    var list = ArrayList<ItemOutlet>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_outlet, container, false)

        val btnadd = view.findViewById<FloatingActionButton>(R.id.btn_add_outlet)

        if (LoginActivity.OutletData.pekerjaan == "Karyawan"){
            btnadd.visibility = View.INVISIBLE
        } else {
            btnadd.setOnClickListener { startActivity(Intent(activity, AddOutletActivity::class.java)) }
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            handler.post {
                getOutlet(LoginActivity.OutletData.namaOutlet)
                pb_outlet.visibility = View.GONE
            }
        }, 3000)
        return view
    }

    private fun getOutlet(outlet: String) {
        val activity = activity

        if (activity != null){
            val queue: RequestQueue = Volley.newRequestQueue(activity)
            val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+"outlet/outletbyoutlet.php?in_outlet=$outlet", null,
                { response ->
                    if (response.length() == 0){
                        txempty_outlet.visibility = View.VISIBLE
                    }
                    else {
                        txempty_outlet.visibility = View.GONE
                        for (i in 0 until response.length()) {
                            val objects = response.getJSONObject(i)
                            val id = objects.getInt("id")
                            val name = objects.getString("name")
                            val address = objects.getString("address")
                            val in_outlet = objects.getString("in_outlet")
                            val image = objects.getString("image").replace("http://localhost/pos/",GlobalData.BASE_URL)

                            list.add(ItemOutlet(id, name, address, in_outlet, image))
                            val adapter = AdapterOutlet(requireContext(), list)
                            rv_outlet.layoutManager = LinearLayoutManager(requireContext())
                            rv_outlet.adapter = adapter
                        }
                    }
                }, {
                        error ->
                    Log.d("error", error.toString())
                })
            queue.add(request)
        }

    }
}