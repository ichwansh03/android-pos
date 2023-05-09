package com.jrektor.skripsi

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.jrektor.skripsi.customer.CustomerFragment
import com.jrektor.skripsi.report.LaporanFragment
import com.jrektor.skripsi.outlet.OutletFragment
import com.jrektor.skripsi.product.items.ItemFragment
import com.jrektor.skripsi.product.items.AddProductActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var backPressedTime: Long = 0

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(this, "Tekan kembali untuk tutup", Toast.LENGTH_SHORT).show()
            backPressedTime = System.currentTimeMillis()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkState()

        btn_menu_nav.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
            drawer_layout.bringToFront()

            if (drawer_layout.isDrawerOpen(GravityCompat.START)){
                drawer_layout.closeDrawer(GravityCompat.START)
                drawer_layout.visibility = View.INVISIBLE
            }

            nav_view.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.kelola_produk -> {
                        val intent = Intent(this, AddProductActivity::class.java)
                        startActivity(intent)
                    }
                    else -> {
                        drawer_layout.closeDrawer(GravityCompat.START)
                        drawer_layout.visibility = View.INVISIBLE
                    }
                }
                true
            }
        }

        if (savedInstanceState == null){
            chipbar.setItemSelected(R.id.product, true)
            supportFragmentManager.beginTransaction().replace(R.id.container, ItemFragment()).commit()
        }

        chipbar.setOnItemSelectedListener { id ->
            var fragment: Fragment? = null
            when(id){
                R.id.product -> fragment = ItemFragment()
                R.id.report -> fragment = LaporanFragment()
                R.id.outlet -> fragment = OutletFragment()
                R.id.customer -> fragment = CustomerFragment()
            }

            if (fragment != null){
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
            }
        }

    }

    fun checkState(){
        val manager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetwork

        if (info == null){
            val noInternetDialog = NoInternetDialog()
            noInternetDialog.show(supportFragmentManager, "NoInternetDialog")
        }
    }
}