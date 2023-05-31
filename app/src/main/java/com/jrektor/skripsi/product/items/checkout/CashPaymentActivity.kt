package com.jrektor.skripsi.product.items.checkout

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_cash_payment.*

class CashPaymentActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_payment)

        total_cash.text = "Rp.${GlobalData.totalBayar}"

        btn_pay_cash.setOnClickListener {
            if (cash_payment.text.toString().isEmpty() && cash_payment.text.toString().toInt() < GlobalData.totalBayar){
                Toast.makeText(this, "Isi total pembayaran dan tidak boleh kurang", Toast.LENGTH_SHORT).show()
            } else {
                DialogCashFragment().show(supportFragmentManager,"DialogCashFragment")
            }
        }
    }
}