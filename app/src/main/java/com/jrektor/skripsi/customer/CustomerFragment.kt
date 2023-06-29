package com.jrektor.skripsi.customer

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.jrektor.skripsi.R
import kotlinx.android.synthetic.main.fragment_customer.*

class CustomerFragment : Fragment() {

    val list = ArrayList<ItemCustomer>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_customer, container, false)

        addFragmentCustomer(TunaiFragment())
        val tunai = view.findViewById<CardView>(R.id.tab_tunai)
        val transfer = view.findViewById<CardView>(R.id.tab_cashless)
        tunai.setOnClickListener {
            tunai.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
            tx_cv_tunai.setTextColor(Color.WHITE)
            addFragmentCustomer(TunaiFragment())
            transfer.setCardBackgroundColor(Color.WHITE)
            tx_cv_cashless.setTextColor(Color.BLACK)
        }

        transfer.setOnClickListener {
            transfer.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
            tx_cv_cashless.setTextColor(Color.WHITE)
            addFragmentCustomer(CashlessFragment())
            tunai.setCardBackgroundColor(Color.WHITE)
            tx_cv_tunai.setTextColor(Color.BLACK)
        }

        return view
    }

    private fun addFragmentCustomer(fragment: Fragment) {
        parentFragmentManager.beginTransaction().replace(R.id.customer_container, fragment).commit()
    }


}