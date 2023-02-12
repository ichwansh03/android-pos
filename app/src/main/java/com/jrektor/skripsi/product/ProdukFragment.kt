package com.jrektor.skripsi.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.items.AdapterProduk
import com.jrektor.skripsi.product.items.ItemProduk
import kotlinx.android.synthetic.main.fragment_produk.*

class ProdukFragment : Fragment() {

    var list = ArrayList<ItemProduk>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_produk, container, false)
        getProduct()
        return view
    }

    private fun getProduct() {
        var queue: RequestQueue = Volley.newRequestQueue(activity)
        var request = JsonArrayRequest(Request.Method.GET, "http://10.0.2.2:5554/pos/apiproduct.php", null,
            { response ->
                for (s in 0 until response.length()) {
                    var jObject = response.getJSONObject(s)
                    var id = jObject.getInt("id")
                    var name = jObject.getString("name")
                    var price = jObject.getInt("price")
                    var merk = jObject.getString("merk")
                    var stock = jObject.getInt("stock")
                    var image = jObject.getString("image").replace("localhost","10.0.2.2")
                    var description = jObject.getString("description")

                    list.add(ItemProduk(id, name, price, merk, stock, image, description))
                    var adapter = AdapterProduk(requireContext(), list)
                    rv_product.layoutManager = GridLayoutManager(requireContext(),2)
                    rv_product.adapter = adapter
                }
            },
            { error ->
                Log.d("Error", error.toString())
            })
        queue.add(request)
    }

}