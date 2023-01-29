package com.mib.lib.mvvm

import androidx.fragment.app.Fragment
import com.mib.lib_util.SingleLiveEvent
import pl.aprilapps.easyphotopicker.EasyImage

interface MediaEmitter {
    val mediaEvent: SingleLiveEvent<Pair<Fragment, EasyImage>>
}