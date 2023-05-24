package com.jrektor.skripsi.report

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_penjualan.*

class PenjualanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penjualan)

        addFragment(DailyFragment())

        tab_harian.setOnClickListener {
            tab_harian.setCardBackgroundColor(ContextCompat.getColor(this, R.color.primary))
            tx_cv_harian.setTextColor(Color.WHITE)
            tab_pekanan.setCardBackgroundColor(Color.WHITE)
            tx_cv_pekanan.setTextColor(Color.BLACK)
            tab_bulanan.setCardBackgroundColor(Color.WHITE)
            tx_cv_bulanan.setTextColor(Color.BLACK)

            addFragment(DailyFragment())
        }

        tab_pekanan.setOnClickListener {
            tab_pekanan.setCardBackgroundColor(ContextCompat.getColor(this, R.color.primary))
            tx_cv_pekanan.setTextColor(Color.WHITE)
            tab_harian.setCardBackgroundColor(Color.WHITE)
            tx_cv_harian.setTextColor(Color.BLACK)
            tab_bulanan.setCardBackgroundColor(Color.WHITE)
            tx_cv_bulanan.setTextColor(Color.BLACK)

            addFragment(WeeklyFragment())

        }

        tab_bulanan.setOnClickListener {
            tab_bulanan.setCardBackgroundColor(ContextCompat.getColor(this, R.color.primary))
            tx_cv_bulanan.setTextColor(Color.WHITE)
            tab_harian.setCardBackgroundColor(Color.WHITE)
            tx_cv_harian.setTextColor(Color.BLACK)
            tab_pekanan.setCardBackgroundColor(Color.WHITE)
            tx_cv_pekanan.setTextColor(Color.BLACK)

            addFragment(MonthlyFragment())
        }

    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.report_container, fragment)
            .commit()
    }

}
