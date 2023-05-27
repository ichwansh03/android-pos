package com.jrektor.skripsi.product.items

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.verification.LoginActivity
import com.jrektor.skripsi.verification.RegisterActivity
import kotlinx.android.synthetic.main.fragment_manage_item.*

class ManageItemFragment : Fragment() {
    companion object {
        var list = ArrayList<ModelProduct>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_manage_item, container, false)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            handler.post {
                getProduct(GlobalData.nameOutlet) {
                    pb_add_item.visibility = View.GONE
                    val adapter = AdapterManageItem(requireContext(), list)
                    rv_add_item.layoutManager = LinearLayoutManager(requireContext())
                    rv_add_item.adapter = adapter
                }
            }
        }, 5000)

        val btnAddItem = view.findViewById<FloatingActionButton>(R.id.fab_add_item)
        btnAddItem.setOnClickListener {
            startActivity(Intent(activity, FormAddProductActivity::class.java))
        }

        return view
    }

    //delete callback()
    private fun getProduct(outlet: String, callback: () -> Unit) {
        val queue: RequestQueue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(
            Request.Method.GET, GlobalData.BASE_URL + "product/apiproductbyoutlet.php?in_outlet=$outlet", null,
            { response ->
                if (response.length() == 0) {
                    txempty_manage_product.visibility = View.VISIBLE
                } else {
                    txempty_manage_product.visibility = View.GONE
                    for (s in 0 until response.length()) {
                        val jObject = response.getJSONObject(s)
                        val id = jObject.getInt("id")
                        val name = jObject.getString("name")
                        val price = jObject.getInt("price")
                        val image = jObject.getString("image")
                            .replace("http://localhost/pos/", GlobalData.BASE_URL)
                        val stock = jObject.getInt("stock")
                        val merk = jObject.getString("merk")
                        val catProduct = jObject.getString("cat_product")
                        val desc = jObject.getString("description")
                        val outlets = jObject.getString("in_outlet")
                        list.add(
                            ModelProduct(
                                id,
                                name,
                                price,
                                merk,
                                stock,
                                catProduct,
                                image,
                                desc,
                                1,
                                outlets
                            )
                        )
                    }
                    callback()
                }

            },
            { error ->
                Log.d("Error", error.toString())
            })
        queue.add(request)
    }

}