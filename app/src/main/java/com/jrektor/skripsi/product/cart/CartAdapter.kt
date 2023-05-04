package com.jrektor.skripsi.product.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.items.ModelProduct
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.cart_item.view.*

class CartAdapter(var context: Context, var data: ArrayList<ModelProduct>, var listener: Listeners) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    interface Listeners {
        fun onUpdate()
        fun onDelete(position: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.name_product_checkout.text
        var price = view.price_product_checkout.text
        var image = view.img_product_checkout
        var cardview = view.findViewById<CardView>(R.id.cv_item)

        var btnadd = view.findViewById<ImageButton>(R.id.btn_add_count)
        var btnmin = view.findViewById<ImageButton>(R.id.btn_min_count)
        var txcounts = view.findViewById<TextView>(R.id.tx_count)

        var btndelete = view.findViewById<TextView>(R.id.remove_cart)
        var checkbox = view.findViewById<CheckBox>(R.id.checkbox_cart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val cart = data[position]
        val prices = Integer.valueOf(cart.price)

        holder.name = cart.name
        //GlobalData.nameProduct = cart.name
        //holder.name = GlobalData.nameProduct

        holder.price = ((prices * cart.quantity).toString())
        //GlobalData.priceProduct = cart.price
        //holder.price = GlobalData.priceProduct.toString()

        holder.checkbox.isChecked = cart.selected
        holder.checkbox.setOnCheckedChangeListener { button, isChecked ->
            cart.selected = isChecked
            updateCarts(cart)
        }

        holder.btndelete.setOnClickListener {
            deleteCarts(cart)
            listener.onDelete(position)
        }

        val image = cart.image
        Glide.with(context).load(image).placeholder(R.drawable.ic_fastfood).into(holder.image)

        var quantities = data[position].quantity
        holder.txcounts.text = quantities.toString()
        holder.btnadd.setOnClickListener {
            quantities++
            cart.quantity = quantities
            updateCarts(cart)

            holder.txcounts.text = quantities.toString()
            //GlobalData.priceProduct = (prices * quantities)
        }

        holder.btnmin.setOnClickListener {
            if(quantities > 0){
                quantities--
                cart.quantity = quantities
                updateCarts(cart)

                holder.txcounts.text = quantities.toString()
                //GlobalData.priceProduct = (prices * quantities)
                GlobalData.jumlahBeli = quantities
            }
        }
    }

    private fun deleteCarts(cart: ModelProduct) {
        val database = CartDB.getInstance(context)
        CompositeDisposable().add(io.reactivex.Observable.fromCallable { database!!.daoCart().delete(cart) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listener.onUpdate()
            })
    }

    private fun updateCarts(cart: ModelProduct) {
        val database = CartDB.getInstance(context)
        CompositeDisposable().add(io.reactivex.Observable.fromCallable { database!!.daoCart().update(cart) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                listener.onUpdate()
            })
    }

    override fun getItemCount(): Int {
        return data.size
    }
}