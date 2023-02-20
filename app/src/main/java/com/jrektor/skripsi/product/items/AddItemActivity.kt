package com.jrektor.skripsi.product.items

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.activity_add_item.*
import org.json.JSONException
import org.json.JSONObject

class AddItemActivity : AppCompatActivity() {

    var listCategory: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        getListCategory()
        var category = spin_category.text

        var uploadProductUrl = "http://192.168.43.8/pos/addproduct.php/"
    }

    private fun getListCategory() {
        AndroidNetworking.get("http://192.168.43.8/pos/category/get_cat_app.php/")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        val jsonArray = response.getJSONArray("server_response")
                        for (i in 0 until jsonArray.length()){
                            val jsonObject = jsonArray.getJSONObject(i)
                            listCategory.add(jsonObject.getString("name"))

                            val arrayAdapter = ArrayAdapter(
                                this@AddItemActivity, android.R.layout.simple_list_item_1, listCategory)
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spin_category?.setAdapter(arrayAdapter)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this@AddItemActivity, "Gagal menampilkan data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(anError: ANError?) {
                    Toast.makeText(this@AddItemActivity, "Tidak ada jaringan internet", Toast.LENGTH_SHORT).show()
                }
            })
    }
}