package com.jrektor.skripsi.product.items

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.jrektor.skripsi.R
import com.jrektor.skripsi.product.categories.ManageCategoryFragment
import kotlinx.android.synthetic.main.activity_add_product.*

class AddProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        addFragment(ManageItemFragment())

        tab_category.setOnClickListener {
            tab_category.setCardBackgroundColor(ContextCompat.getColor(this, R.color.primary))
            tx_cv_category.setTextColor(Color.WHITE)
            addFragment(ManageCategoryFragment())
            tab_product.setCardBackgroundColor(Color.WHITE)
            tx_cv_product.setTextColor(Color.BLACK)
        }
        tab_product.setOnClickListener {
            tab_product.setCardBackgroundColor(ContextCompat.getColor(this, R.color.primary))
            tx_cv_product.setTextColor(Color.WHITE)
            addFragment(ManageItemFragment())
            tab_category.setCardBackgroundColor(Color.WHITE)
            tx_cv_category.setTextColor(Color.BLACK)
        }
    }
    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_add_product, fragment)
            .commit()
    }
}