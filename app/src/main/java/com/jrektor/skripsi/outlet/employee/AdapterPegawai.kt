package com.jrektor.skripsi.outlet.employee

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jrektor.skripsi.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_pegawai.view.*

class AdapterPegawai(var context: Context, var pegawaiList: ArrayList<ItemPegawai>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class myAdapterPegawai(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun adapter(names: String, job: String, image: String){
            itemView.nama_pegawai.text = names
            itemView.jabatan.text = job
            Picasso.get().load(image).into(itemView.img_pegawai)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_pegawai, parent, false)

        return myAdapterPegawai(view)
    }

    override fun getItemCount(): Int {
        return pegawaiList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as myAdapterPegawai).adapter(pegawaiList[position].name, pegawaiList[position].job, pegawaiList[position].image)
        (holder).itemView.cv_pegawai.setOnClickListener {

        }
    }
}