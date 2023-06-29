package com.jrektor.skripsi.product.items.checkout

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.MainActivity
import com.jrektor.skripsi.R
import com.jrektor.skripsi.verification.LoginActivity
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
import java.text.NumberFormat
import java.util.*
import kotlin.collections.HashMap

class PayOptionActivity : AppCompatActivity() {

    private var status: String = ""

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                    deleteItem()
                    getPaymentStatus(transactionResult?.transactionId)
                    Toast.makeText(this, "Transaksi Berhasil ${transactionResult?.transactionId}", Toast.LENGTH_LONG).show()
                    insertPaymentStatus(transactionResult?.transactionId)
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

    private var itemDetails = listOf(ItemDetails("id-11", GlobalData.totalBayar.toDouble(), 1, "id-11"))

    private fun initTransactionDetails() : SnapTransactionDetail {
        val orderID = UUID.randomUUID().toString()
        return SnapTransactionDetail(
            orderId = orderID,
            grossAmount = GlobalData.totalBayar.toDouble()
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_option)

        val customerDetails = com.midtrans.sdk.uikit.api.model.CustomerDetails(
            "pembayaran",
            "${GlobalData.namaPelanggan} dari ${LoginActivity.OutletData.namaOutlet}",
            " No.HP: ${GlobalData.nohpPelanggan}",
            "ichwansholihin03@gmail.com"
        )

        val total = GlobalData.totalBayar
        val formatRp = NumberFormat.getCurrencyInstance(Locale("id","ID"))
        formatRp.minimumFractionDigits = 0
        val totalFormatted = formatRp.format(total)
        total_bayar.text = totalFormatted

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

    private fun insertPaymentStatus(transactionId: String?) {
        val url = GlobalData.BASE_URL + "order/midtrans_status.php"
        val request = Volley.newRequestQueue(applicationContext)

        val stringRequest = object : StringRequest(
            Method.GET,
            "$url?order_id=$transactionId&name=${GlobalData.namaPelanggan}&phone=${GlobalData.nohpPelanggan}&payment_status=$status",
            { response ->
                if (response == "1") {
                    //success
                }
            },
            { error ->
                Toast.makeText(this, "Error inserting payment status: ${error.toString()}", Toast.LENGTH_LONG).show()
                Log.d("Error insert payment status", error.toString())
            }) {}

        request.add(stringRequest)
    }

    private fun getPaymentStatus(transactionId: String?) {
        val url = "https://api.sandbox.midtrans.com/v2/$transactionId/status"

        val headers = HashMap<String, String>()
        headers["Accept"] = "application/json"
        headers["Content-Type"] = "application/json"
        headers["Authorization"] = "Basic U0ItTWlkLXNlcnZlci1BVHhkbmowM1Q0bGRmQ3c1TGI1bkVUMkM6"

        val request = object : JsonObjectRequest(
            Method.GET, url, null,
            { response ->
                val transactionStatus = response.getString("transaction_status")
                status = transactionStatus
            },
            { error ->
                Log.d("Error to get payment status", error.toString())
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                return headers
            }
        }

        // Create a request queue
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
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