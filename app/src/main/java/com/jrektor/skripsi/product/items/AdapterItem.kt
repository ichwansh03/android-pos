package com.jrektor.skripsi.product.items

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.item_product.view.*

class AdapterItem(var context: Context, var list: ArrayList<ModelProduct>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class myAdapterProduk(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun adapter(
            context: Context,
            titles: String,
            prices: Int,
            category: String,
            images: String
        ) {
            itemView.name_product.text = titles
            itemView.price_product.text = "Rp. $prices"
            itemView.txcategory.text = category
            Glide.with(context).load(images).placeholder(R.drawable.ic_fastfood)
                .into(itemView.img_product)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)

        return myAdapterProduk(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as myAdapterProduk).adapter(
            context,
            list[position].name,
            list[position].price,
            list[position].catProduct,
            list[position].image
        )
        (holder).itemView.cv_product.setOnClickListener {
            val intent = Intent(context, DetailProductActivity::class.java)
            GlobalData.ids = list[position].id
            GlobalData.nameProduct = list[position].name
            GlobalData.merkProduct = list[position].merk
            GlobalData.priceProduct = list[position].price
            GlobalData.stockProduct = list[position].stock
            GlobalData.imageProduct = list[position].image
            GlobalData.descProduct = list[position].description
            val str = Gson().toJson(list[position], ModelProduct::class.java)
            intent.putExtra("extra", str)
            context.startActivity(intent)
        }
    }
}