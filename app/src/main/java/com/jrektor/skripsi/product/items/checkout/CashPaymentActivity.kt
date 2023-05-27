package com.jrektor.skripsi.product.items.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_cash_payment.*

class CashPaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_payment)

        val total = intent.getIntExtra("total",0)

        btn_pay_cash.setOnClickListener {
            if (cash_payment.text.toString().isEmpty() || cash_payment.text.toString().toInt() < total){
                Toast.makeText(this, "Isi total pembayaran dan tidak boleh kurang", Toast.LENGTH_SHORT).show()
            } else {
                DialogCashFragment().show(supportFragmentManager,"DialogCashFragment")
            }
        }

        btn_print_struk.setOnClickListener {
            startActivity(Intent(this, PrintReceiptActivity::class.java))
        }
    }
}