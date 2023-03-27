package com.jrektor.skripsi.product.items

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.cart.CartDB
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_product.*

class DetailProductActivity : AppCompatActivity() {

    lateinit var cartDB: CartDB
    //kotlin.UninitializedPropertyAccessException: lateinit property cartItem has not been initialized
    lateinit var cartItem: ModelProduct
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        cartDB = CartDB.getInstance(this)!!

        detailProduct()

        btn_checkout.setOnClickListener {
            val data = cartDB.daoCart().getProduct(cartItem.id)

            if (data == null){
                insertData()
            } else {
                data.quantity += 1
                updateData(data)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun detailProduct() {
        Glide.with(this@DetailProductActivity).load(GlobalData.imageProduct).into(img_product_detail)
        price_product_detail.text = "Rp. ${GlobalData.priceProduct.toString()}"
        name_product_detail.text = GlobalData.nameProduct
        merk_product.text = "Merk : "+GlobalData.merkProduct
        stock_product.text = "Stok Tersedia : "+GlobalData.stockProduct.toString()
        desc_product.text = GlobalData.descProduct
    }

    private fun updateData(data: ModelProduct) {
        CompositeDisposable().add(io.reactivex.Observable.fromCallable { cartDB.daoCart().update(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("respons","data inserted")
                Toast.makeText(this, "Berhasil menambahkan ke keranjang",Toast.LENGTH_SHORT).show()
            })
    }

    private fun insertData() {
        CompositeDisposable().add(io.reactivex.Observable.fromCallable { cartDB.daoCart().insert(cartItem) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("respons","data inserted")
                Toast.makeText(this, "Berhasil menambahkan ke keranjang",Toast.LENGTH_SHORT).show()
            })
    }

}