package com.jrektor.skripsi.report

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.fragment_laporan.*

class LaporanFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_laporan, container, false)

        getTotalPemasukan()

        val btnPenjualan = view.findViewById<CardView>(R.id.cv_penjualan)
        btnPenjualan.setOnClickListener {
            val penjualan = Intent(activity, PenjualanActivity::class.java)
            startActivity(penjualan)
        }

        val btnRugilaba = view.findViewById<CardView>(R.id.cv_rugilaba)
        btnRugilaba.setOnClickListener {
            val rugilaba = Intent(activity, RugiLabaActivity::class.java)
            startActivity(rugilaba)
        }

        return view
    }

    private fun getTotalPemasukan() {
        val queue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+"order/apireportall.php", null,
            { response ->
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val total = jsonObject.getInt("total")

                    tx_total_pemasukan.text = "Rp. $total"
                }
            }, {
                error ->
                Log.d("Error ", error.toString())
            })
        queue.add(request)
    }

}