package com.jrektor.skripsi.product.items.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_pay_option.*
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_CANCELED
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_FAILED
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_INVALID
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_PENDING
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_SUCCESS
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import java.util.*

class PayOptionActivity : AppCompatActivity() {

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                    Toast.makeText(this, "${transactionResult?.transactionId}", Toast.LENGTH_LONG).show()
                }
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val transactionResult = data?.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
            if (transactionResult != null) {
                when (transactionResult.status) {
                    STATUS_SUCCESS -> {
                        Toast.makeText(this, "Transaction Finished. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                    }
                    STATUS_PENDING -> {
                        Toast.makeText(this, "Transaction Pending. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                    }
                    STATUS_FAILED -> {
                        Toast.makeText(this, "Transaction Failed. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                    }
                    STATUS_CANCELED -> {
                        Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_LONG).show()
                    }
                    STATUS_INVALID -> {
                        Toast.makeText(this, "Transaction Invalid. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this, "Transaction ID: " + transactionResult.transactionId + ". Message: " + transactionResult.status, Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private var customerDetails = com.midtrans.sdk.uikit.api.model.CustomerDetails(
        GlobalData.namePelanggan,
        GlobalData.namePelanggan,
        "- ${GlobalData.phonePelanggan}",
        GlobalData.email
    )
    private var itemDetails = listOf(ItemDetails("test-03", GlobalData.totalBayar.toDouble(), 1, GlobalData.nameProduct))

    private fun initTransactionDetails() : SnapTransactionDetail {
        return SnapTransactionDetail(
            orderId = UUID.randomUUID().toString(),
            grossAmount = GlobalData.totalBayar.toDouble()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_option)

        total_bayar.text = GlobalData.totalBayar.toString()
        buildUiKit()

        tunai.setOnClickListener {
            startActivity(Intent(this, CashPaymentActivity::class.java))
        }

        transfer.setOnClickListener {
            UiKitApi.getDefaultInstance().startPaymentUiFlow(
                this@PayOptionActivity,
                launcher,
                initTransactionDetails(),
                customerDetails,
                itemDetails,
            )
        }
    }

    private fun buildUiKit() {
        UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl("http://192.168.43.8/charge/midtrans.php/")
            .withMerchantClientKey("SB-Mid-client-UZ9Yl7aefQos1858")
            .enableLog(true)
            .withColorTheme(com.midtrans.sdk.uikit.api.model.CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .build()
        uiKitCustomSetting()
    }

    private fun uiKitCustomSetting() {
        val uIKitCustomSetting = UIKitCustomSetting()
        uIKitCustomSetting.setSaveCardChecked(true)
        MidtransSDK.getInstance().setUiKitCustomSetting(uIKitCustomSetting)
    }

}