package com.jrektor.skripsi.product.categories

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : Fragment() {

    var list = ArrayList<ModelCategory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_category)
        val emptyText = view.findViewById<TextView>(R.id.txempty_category)
        val fabCategory = view.findViewById<FloatingActionButton>(R.id.fab_category)
        fabCategory.visibility = View.GONE

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            handler.post {
                val category = CategoryFragment()
                category.getCategories(activity, emptyText, context, recyclerView, GlobalData.nameOutlet)
                pb_main_cat.visibility = View.GONE
            }
        },5000)
        return view
    }
}

//implementasi extention functions
fun CategoryFragment.getCategories(activity: FragmentActivity?, txempty: TextView, context: Context?, recyclerView: RecyclerView, outlet: String) {
    val queue: RequestQueue = Volley.newRequestQueue(activity)
    val request = JsonArrayRequest(
        Request.Method.GET, GlobalData.BASE_URL+"category/get_cat_app.php?in_outlet=$outlet",null,
        { response ->
            if (response.length() == 0){
                txempty.visibility = View.VISIBLE
            }
            else {
                txempty.visibility = View.GONE
                for (cat in 0 until response.length()){
                    val obj = response.getJSONObject(cat)
                    val id = obj.getInt("id")
                    val name = obj.getString("name")
                    val outlets = obj.getString("in_outlet")

                    //named parameter impl
                    list.add(ModelCategory(nameCategory = name, id = id, outlet = outlets))
                    val adapterCategory = context?.let { AdapterCategory(it, list) }
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = adapterCategory
                }
            }
        },
        { error ->
            Log.d("error", error.toString())
        })
    queue.add(request)
}