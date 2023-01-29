package com.mib.lib.mvvm

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mib.lib_util.SingleLiveEvent
import pl.aprilapps.easyphotopicker.EasyImage

abstract class BaseViewModel<ViewState : BaseViewState>(
    initialState: ViewState
) : ViewModel(), ToastEmitter, NavigationEmitter, MediaEmitter {

    private val _stateLiveData = MutableLiveData(initialState)
    val stateLiveData get() = _stateLiveData

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    override val navigationEvent: SingleLiveEvent<Int> = SingleLiveEvent()

    override val mediaEvent: SingleLiveEvent<Pair<Fragment, EasyImage>> = SingleLiveEvent()

    protected var state: ViewState = initialState
        set(value) {
            field = value
            _stateLiveData.postValue(value)
        }
}