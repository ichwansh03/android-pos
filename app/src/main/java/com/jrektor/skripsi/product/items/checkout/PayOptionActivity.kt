package com.jrektor.skripsi.product.items.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.uikit.api.model.*
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import kotlinx.android.synthetic.main.activity_pay_option.*
import java.util.*

class PayOptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_option)

        total_bayar.text = GlobalData.totalBayar.toString()


        tunai.setOnClickListener {
            startActivity(Intent(this, CashPaymentActivity::class.java))
        }

        transfer.setOnClickListener {
            startActivity(Intent(this, MidtransPaymentActivity::class.java))
        }
    }
}