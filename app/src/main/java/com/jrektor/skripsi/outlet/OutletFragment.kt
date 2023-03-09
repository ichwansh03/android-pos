package com.jrektor.skripsi.outlet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jrektor.skripsi.R
import com.jrektor.skripsi.outlet.employee.ListEmployeeFragment
import com.jrektor.skripsi.outlet.shop.ListOutletFragment
import kotlinx.android.synthetic.main.fragment_outlet.*

class OutletFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_outlet, container, false)

        addFragmentOutlet(ListOutletFragment())

        tab_market.setOnClickListener { addFragmentOutlet(ListOutletFragment()) }
        tab_employee.setOnClickListener { addFragmentOutlet(ListEmployeeFragment()) }
        return view
    }

    private fun addFragmentOutlet(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.outlet_container, fragment)
            .commit()
    }

}