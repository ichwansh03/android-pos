package com.jrektor.skripsi.outlet.shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.jrektor.skripsi.GlobalData
import com.jrektor.skripsi.R
import com.jrektor.skripsi.outlet.employee.ItemPegawai
import com.jrektor.skripsi.outlet.employee.ListEmployeeFragment
import kotlinx.android.synthetic.main.activity_detail_outlet.*

class DetailOutletActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_outlet)

        Glide.with(this@DetailOutletActivity).load(GlobalData.imageOutlet).into(img_detail_outlet)
        name_detail_outlet.text = GlobalData.nameOutlet
        place_detail_outlet.text = GlobalData.addressOutlet

        val listEmployeeFragment = ListEmployeeFragment()
        listEmployeeFragment.list
        listEmployeeFragment.getEmployee()

        fab_edit_outlet.setOnClickListener {
            startActivity(Intent(this, EditOutletActivity::class.java))
        }
    }

}