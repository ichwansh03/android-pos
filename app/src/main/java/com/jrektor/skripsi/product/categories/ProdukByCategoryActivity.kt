package com.jrektor.skripsi.product.categories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.items.AdapterItem
import com.jrektor.skripsi.product.items.ModelProduct
import kotlinx.android.synthetic.main.fragment_item.*

class ProdukByCategoryActivity : AppCompatActivity() {

    var list = ArrayList<ModelProduct>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_item)

        cv_search_item.visibility = View.GONE

        val category = GlobalData.nameCategory
        getItemByCategories(category)
    }

    private fun getItemByCategories(category: String) {
        val queue: RequestQueue = Volley.newRequestQueue(this@ProdukByCategoryActivity)
        val request = JsonArrayRequest(
            Request.Method.GET, GlobalData.BASE_URL+"product/apiproductbycat.php?cat_product=$category",null,
            { response ->
                for (cat in 0 until response.length()){
                    val jObject = response.getJSONObject(cat)
                    val id = jObject.getInt("id")
                    val name = jObject.getString("name")
                    val price = jObject.getInt("price")
                    val image = jObject.getString("image").replace("localhost","192.168.110.68")
                    val stock = jObject.getInt("stock")
                    val merk = jObject.getString("merk")
                    val desc = jObject.getString("description")
                    val catProduct = jObject.getString("cat_product")

                    list.add(ModelProduct(id, name, price, merk, stock, catProduct, image, desc, 1, false))
                    val adapter = AdapterItem(this, list)
                    rv_product.layoutManager = GridLayoutManager(this,2)
                    rv_product.adapter = adapter
                }
            },
            { error ->
                Log.d("error", error.toString())
            })
        queue.add(request)
    }
}