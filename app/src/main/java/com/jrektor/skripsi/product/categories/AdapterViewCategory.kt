package com.jrektor.skripsi.product.categories

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.item_category.view.*

class AdapterViewCategory(var context: Context, var catList: ArrayList<ItemCategory>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class myAdapterCategory(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun adapter(names: String) {
            itemView.name_category.text = names
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)

        return myAdapterCategory(view)
    }

    override fun getItemCount(): Int {
        return catList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as myAdapterCategory).adapter(catList[position].nameCategory)
        (holder).itemView.cv_category_list.setOnClickListener {

        }
    }
}