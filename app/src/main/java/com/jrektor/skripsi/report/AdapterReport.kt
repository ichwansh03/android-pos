package com.jrektor.skripsi.report

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.item_report.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterReport(var context: Context, var itemList: ArrayList<ItemReport>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ReportView(itemView: View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun adapter(context: Context, label: String, dates: String, total: Int, quantity: Int){
            val formatRp = NumberFormat.getCurrencyInstance(Locale("id","ID"))
            formatRp.minimumFractionDigits = 0

            itemView.tx_date.text = dates
            itemView.tx_name.text = label
            itemView.tx_total.text = formatRp.format(total)
            itemView.tx_quantity.text = quantity.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_report, parent, false)

        return ReportView(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ReportView).adapter(context, itemList[position].name, itemList[position].date, itemList[position].total, itemList[position].quantity)
    }
}