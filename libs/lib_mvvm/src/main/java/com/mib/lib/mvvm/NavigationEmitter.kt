package com.mib.lib.mvvm

import com.mib.lib_util.SingleLiveEvent

interface NavigationEmitter {
    val navigationEvent: SingleLiveEvent<Int>
}