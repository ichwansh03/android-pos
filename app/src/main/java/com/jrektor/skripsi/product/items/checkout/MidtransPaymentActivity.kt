package com.jrektor.skripsi.product.items.checkout

import android.widget.Button
import com.jrektor.skripsi.R
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_CANCELED
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_FAILED
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_INVALID
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_PENDING
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_SUCCESS
import java.util.*
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants

class MidtransPaymentActivity : AppCompatActivity() {

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
        "user fullname",
        "ichwan",
        "sholihin",
        "ichwansholihin03@gmail.com"
    )
    private var itemDetails = listOf(ItemDetails("test-03", 36400.0, 1, "test03"))

    private fun initTransactionDetails() : SnapTransactionDetail {
        return SnapTransactionDetail(
            orderId = UUID.randomUUID().toString(),
            grossAmount = 36400.0
        )
    }

    lateinit var btnUiKit: Button
    lateinit var btnCreditCard: Button
    lateinit var btnMandiriVa: Button
    lateinit var btnBniVa: Button
    lateinit var btnAtmBersama: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_midtrans_payment)

        bindViews()
        buildUiKit()
        btnUiKit.setOnClickListener {
            UiKitApi.getDefaultInstance().startPaymentUiFlow(
                this@MidtransPaymentActivity,
                launcher,
                initTransactionDetails(),
                customerDetails,
                itemDetails,
            )
        }

        btnCreditCard.setOnClickListener {
            UiKitApi.getDefaultInstance().startPaymentUiFlow(
                this@MidtransPaymentActivity,
                launcher,
                initTransactionDetails(),
                customerDetails,
                itemDetails,
                paymentMethod = PaymentMethod.CREDIT_CARD
            )
        }

        btnMandiriVa.setOnClickListener {
            UiKitApi.getDefaultInstance().startPaymentUiFlow(
                this@MidtransPaymentActivity,
                launcher,
                initTransactionDetails(),
                customerDetails,
                itemDetails,
                paymentMethod = PaymentMethod.BANK_TRANSFER_MANDIRI
            )
        }

        btnBniVa.setOnClickListener {
            UiKitApi.getDefaultInstance().startPaymentUiFlow(
                this@MidtransPaymentActivity,
                launcher,
                initTransactionDetails(),
                customerDetails,
                itemDetails,
                paymentMethod = PaymentMethod.BANK_TRANSFER_OTHER
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

    private fun bindViews() {
        btnUiKit = findViewById(R.id.btn_uikit)
        btnCreditCard = findViewById(R.id.btn_credit_card)
        btnMandiriVa = findViewById(R.id.btn_mandiri)
        btnBniVa = findViewById(R.id.btn_bni_va)
        btnAtmBersama = findViewById(R.id.btn_atm_bersama)
    }

}