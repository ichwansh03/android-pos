package com.jrektor.skripsi.customer

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
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.fragment_customer.*
import kotlinx.android.synthetic.main.fragment_item.*

class CustomerFragment : Fragment() {

    val list = ArrayList<ItemCustomer>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_customer, container, false)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            handler.post {
                getConsumer()
                pb_consumer.visibility = View.GONE
            }
        },5000)

        return view
    }

    private fun getConsumer() {
        val queue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+"order/apiorder.php", null,
            { response ->
                if (response.length() == 0){
                    txempty_customer.visibility = View.VISIBLE
                }
                else {
                    txempty_customer.visibility = View.GONE
                    for (c in 0 until response.length()) {
                        val objects = response.getJSONObject(c)
                        val id = objects.getInt("id")
                        val name = objects.getString("name")
                        val phone = objects.getString("nohp")
                        val status = objects.getString("status")

                        list.add(ItemCustomer(id, name, phone, status))
                        val adapter = AdapterCustomer(requireContext(), list)
                        rv_consumer.layoutManager = LinearLayoutManager(requireContext())
                        rv_consumer.adapter = adapter
                    }
                }
            },
            { error ->
                Log.d("error ", error.toString())
            })
        queue.add(request)
    }

}