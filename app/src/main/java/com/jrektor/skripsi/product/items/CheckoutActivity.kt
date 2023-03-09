package com.jrektor.skripsi.product.items

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.midtrans.sdk.uikit.api.model.*
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import kotlinx.android.synthetic.main.activity_checkout.*
import java.text.SimpleDateFormat
import java.util.*

class CheckoutActivity : AppCompatActivity() {

    var counter:Int = 0
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        var urlOrder = "http://192.168.43.8/pos/addorder.php"

        UiKitApi.Builder()
            .withMerchantClientKey("SB-Mid-client-UZ9Yl7aefQos1858")
            .withContext(this)
            .withMerchantUrl("http://192.168.43.8/charge/index.php/")
            .enableLog(true)
            .build()

        var nama = nama_pelanggan.text
        var nohp = nohp_pelanggan.text
        var catatan = txcatatan.text
        var count = tx_count.text
        Glide.with(this@CheckoutActivity).load(GlobalData.imageProduct).into(img_product_checkout)
        name_product_checkout.text = GlobalData.nameProduct
        price_product_checkout.text = "Rp. ${GlobalData.priceProduct}"
        val harga_produk = GlobalData.priceProduct

        btn_add_count.setOnClickListener {
            counter++
            count = counter.toString()
        }

        btn_min_count.setOnClickListener {
            if (counter > 0){
                counter--
                count = counter.toString()
            }
        }

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                    Toast.makeText(this,"${transactionResult?.transactionId}", Toast.LENGTH_LONG).show()
                }
            }
        }
        val itemDetails = listOf(ItemDetails("Test01", 50000.00, 1, "lalalala"))

        btn_bayar.setOnClickListener {
            val totalBayar = counter* harga_produk.toDouble()
            tx_total_bayar.text = totalBayar.toString()
            GlobalData.jmlBeli = counter
            count = counter.toString()

            UiKitApi.getDefaultInstance().startPaymentUiFlow(
                this@CheckoutActivity,
                launcher,
                SnapTransactionDetail(UUID.randomUUID().toString(),50000.00, "IDR"),
                CustomerDetails("ichwan-0304","Ichwan","Sholihin","ichwansholihin03@gmail.com","085766689597",null,null),
                itemDetails,
                CreditCard(false,null,null,null,null,null,null,null,null,null),
                "customerIdentifier",
                PaymentCallback("demo://midtrans"),
                GopayPaymentCallback("demo://midtrans"),
                PaymentCallback("demo://midtrans"),
                null,
                PaymentMethod.CREDIT_CARD,
                listOf(PaymentType.CREDIT_CARD, PaymentType.GOPAY, PaymentType.SHOPEEPAY, PaymentType.UOB_EZPAY, PaymentType.INDOMARET, PaymentType.ALFAMART),null,null,null,null,"Cash1","Debit2","Credit3")
        }
    }
}

