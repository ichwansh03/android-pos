package com.jrektor.skripsi.product.items

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.items.checkout.CheckoutActivity
import kotlinx.android.synthetic.main.activity_detail_product.*

class DetailProductActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)

        Glide.with(this@DetailProductActivity).load(GlobalData.imageProduct).into(img_product_detail)
        price_product_detail.text = "Rp. "+GlobalData.priceProduct.toString()
        name_product_detail.text = GlobalData.nameProduct
        merk_product.text = GlobalData.merkProduct
        stock_product.text = GlobalData.stockProduct.toString()
        desc_product.text = GlobalData.descProduct

        btn_checkout.setOnClickListener {
            val i = Intent(applicationContext, CheckoutActivity::class.java)
            startActivity(i)
        }
    }
}