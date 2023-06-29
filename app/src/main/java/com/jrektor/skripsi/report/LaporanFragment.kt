package com.jrektor.skripsi.report

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.items.AddProductActivity
import com.jrektor.skripsi.verification.LoginActivity
import kotlinx.android.synthetic.main.fragment_laporan.*
import java.text.NumberFormat
import java.util.*

class LaporanFragment : Fragment() {

    lateinit var totalProduk: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_laporan, container, false)

        totalProduk = view.findViewById(R.id.tx_total_produk)

        getTotalPemasukan(LoginActivity.OutletData.namaOutlet)
        getTotalPenjualan(LoginActivity.OutletData.namaOutlet)
        getTotalProduk(LoginActivity.OutletData.namaOutlet)

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

    @SuppressLint("SetTextI18n")
    private fun getTotalProduk(outlet: String) {
        val queue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+"order/totalproduct.php?in_outlet=$outlet", null,
            { response ->
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    if (!jsonObject.isNull("name")){
                        val name = jsonObject.getInt("name")
                        totalProduk.text = "$name Produk"
                    } else {
                        totalProduk.text = "0 Produk"
                    }

                }
            }, {
                    error ->
                Log.d("Error ", error.toString())
            })
        queue.add(request)
    }

    @SuppressLint("SetTextI18n")
    private fun getTotalPenjualan(outlet: String) {
        val queue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+"order/totalquantity.php?in_outlet=$outlet", null,
            { response ->
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    if (!jsonObject.isNull("quantity_harian")){
                        val harian = jsonObject.getInt("quantity_harian")
                        tx_total_penjualan_daily.text = "Hari ini: $harian Produk"
                        val pekanan = jsonObject.getInt("quantity_mingguan")
                        tx_total_penjualan_weekly.text = "Pekan ini: $pekanan Produk"
                        val bulanan = jsonObject.getInt("quantity_bulanan")
                        tx_total_penjualan_monthly.text = "Bulan ini: $bulanan Produk"
                    } else {
                        tx_total_penjualan_daily.text = "0 Produk"
                        tx_total_penjualan_weekly.text = "0 Produk"
                        tx_total_penjualan_monthly.text = "0 Produk"
                    }
                }
            }, {
                    error ->
                Log.d("Error ", error.toString())
            })
        queue.add(request)
    }

    @SuppressLint("SetTextI18n")
    private fun getTotalPemasukan(outlet: String) {
        val queue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+"order/totalorder.php?in_outlet=$outlet", null,
            { response ->
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val formatRp = NumberFormat.getCurrencyInstance(Locale("id","ID"))
                    formatRp.minimumFractionDigits = 0
                    if (!jsonObject.isNull("total_harian")){
                        val harian = jsonObject.getInt("total_harian")
                        tx_total_pemasukan_daily.text = "Hari ini: ${formatRp.format(harian)}"
                        val pekanan = jsonObject.getInt("total_mingguan")
                        tx_total_pemasukan_weekly.text = "Pekan ini: ${formatRp.format(pekanan)}"
                        val bulanan = jsonObject.getInt("total_bulanan")
                        tx_total_pemasukan_monthly.text = "Bulan ini: ${formatRp.format(bulanan)}"
                    } else {
                        tx_total_pemasukan_daily.text = "Rp.0"
                        tx_total_pemasukan_weekly.text = "Rp.0"
                        tx_total_pemasukan_monthly.text = "Rp.0"
                    }
                }
            }, {
                    error ->
                Log.d("Error ", error.toString())
            })
        queue.add(request)
    }

}