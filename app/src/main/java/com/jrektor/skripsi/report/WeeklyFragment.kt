package com.jrektor.skripsi.report

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.verification.LoginActivity
import kotlinx.android.synthetic.main.fragment_report_view.*

class WeeklyFragment : Fragment() {

    val weekList = ArrayList<ItemReport>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_report_view, container, false)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            handler.post {
                getReportWeekly(LoginActivity.OutletData.namaOutlet)
                pb_report.visibility = View.GONE
            }
        },5000)

        return view
    }

    private fun getReportWeekly(outlet: String) {
        val queue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(
            Request.Method.GET, GlobalData.BASE_URL+"order/orderweekly.php?in_outlet=$outlet", null,
            { response ->
                if (response.length() == 0){
                    Toast.makeText(context, "Data Kosong", Toast.LENGTH_SHORT).show()
                } else {
                    for (i in 0 until response.length()){
                        val obj = response.getJSONObject(i)
                        val nopekan = "Pekan ke-"+obj.getInt("nomor_pekan")
                        val total = obj.getInt("total")
                        val quantity = obj.getInt("quantity")

                        weekList.add(ItemReport(nopekan, "", total, quantity))
                        val adapter = AdapterReport(requireContext(), weekList)
                        rv_report.layoutManager = LinearLayoutManager(requireContext())
                        rv_report.adapter = adapter
                    }
                }
            },
            { error ->
                Log.d("error in weekly ", error.toString())
            })
        queue.add(request)
    }
}