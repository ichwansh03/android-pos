package com.jrektor.skripsi.product

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import kotlinx.android.synthetic.main.activity_checkout.*

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
        }

        btn_min_count.setOnClickListener {
            counter--
        }

        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-UZ9Yl7aefQos1858")
            .setContext(applicationContext)
            .setTransactionFinishedCallback(TransactionFinishedCallback {
                result ->
            })
            .setMerchantBaseUrl("http://192.168.43.8/pos/ppresponse.php/")
            .enableLog(true)
            .setLanguage("id")
            .buildSDK()

        btn_bayar.setOnClickListener {
            val totalBayar = counter* harga_produk.toDouble()
            tx_total_bayar.text = totalBayar.toString()
            GlobalData.jmlBeli = counter
            count = counter.toString()

            val transactionRequest = TransactionRequest("POS-Teknokrat-"+System.currentTimeMillis().toString() + "", totalBayar )
            val detail = com.midtrans.sdk.corekit.models.ItemDetails("NamaItemId",GlobalData.priceProduct.toDouble(), counter, "Nama Test")
            val itemDetails = ArrayList<com.midtrans.sdk.corekit.models.ItemDetails>()
            itemDetails.add(detail)
            uiKitDetails(transactionRequest)
            transactionRequest.itemDetails = itemDetails
            MidtransSDK.getInstance().transactionRequest = transactionRequest
            MidtransSDK.getInstance().startPaymentUiFlow(this)
        }
    }

    private fun uiKitDetails(transactionRequest: TransactionRequest) {
        val customerDetails = CustomerDetails()
        customerDetails.customerIdentifier = "Ichwan Sholihin"
        customerDetails.phone = "085766689697"
        customerDetails.firstName = "Ichwan"
        customerDetails.lastName = "Sholihin"
        customerDetails.email = "ichwansholihin03@gmail.com"
        val shippingAddress = ShippingAddress()
        shippingAddress.address = "Candimas, Natar"
        shippingAddress.city = "Lampung Selatan"
        shippingAddress.postalCode = "35362"
        customerDetails.shippingAddress = shippingAddress
        val billingAddress = BillingAddress()
        billingAddress.address  = "Candimas, Natar"
        billingAddress.city = "Lampung Selatan"
        billingAddress.postalCode = "35362"
        customerDetails.billingAddress = billingAddress

        transactionRequest.customerDetails = customerDetails
    }
}