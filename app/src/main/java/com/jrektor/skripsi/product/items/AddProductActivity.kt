package com.jrektor.skripsi.product.items

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.categories.ManageCategoryFragment
import kotlinx.android.synthetic.main.activity_add_product.*

class AddProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        addFragment(ManageItemFragment())

        tab_category.setOnClickListener { addFragment(ManageCategoryFragment()) }
        tab_product.setOnClickListener { addFragment(ManageItemFragment()) }
    }
    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_add_product, fragment)
            .commit()
    }
}