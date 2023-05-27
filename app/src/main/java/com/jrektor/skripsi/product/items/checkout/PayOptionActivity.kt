package com.jrektor.skripsi.product.items.checkout

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.MainActivity
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
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import java.util.*

class PayOptionActivity : AppCompatActivity() {

    private var name: String = ""
    private var phone: String = ""

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                    deleteItem()
                    Toast.makeText(this, "Transaksi Berhasil ${transactionResult?.transactionId}", Toast.LENGTH_LONG).show()
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
                        deleteItem()
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

    private fun deleteItem() {
        val queue = Volley.newRequestQueue(this)
        val stringRequest =
            object : StringRequest(Method.DELETE, GlobalData.BASE_URL+"item/deleteitemall.php", Response.Listener { _ ->
                startActivity(Intent(this, MainActivity::class.java))
            }, { _ ->
                Toast.makeText(this, "Terjadi kesalahan saat menghapus produk", Toast.LENGTH_SHORT)
                    .show()
            }) {}
        queue.add(stringRequest)
    }

    private var itemDetails = listOf(ItemDetails("test-03", GlobalData.totalBayar.toDouble(), 1, "test-03"))

    private fun initTransactionDetails() : SnapTransactionDetail {
        return SnapTransactionDetail(
            orderId = UUID.randomUUID().toString(),
            grossAmount = GlobalData.totalBayar.toDouble()
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_option)

        name = intent.getStringExtra("name").toString()
        phone = intent.getStringExtra("phone").toString()

        val customerDetails = com.midtrans.sdk.uikit.api.model.CustomerDetails(
            "pembayaran",
            "dari $name",
            "- $phone",
            "ichwansholihin03@gmail.com"
        )

        total_bayar.text = "Rp."+GlobalData.totalBayar.toString()
        buildUiKit()

        tunai.setOnClickListener {
            val intent = Intent(this, CashPaymentActivity::class.java)
            intent.putExtra("total",GlobalData.totalBayar.toString())
            startActivity(intent)
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
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .build()
        uiKitCustomSetting()
    }

    private fun uiKitCustomSetting() {
        val uIKitCustomSetting = UIKitCustomSetting()
        uIKitCustomSetting.setSaveCardChecked(true)
        MidtransSDK.getInstance().setUiKitCustomSetting(uIKitCustomSetting)
    }

}