package com.jrektor.skripsi.customer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_cart.view.*
import kotlinx.android.synthetic.main.consumer_list.view.*

class AdapterCustomer(var context: Context, var customerList: ArrayList<ItemCustomer>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class myAdapterCustomer(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun adapter(context: Context, names: String, phone: String, status: String){
            itemView.name_consumer.text = names
            itemView.phone_consumer.text = phone
            itemView.status_consumer.text = status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.consumer_list, parent, false)

        return myAdapterCustomer(view)
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as myAdapterCustomer).adapter(context, customerList[position].name, customerList[position].phone, customerList[position].status)

    }
}