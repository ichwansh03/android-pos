package com.jrektor.skripsi

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.jrektor.skripsi.verification.LoginActivity
import com.jrektor.skripsi.verification.RegisterActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        loginbtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        registerbtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}