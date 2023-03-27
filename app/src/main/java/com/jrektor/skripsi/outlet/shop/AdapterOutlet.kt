package com.jrektor.skripsi.outlet.shop

import android.content.Context
import android.content.Intent
import android.provider.Settings.Global
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.item_outlet.view.*

class AdapterOutlet(var context: Context, var list: ArrayList<ItemOutlet>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class myAdapterOutlet(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun adapter(context: Context, name: String, address: String, image: String){
            itemView.tipe_outlet.text = name
            itemView.alamat_outlet.text = address
            Glide.with(context).load(image).placeholder(R.drawable.ic_outlet).into(itemView.img_outlet)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_outlet, parent, false)

        return myAdapterOutlet(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as myAdapterOutlet).adapter(context, list[position].nameOutlet, list[position].addressOutlet, list[position].imageOutlet)
        (holder).itemView.cv_outlet.setOnClickListener {
            val intent = Intent(context, EditOutletActivity::class.java)
            GlobalData.idOutlet = list[position].idOutlet
            GlobalData.nameOutlet = list[position].nameOutlet
            GlobalData.addressOutlet = list[position].addressOutlet
            GlobalData.imageOutlet = list[position].imageOutlet
            context.startActivity(intent)
        }
    }
}