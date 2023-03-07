package com.jrektor.skripsi.product.items

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.fragment_manage_item.*

class ManageItemFragment : Fragment() {

    var list = ArrayList<ModelProduct>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_manage_item, container, false)
        getProduct()

        val btnAddItem = view.findViewById<FloatingActionButton>(R.id.fab_add_item)
        btnAddItem.setOnClickListener {
            startActivity(Intent(activity, FormAddProdukActivity::class.java))
        }
        return view
    }

    private fun getProduct() {
        val queue: RequestQueue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(
            Request.Method.GET, "http://192.168.43.8/pos/product/apiproduct.php", null,
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

                    list.add(ModelProduct(id, name, price, merk, stock, image, desc))
                    val adapter = AdapterManageItem(requireContext(), list)
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