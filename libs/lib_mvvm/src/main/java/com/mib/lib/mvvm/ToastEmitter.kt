package com.mib.lib.mvvm

import com.mib.lib_util.SingleLiveEvent

interface ToastEmitter {
    val toastEvent: SingleLiveEvent<String>
}