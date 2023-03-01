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
import com.jrektor.skripsi.product.ItemProduk
import kotlinx.android.synthetic.main.item_add_product.view.*

class AdapterAddItem(var context: Context, var list: ArrayList<ItemProduk>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class myAddItemProduk(itemView: View): RecyclerView.ViewHolder(itemView){
        fun adapter(context: Context, names: String, images: String){
            itemView.name_add_item.text = names
            Glide.with(context).load(images).into(itemView.img_add_item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_add_product, parent, false)

        return myAddItemProduk(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as myAddItemProduk).adapter(context, list[position].name, list[position].image)
        (holder).itemView.cv_add_item.setOnClickListener {
            val intent = Intent(context, AddItemActivity::class.java)
            GlobalData.ids = list[position].id
            context.startActivity(intent)
        }
    }
}