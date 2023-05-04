package com.jrektor.skripsi.outlet

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.jrektor.skripsi.R
import com.jrektor.skripsi.outlet.employee.ListEmployeeFragment
import com.jrektor.skripsi.outlet.shop.ListOutletFragment
import kotlinx.android.synthetic.main.fragment_outlet.*

class OutletFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_outlet, container, false)

        addFragmentOutlet(ListOutletFragment())

        val market = view.findViewById<CardView>(R.id.tab_market)
        val employee = view.findViewById<CardView>(R.id.tab_employee)
        market.setOnClickListener {
            market.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
            txmarket.setTextColor(Color.WHITE)
            addFragmentOutlet(ListOutletFragment())
            employee.setCardBackgroundColor(Color.WHITE)
            txemployee.setTextColor(Color.BLACK)
        }

        employee.setOnClickListener {
            employee.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
            txemployee.setTextColor(Color.WHITE)
            addFragmentOutlet(ListEmployeeFragment())
            market.setCardBackgroundColor(Color.WHITE)
            txmarket.setTextColor(Color.BLACK)
        }
        return view
    }

    private fun addFragmentOutlet(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.outlet_container, fragment)
            .commit()
    }

}