package com.jrektor.skripsi.product.items.checkout

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.uikit.api.model.*
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import kotlinx.android.synthetic.main.activity_checkout.*
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
            GlobalData.totalBayar = totalBayar.toInt()
            GlobalData.jmlBeli = counter
            count = counter.toString()

            startActivity(Intent(this, PayOptionActivity::class.java))
        }
    }
}

