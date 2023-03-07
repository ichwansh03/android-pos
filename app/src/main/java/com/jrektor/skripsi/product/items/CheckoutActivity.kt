package com.jrektor.skripsi.product.items

import android.annotation.SuppressLint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.midtrans.sdk.corekit.core.PaymentMethod
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

        btn_bayar.setOnClickListener {
            val totalBayar = counter* harga_produk.toDouble()
            tx_total_bayar.text = totalBayar.toString()
            GlobalData.jmlBeli = counter
            count = counter.toString()

            UiKitApi.Builder()
                .withMerchantClientKey("SB-Mid-client-UZ9Yl7aefQos1858") // client_key is mandatory
                .withContext(this) // context is mandatory
                .withMerchantUrl("http://192.168.43.8/charge/index.php/") // set transaction finish callback (sdk callback)
                .enableLog(true) // enable sdk log (optional)
                .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
                .build()
            setLocaleNew("id") //`en` for English and `id` for Bahasa
            //java.lang.IllegalStateException: LifecycleOwner com.jrektor.skripsi.product.items.CheckoutActivity@ccea1aa is attempting to register while current state is RESUMED. LifecycleOwners must call register before they are STARTED.
            val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result?.resultCode == RESULT_OK) {
                    result.data?.let {
                        val transactionResult = it.getParcelableExtra<TransactionResult>(
                            UiKitConstants.KEY_TRANSACTION_RESULT)
                        Toast.makeText(this,"${transactionResult?.transactionId}", Toast.LENGTH_LONG).show()
                    }
                }
            }
            val itemDetails = listOf(ItemDetails("Test01", 50000.00, 1, "lalalala"))

            UiKitApi.getDefaultInstance().startPaymentUiFlow(
                this@CheckoutActivity, // activity
                launcher, //ActivityResultLauncher
                SnapTransactionDetail(UUID.randomUUID().toString(), 50000.00, "IDR"), // Transaction Details
                CustomerDetails("budi-6789", "Budi", "Utomo", "budi@utomo.com", "0213213123", null, null), // Customer Details
                itemDetails, // Item Details
                CreditCard(false, null, null, null, null, null, null, null, null, null), // Credit Card
                "customerIdentifier", // User Id
                PaymentCallback("mysamplesdk://midtrans"), // UobEzpayCallback
                GopayPaymentCallback("mysamplesdk://midtrans"), // GopayCallback
                PaymentCallback("mysamplesdk://midtrans"), // ShopeepayCallback
                Expiry(getFormattedTime(System.currentTimeMillis()), Expiry.UNIT_HOUR, 5), // expiry (null: default expiry time)
                PaymentMethod.CREDIT_CARD, // Direct Payment Method Type
                listOf(PaymentType.CREDIT_CARD, PaymentType.GOPAY, PaymentType.SHOPEEPAY, PaymentType.UOB_EZPAY), // Enabled Payment (null: enabled all available payment)
                BankTransferRequest(vaNumber = "1234567890"), // Permata Custom VA (null: default va)
                BankTransferRequest(vaNumber = "12345"), // BCA Custom VA (null: default va)
                BankTransferRequest(vaNumber = "12345"), // BNI Custom VA (null: default va)
                BankTransferRequest(vaNumber = "12345"), // BRI Custom VA (null: default va)
                "Cash1", // Custom Field 1
                "Debit2", // Custom Field 2
                "Credit3"  // Custom Field 3
            )
        }
    }

    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    fun getFormattedTime(timestamp: Long): String {
        val date = Date(timestamp * 1000L)
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        return sdf.format(date)
    }
}