package com.jrektor.skripsi.outlet.employee

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jrektor.skripsi.R
import com.jrektor.skripsi.verification.LoginActivity
import kotlinx.android.synthetic.main.item_pegawai.view.*

class AdapterPegawai(var context: Context, var pegawaiList: ArrayList<ItemPegawai>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class myAdapterPegawai(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun adapter(context: Context, names: String, job: String, outlet: String, phone: String, image: String){
            itemView.nama_pegawai.text = names
            itemView.jabatan.text = job
            itemView.phone.text = phone
            itemView.outlet.text = " di $outlet"
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
        (holder as myAdapterPegawai).adapter(context, pegawaiList[position].name, pegawaiList[position].job, pegawaiList[position].branch, pegawaiList[position].phone, pegawaiList[position].image)

        if (LoginActivity.OutletData.pekerjaan != "Karyawan") {
            (holder).itemView.cv_pegawai.setOnClickListener {
                val intent = Intent(context, EditPegawaiActivity::class.java)
                intent.putExtra("id",pegawaiList[position].id)
                context.startActivity(intent)
            }
        }
    }

    fun getData(pegawaiList: ArrayList<ItemPegawai>) {
        this.pegawaiList = pegawaiList
        notifyDataSetChanged()
    }

}