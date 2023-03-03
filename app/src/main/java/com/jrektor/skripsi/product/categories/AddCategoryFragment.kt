package com.jrektor.skripsi.product.categories

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
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.fragment_add_category.*

class AddCategoryFragment : Fragment() {

    var list = ArrayList<ItemCategory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_category, container, false)

        getCategories()

        val dialogAddCategory = DialogAddCategory()

        fab_category.setOnClickListener{
            dialogAddCategory.show(childFragmentManager,"dialogAddCategory")
        }
        return view
    }

    public fun getCategories() {
        val queue: RequestQueue = Volley.newRequestQueue(activity)
        val request = JsonArrayRequest(Request.Method.GET, "http://192.168.43.8/pos/apicategory.php",null,
            { response ->
                for (cat in 0 until response.length()){
                    val obj = response.getJSONObject(cat)
                    val id = obj.getInt("id")
                    val name = obj.getString("name")

                    list.add(ItemCategory(id, name))
                    val adapterCategory = AdapterCategory(requireContext(), list)
                    rv_category.layoutManager = LinearLayoutManager(requireContext())
                    rv_category.adapter = adapterCategory
                }
            },
            { error ->
                Log.d("error", error.toString())
            })
        queue.add(request)
    }

}