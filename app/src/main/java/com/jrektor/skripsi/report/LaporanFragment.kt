package com.jrektor.skripsi.report

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.jrektor.skripsi.R

class LaporanFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_laporan, container, false)

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

}