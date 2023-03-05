package com.jrektor.skripsi.report

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.fragment_laporan.*

class LaporanFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_laporan, container, false)

        cv_penjualan.setOnClickListener {
            val intent = Intent(activity, PenjualanActivity::class.java)
            startActivity(intent)
        }

        cv_rugilaba.setOnClickListener {
            val intent = Intent(activity, RugiLabaActivity::class.java)
            startActivity(intent)
        }
        return view
    }

}