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

        UiKitApi.Builder()
            .withMerchantClientKey("SB-Mid-client-UZ9Yl7aefQos1858")
            .withContext(this)
            .withMerchantUrl("http://pos-teknokrat.epizy.com/charge/") //400 BAD Request
            .enableLog(true)
            .build()

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                    Toast.makeText(this,"${transactionResult?.transactionId}", Toast.LENGTH_LONG).show()
                }
            }
        }
        val itemDetails = listOf(ItemDetails("Test01", 50000.00, 1, "lalalala"))

        tunai.setOnClickListener {
            startActivity(Intent(this, CashPaymentActivity::class.java))
        }

        transfer.setOnClickListener {

            UiKitApi.getDefaultInstance().startPaymentUiFlow(
                this,
                launcher,
                SnapTransactionDetail(UUID.randomUUID().toString(),50000.00, "IDR"),
                CustomerDetails("ichwan-0304","Ichwan","Sholihin","ichwansholihin03@gmail.com","085766689597",null,null),
                itemDetails,
                CreditCard(false,null,null,null,null,null,null,null,null,null),
                "customerIdentifier",
                null,
                GopayPaymentCallback("demo://midtrans"),
                null,
                null,
                PaymentMethod.CREDIT_CARD,
                listOf(PaymentType.CREDIT_CARD, PaymentType.GOPAY, PaymentType.SHOPEEPAY, PaymentType.UOB_EZPAY, PaymentType.INDOMARET, PaymentType.ALFAMART),null,null,null,null,"Cash1","Debit2","Credit3")
        }
    }
}