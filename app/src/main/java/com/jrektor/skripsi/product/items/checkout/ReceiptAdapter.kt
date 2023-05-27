package com.jrektor.skripsi.product.items.checkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.cart.OrderItem
import kotlinx.android.synthetic.main.item_order_receipt.view.*

class ReceiptAdapter(var context: Context, var list: ArrayList<OrderItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class Adapter(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun adapter(context: Context, name: String, price: Int, quantity: Int){
            itemView.name_product_receipt.text = name
            itemView.price_product_receipt.text = price.toString()
            itemView.quantity_product_receipt.text = quantity.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_receipt, parent, false)

        return Adapter(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Adapter).adapter(context, list[position].name, list[position].price, list[position].quantity)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}