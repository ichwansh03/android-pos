package com.jrektor.skripsi.product.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.dialog_add_category.*

class DialogAddCategory : DialogFragment() {

    private val addCategoryUrl = "http://192.168.43.8/pos/category/create_cat_app.php"
    private val updateCategoryUrl = "http://192.168.43.8/pos/category/update_cat_app.php"
    private val deleteCategoryUrl = "http://192.168.43.8/pos/category/delete_cat_app.php"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_add_category, container, false)


        btn_save_category.setOnClickListener {
            insertOrUpdateData()
        }

        btn_del_category.setOnClickListener {
            deleteCategory()
        }

        return view
    }

    private fun deleteCategory() {
        val request: RequestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(Method.POST, deleteCategoryUrl,
            Response.Listener { response ->
                if (response == "OK") {
                    Toast.makeText(context, "Berhasil dihapus", Toast.LENGTH_SHORT).show()
                    dismiss() // menutup dialog setelah data dihapus
                }
            },
            Response.ErrorListener { error ->
                Log.d("Error", error.toString())
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id"] = GlobalData.idCategory.toString() // categoryId adalah id dari data yang akan dihapus
                return params
            }
        }
        request.add(stringRequest)

    }

    private fun insertOrUpdateData() {
        if (GlobalData.idCategory == 0){
            addCategory()
        } else {
            updateCategory()
        }
    }

    private fun updateCategory() {
        val request: RequestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(Method.POST, updateCategoryUrl,
            Response.Listener { response ->
                if (response == "OK") {
                    Toast.makeText(context, "Berhasil diupdate", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.d("Error", error.toString())
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["name"] = txcategory.text.toString()
                params["id"] = GlobalData.idCategory.toString()
                return params
            }
        }
        request.add(stringRequest)
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