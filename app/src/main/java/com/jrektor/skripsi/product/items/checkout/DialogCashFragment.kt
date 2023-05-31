package com.jrektor.skripsi.product.items.checkout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import com.jrektor.skripsi.R

class DialogCashFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dialog_cash, container, false)
        //NPE
        val printStruk = view.findViewById<CardView>(R.id.print_struk)
        printStruk.setOnClickListener { startActivity(Intent(requireContext(), PrintReceiptActivity::class.java)) }

        return view
    }

}