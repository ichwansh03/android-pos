package com.jrektor.skripsi.product.items

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.cart.CartActivity
import com.jrektor.skripsi.product.categories.CategoryFragment
import kotlinx.android.synthetic.main.fragment_item.*

class ItemFragment : Fragment() {

    var list = ArrayList<ModelProduct>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_item, container, false)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            handler.post {
                getProduct()
                pb_main_product.visibility = View.GONE
            }
        },5000)

        val search = view.findViewById<ImageButton>(R.id.btn_search_cat)
        search.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, CategoryFragment())
                .commit()
        }

        val fabCart = view.findViewById<FloatingActionButton>(R.id.fab_cart)
        fabCart.setOnClickListener {
            startActivity(Intent(requireActivity(), CartActivity::class.java))
        }

        return view
    }

    private fun getProduct() {

        val queue: RequestQueue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+"product/apiproduct.php", null,
            { response ->
                for (s in 0 until response.length()) {
                    val jObject = response.getJSONObject(s)
                    val id = jObject.getInt("id")
                    val name = jObject.getString("name")
                    val price = jObject.getInt("price")
                    val image = jObject.getString("image").replace("http://localhost/pos/",GlobalData.BASE_URL)
                    val stock = jObject.getInt("stock")
                    val merk = jObject.getString("merk")
                    val desc = jObject.getString("description")
                    val catProduct = jObject.getString("cat_product")

                    list.add(ModelProduct(id, name, price, merk, stock, catProduct, image, desc))
                    val adapter = AdapterItem(requireContext(), list)
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