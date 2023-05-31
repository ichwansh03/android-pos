package com.jrektor.skripsi.product.categories

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.verification.LoginActivity
import kotlinx.android.synthetic.main.fragment_category.*

class ManageCategoryFragment : Fragment() {

    var list = ArrayList<ModelCategory>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        val cardView = view.findViewById<CardView>(R.id.cv_search_cat)
        cardView.visibility = View.GONE

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            handler.post {
                getCategories(LoginActivity.OutletData.namaOutlet)
                pb_main_cat.visibility = View.GONE
            }
        },3000)


        val dialogAddCategory = DialogManageCategory()

        val btnAddCat = view.findViewById<FloatingActionButton>(R.id.fab_category)

        btnAddCat.setOnClickListener{
            dialogAddCategory.show(childFragmentManager,"dialogAddCategory")
        }
        return view
    }

    private fun getCategories(outlet: String) {
        val activity = activity // Simpan reference activity dalam variabel lokal

        if (activity != null) {
            val queue: RequestQueue = Volley.newRequestQueue(activity.applicationContext)
            val request = JsonArrayRequest(Request.Method.GET, GlobalData.BASE_URL+"category/get_cat_app.php?in_outlet=$outlet", null,
                { response ->
                    if (response.length() == 0) {
                        txempty_category.visibility = View.VISIBLE
                    } else {
                        txempty_category.visibility = View.GONE
                        for (cat in 0 until response.length()) {
                            val obj = response.getJSONObject(cat)
                            val id = obj.getInt("id")
                            val name = obj.getString("name")
                            val outles = obj.getString("in_outlet")

                            list.add(ModelCategory(nameCategory = name, id = id, outlet = outles))
                            val adapterCategory = AdapterManageCategory(requireContext(), list)
                            rv_category.layoutManager = LinearLayoutManager(requireContext())
                            rv_category.adapter = adapterCategory
                        }
                    }
                },
                { error ->
                    Log.d("error", error.toString())
                })
            queue.add(request)
        } else {
            Toast.makeText(requireContext(), "Data masih dalam proses", Toast.LENGTH_SHORT).show()
        }
    }

}