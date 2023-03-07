package com.jrektor.skripsi.outlet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jrektor.skripsi.R
import com.jrektor.skripsi.outlet.shop.ListOutletFragment

class OutletFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_outlet, container, false)

        addFragmentOutlet(ListOutletFragment())
        return view
    }

    private fun addFragmentOutlet(fragment: Fragment) {
        TODO("Not yet implemented")
    }

}