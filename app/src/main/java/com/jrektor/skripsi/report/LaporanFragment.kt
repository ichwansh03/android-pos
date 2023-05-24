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
import com.jrektor.skripsi.product.items.AddProductActivity
import kotlinx.android.synthetic.main.fragment_laporan.*

class LaporanFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_laporan, container, false)

        getTotalPemasukan(GlobalData.nameOutlet)
        getTotalPenjualan(GlobalData.nameOutlet)
        getTotalProduk(GlobalData.nameOutlet)

        val btnPenjualan = view.findViewById<CardView>(R.id.cv_penjualan)
        btnPenjualan.setOnClickListener {
            val penjualan = Intent(activity, PenjualanActivity::class.java)
            startActivity(penjualan)
        }

        val btnRugilaba = view.findViewById<CardView>(R.id.cv_rugilaba)
        btnRugilaba.setOnClickListener {
            val rugilaba = Intent(activity, PenjualanActivity::class.java)
            startActivity(rugilaba)
        }

        val btnTotalProduk = view.findViewById<CardView>(R.id.cv_produk)
        btnTotalProduk.setOnClickListener {
            val produk = Intent(activity, AddProductActivity::class.java)
            startActivity(produk)
        }

        return view
    }

    private fun getTotalProduk(outlet: String) {
        val queue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+"order/totalproduct.php?in_outlet=$outlet", null,
            { response ->
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val name = jsonObject.getInt("name")

                    tx_total_produk.text = "$name Produk"
                }
            }, {
                    error ->
                Log.d("Error ", error.toString())
            })
        queue.add(request)
    }

    private fun getTotalPenjualan(outlet: String) {
        val queue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+"order/quantitiesall.php?in_outlet=$outlet", null,
            { response ->
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val quantity = jsonObject.getInt("quantity")

                    tx_total_penjualan.text = "$quantity Produk"
                }
            }, {
                    error ->
                Log.d("Error ", error.toString())
            })
        queue.add(request)
    }

    private fun getTotalPemasukan(outlet: String) {
        val queue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+"order/totalorderall.php?in_outlet=$outlet", null,
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