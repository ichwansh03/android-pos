package com.jrektor.skripsi.product.categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.dialog_add_category.*

class DialogAddCategory : DialogFragment() {

    private val addCategoryUrl = "http://192.168.43.8/pos/category/create_cat_app.php"
    private val updateCategoryUrl = "http://192.168.43.8/pos/category/update_cat_app.php"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.dialog_add_category, container, false)

        btn_save_category.setOnClickListener {
            addCategory()
        }

        return view
    }

    private fun insertOrUpdateData() {

    }

    private fun addCategory() {
        if (txcategory.text.toString().isEmpty()){
            Toast.makeText(context, "Isi data terlebih dahulu", Toast.LENGTH_SHORT).show()
        } else {
            val request: RequestQueue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(Request.Method.GET, addCategoryUrl+"?name="+txcategory.text.toString(),
                { response ->
                    if (response.equals("OK")){
                        Toast.makeText(context, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    }
                }, {
                        error ->
                    Log.d("Error", error.toString())
                })
            request.add(stringRequest)
        }
    }

}