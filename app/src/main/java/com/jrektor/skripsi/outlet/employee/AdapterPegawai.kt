package com.jrektor.skripsi.outlet.employee

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.item_pegawai.view.*

class AdapterPegawai(var context: Context, var pegawaiList: ArrayList<ItemPegawai>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class myAdapterPegawai(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun adapter(context: Context, names: String, job: String, phone: String, image: String){
            itemView.nama_pegawai.text = names
            itemView.jabatan.text = job
            itemView.phone.text = phone
            Glide.with(context).load(image).placeholder(R.drawable.ic_pegawai).into(itemView.img_pegawai)
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
        (holder as myAdapterPegawai).adapter(context, pegawaiList[position].name, pegawaiList[position].job, pegawaiList[position].phone, pegawaiList[position].image)
        (holder).itemView.cv_pegawai.setOnClickListener {
            val intent = Intent(context, EditPegawaiActivity::class.java)
            intent.putExtra("id",pegawaiList[position].id)
            GlobalData.idPegawai = pegawaiList[position].id
            GlobalData.namePegawai = pegawaiList[position].name
            GlobalData.jobPegawai = pegawaiList[position].job
            GlobalData.phonePegawai = pegawaiList[position].phone
            context.startActivity(intent)
        }
    }
}