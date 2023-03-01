package com.jrektor.skripsi.product.items

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.AdapterProduk
import com.jrektor.skripsi.product.ItemProduk
import kotlinx.android.synthetic.main.fragment_add_item.*
import kotlinx.android.synthetic.main.fragment_produk.*

class AddItemFragment : Fragment() {

    var list = ArrayList<ItemProduk>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_item, container, false)
        getProduct()
        fab_add_item.setOnClickListener {
            startActivity(Intent(activity, AddItemActivity::class.java))
        }
        return view
    }

    private fun getProduct() {
        val queue: RequestQueue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(
            Request.Method.GET, "http://192.168.43.8/pos/apiproduct.php", null,
            { response ->
                for (s in 0 until response.length()) {
                    val jObject = response.getJSONObject(s)
                    val id = jObject.getInt("id")
                    val name = jObject.getString("name")
                    val price = jObject.getInt("price")
                    val image = jObject.getString("image").replace("localhost","192.168.43.8")
                    val stock = jObject.getInt("stock")
                    val merk = jObject.getString("merk")
                    val desc = jObject.getString("description")

                    list.add(ItemProduk(id, name, price, merk, stock, image, desc))
                    val adapter = AdapterAddItem(requireContext(), list)
                    rv_add_item.layoutManager = LinearLayoutManager(requireContext())
                    rv_add_item.adapter = adapter
                }
            },
            { error ->
                Log.d("Error", error.toString())
            })
        queue.add(request)
    }

}