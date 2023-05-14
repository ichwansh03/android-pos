package com.jrektor.skripsi.product.items

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.item_add_product.view.*

class AdapterManageItem(var context: Context, var list: ArrayList<ModelProduct>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class myAddItemProduk(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun adapter(context: Context, names: String, images: String) {
            itemView.name_add_item.text = names
            Glide.with(context).load(images).placeholder(R.drawable.ic_fastfood)
                .into(itemView.img_add_item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_add_product, parent, false)

        return myAddItemProduk(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as myAddItemProduk).adapter(context, list[position].name, list[position].image)
        (holder).itemView.cv_add_item.setOnClickListener {
            val intent = Intent(context, FormEditProdukActivity::class.java)
            intent.putExtra("id",list[position].id)

            GlobalData.ids = list[position].id
            GlobalData.nameProduct = list[position].name
            GlobalData.merkProduct = list[position].merk
            GlobalData.priceProduct = list[position].price
            GlobalData.stockProduct = list[position].stock
            GlobalData.imageProduct = GlobalData.BASE_URL + "image/" + list[position].image
            GlobalData.descProduct = list[position].description
            context.startActivity(intent)
        }
    }
}