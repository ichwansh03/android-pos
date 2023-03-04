package com.jrektor.skripsi.product.items

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.jrektor.skripsi.product.categories.CategoryFragment
import kotlinx.android.synthetic.main.fragment_produk.*

class ProdukFragment : Fragment() {

    var list = ArrayList<ItemProduk>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_produk, container, false)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            handler.post {
                getProduct()
                pb_main_product.visibility = View.GONE
            }
        },5000)

        btn_search_cat.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, CategoryFragment())
                .commit()
        }

        return view
    }

    private fun getProduct() {

        val queue: RequestQueue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(Request.Method.GET, "http://192.168.43.8/pos/apiproduct.php", null,
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
                    val adapter = AdapterProduk(requireContext(), list)
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