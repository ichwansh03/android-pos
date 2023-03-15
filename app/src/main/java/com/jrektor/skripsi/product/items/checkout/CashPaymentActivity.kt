package com.jrektor.skripsi.product.items.checkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_cash_payment.*

class CashPaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_payment)

        btn_pay_cash.setOnClickListener {
            DialogCashFragment().show(supportFragmentManager,"DialogCashFragment")
        }
    }
}