package com.jrektor.skripsi.product.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.dialog_manage_category.*
import kotlinx.android.synthetic.main.item_category.*

class DialogManageCategory : DialogFragment() {

    private val addCategoryUrl = GlobalData.BASE_URL+"category/create_cat_app.php"
    private val updateCategoryUrl = GlobalData.BASE_URL+"category/update_cat_app.php"
    private val deleteCategoryUrl = GlobalData.BASE_URL+"category/delete_cat_app.php?id={${GlobalData.idCategory}}"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_manage_category, container, false)

        val save = view.findViewById<CardView>(R.id.btn_save_category)
        save.setOnClickListener {
            addCategory()
        }

        val update = view.findViewById<CardView>(R.id.btn_update_category)
        update.setOnClickListener {
            updateCategory()
            Toast.makeText(context,"Kategori Berhasil Diubah", Toast.LENGTH_SHORT).show()
        }

        val delete = view.findViewById<CardView>(R.id.btn_del_category)
        delete.setOnClickListener {
            deleteCategory()
            Toast.makeText(context,"Kategori Berhasil Dihapus", Toast.LENGTH_SHORT).show()
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

    private fun updateCategory() {
        val request: RequestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(Method.POST, updateCategoryUrl,
            Response.Listener { response ->
                if (response == "OK") {
                    Toast.makeText(context, "Berhasil diupdate", Toast.LENGTH_SHORT).show()
                    dismiss()
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
            val queue: RequestQueue = Volley.newRequestQueue(context)
            val request = object : StringRequest(Method.POST, addCategoryUrl, Response.Listener { _ ->
                Toast.makeText(context, "Kategori berhasil disimpan", Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            },
            Response.ErrorListener {
                error ->
                Log.d("error ",error.message.toString())
            }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["name"] = txcategory.text.toString()
                    params["in_outlet"] = GlobalData.nameOutlet
                    return params
                }
            }
                 queue.add(request)
        }
    }

}