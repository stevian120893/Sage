package com.mib.lib_util

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

@SuppressLint("StaticFieldLeak")
object Utils {

    internal var context: Context? = null
        private set

    fun init(context: Context) {
        this.context = context
    }
}