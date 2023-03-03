package com.jrektor.skripsi.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.categories.AddCategoryFragment
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        val addCategoryFragment = AddCategoryFragment()

        addCategoryFragment.getCategories()

        btn_search_product.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProdukFragment())
                .commit()
        }

        return view
    }

}