package com.jrektor.skripsi.product.cart

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import org.json.JSONException

class OrderItemAdapter(var context: Context, var list: ArrayList<OrderItem>, var listener: ItemListener)  : RecyclerView.Adapter<OrderItemAdapter.ViewHolder>(){

    interface ItemListener {
        fun onUpdate()
        fun onDelete(position: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.findViewById<TextView>(R.id.name_product_checkout)
        var price = view.findViewById<TextView>(R.id.price_product_checkout)
        var cardview = view.findViewById<CardView>(R.id.cv_item)

        var btnadd = view.findViewById<ImageButton>(R.id.btn_add_count)
        var btnmin = view.findViewById<ImageButton>(R.id.btn_min_count)
        var txcounts = view.findViewById<TextView>(R.id.tx_count)

        var btndelete = view.findViewById<TextView>(R.id.remove_cart)
        var checkbox = view.findViewById<CheckBox>(R.id.checkbox_cart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = list[position]

        holder.name.text = cart.name
        holder.price.text = "Rp. "+cart.price.toString()

        holder.checkbox.isChecked = cart.selected
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            cart.selected = isChecked
            updateCart(cart)
        }

        holder.btndelete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Hapus Produk")
            alertDialog.setMessage("Apakah anda yakin ingin menghapus produk ini dari keranjang belanja?")
            alertDialog.setPositiveButton("Ya") { _, _ ->
                if (list.contains(cart)) {
                    deleteCart(cart)
                    listener.onDelete(position)
                }
            }
            alertDialog.setNegativeButton("Tidak", null)
            alertDialog.show()
        }

        var quantities = cart.quantity
        holder.txcounts.text = quantities.toString()
        holder.btnadd.setOnClickListener {
            quantities++
            cart.quantity = quantities
            updateCart(cart)

            holder.txcounts.text = quantities.toString()
        }

        holder.btnmin.setOnClickListener {
            if(quantities > 0){
                quantities--
                cart.quantity = quantities
                updateCart(cart)

                holder.txcounts.text = quantities.toString()
                //GlobalData.priceProduct = (prices * quantities)
                GlobalData.jumlahBeli = quantities
            }
        }
    }

    private fun deleteCart(cart: OrderItem) {
        val queue = Volley.newRequestQueue(context)
        val url = GlobalData.BASE_URL + "item/deleteitem.php?id=" + cart.id

        val request = StringRequest(
            Request.Method.DELETE, url,
            { _ ->
                try {
                    listener.onUpdate()
                } catch (e: JSONException) {
                    Toast.makeText(context, "Error occurred while deleting cart.", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            })

        queue.add(request)
    }

    private fun updateCart(cart: OrderItem) {
        val requestQueue = Volley.newRequestQueue(context)

        val url = GlobalData.BASE_URL + "item/updateitem.php"

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { _ ->
                listener.onUpdate()
            },
            Response.ErrorListener { error ->
                Log.d("Error ", error.message.toString())
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = cart.id.toString()
                params["name"] = cart.name
                params["price"] = cart.price.toString()
                params["quantity"] = cart.quantity.toString()
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getItemCountData(): Int {
        return list.size
    }
}