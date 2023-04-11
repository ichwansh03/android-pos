package com.jrektor.skripsi.product.categories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.item_category.view.*

class AdapterManageCategory(var context: Context, var catList: ArrayList<ModelCategory>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class myAdapterCategory(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun adapter(names: String) {
            itemView.name_category.text = names
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)

        return myAdapterCategory(view)
    }

    override fun getItemCount(): Int {
        return catList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as myAdapterCategory).adapter(catList[position].nameCategory)
        (holder).itemView.cv_category_list.setOnClickListener {
            var dialog = DialogManageCategory()
            GlobalData.idCategory = catList[position].id
            GlobalData.nameCategory = catList[position].nameCategory
            //AddProductActivity cannot be cast to androidx.fragment.app.DialogFragment
            dialog.show((context as DialogFragment).parentFragmentManager, "DialogManageFragment")
        }
    }
}