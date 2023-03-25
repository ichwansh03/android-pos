package com.jrektor.skripsi.product.cart

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(activity: Activity) {

    val mypref = "main_preferences"
    val sharedPrefs: SharedPreferences

    init {
        sharedPrefs = activity.getSharedPreferences(mypref, Context.MODE_PRIVATE)
    }
}