package com.jrektor.skripsi.product.cart

import android.content.Context
import android.provider.Settings.Global
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
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
        val name = view.name_product_checkout.text
        val price = view.price_product_checkout.text
        val image = view.img_product_checkout
        val cardview = view.findViewById<CardView>(R.id.cv_item)

        val btnadd = view.findViewById<ImageButton>(R.id.btn_add_count)
        val btnmin = view.findViewById<ImageButton>(R.id.btn_min_count)
        val txcounts = view.findViewById<TextView>(R.id.tx_count)

        val btndelete = view.findViewById<TextView>(R.id.remove_cart)
        val checkbox = view.findViewById<CheckBox>(R.id.checkbox_cart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val cart = data[position]
        val prices = Integer.valueOf(cart.price)

        GlobalData.nameProduct = cart.name
        GlobalData.priceProduct = (prices * cart.quantity)

        var quantities = data[position].quantity
        holder.txcounts.text = quantities.toString()

        holder.checkbox.isChecked = cart.selected
        holder.checkbox.setOnCheckedChangeListener { button, isChecked ->
            cart.selected = isChecked
            updateCarts(cart)
        }

        holder.btndelete.setOnClickListener {
            deleteCarts(cart)
            listener.onDelete(position)
        }

        val image = data[position].image
        Glide.with(context).load(image).placeholder(R.drawable.ic_fastfood).into(holder.image)

        holder.btnadd.setOnClickListener {
            quantities++
            cart.quantity = quantities
            updateCarts(cart)

            holder.txcounts.text = quantities.toString()
            GlobalData.priceProduct = (prices * quantities)
        }

        holder.btnmin.setOnClickListener {
            if (quantities <= 1) return@setOnClickListener
            quantities--
            cart.quantity = quantities
            updateCarts(cart)

            holder.txcounts.text = quantities.toString()
            GlobalData.priceProduct = (prices * quantities)
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