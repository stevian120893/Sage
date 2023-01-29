package com.mib.lib_pref.init

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object LibPref {

    internal lateinit var context: Context
        private set

    fun init(context: Context) {
        LibPref.context = context
    }
}