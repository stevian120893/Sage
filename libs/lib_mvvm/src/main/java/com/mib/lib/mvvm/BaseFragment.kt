package com.mib.lib.mvvm

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mib.lib.mvvm.utils.DialogUtils
import com.mib.lib.mvvm.utils.openCamera
import com.mib.lib.mvvm.utils.openGallery
import com.mib.lib_navigation.DialogListener
import pl.aprilapps.easyphotopicker.EasyImage

/**
 * Somehow error when unit test if we call Fragment(contentLayoutId)
 */
abstract class BaseFragment<T : ViewModel>(@LayoutRes protected val contentLayoutId: Int) : Fragment() {

    /**
     * Toolbar title
     */
    @get:StringRes
    open val title: Int = 0

    open var toolbarBackIcon: Drawable? = null

    open val screenName: String = ""

    lateinit var viewModel: T

    // use it when you need to access ViewModel in variable getter
    val safeViewModel: T? get() {
        return if (::viewModel.isInitialized) viewModel else null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel(firstInit = savedInstanceState == null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (contentLayoutId != 0) inflater.inflate(contentLayoutId, container, false)
        else super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (::viewModel.isInitialized) {
            (viewModel as? ToastEmitter)?.let {
                it.toastEvent.observe(viewLifecycleOwner) { event ->
                    showToast(event)
                }
            }
            (viewModel as? MediaEmitter)?.let {
                it.mediaEvent.observe(viewLifecycleOwner) { event ->
                    showUploadOptionDialog(event.first, event.second)
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (title != 0 || toolbarBackIcon != null) {
            (activity as? AppCompatActivity)?.let {
                it.supportActionBar?.title = it.getString(this.title)
                it.supportActionBar?.setHomeAsUpIndicator(toolbarBackIcon)
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }

    fun setViewModel(klaz: Class<T>) {
        viewModel = ViewModelProvider(this).get(klaz)
    }

    /**
     * Use this if you want to modify the arguments and make it survive onDestroy by using [SavedStateHandle] in
     * your [ViewModel]
     */
    fun setViewModelAndTransferArguments(klaz: Class<T>, firstInit: Boolean, body: (args: Bundle) -> Unit) {
        setViewModel(klaz)
        val args = arguments
        if (firstInit && args != null) {
            body(args)
        }
        arguments = null
    }

    open fun initViewModel(firstInit: Boolean) {}

    fun showToast(message: String) {
        if (message.isBlank()) return
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun showUploadOptionDialog(fragment: Fragment, easyImage: EasyImage) {
        DialogUtils.showDialogWithTwoButtons(
            fragment.context,
            fragment.context?.getString(R.string.upload_image).orEmpty(),
            fragment.context?.getString(R.string.upload_image_via).orEmpty(),
            fragment.context?.getString(R.string.galery).orEmpty(),
            fragment.context?.getString(R.string.camera).orEmpty(),
            object : DialogListener {
                override fun onLeftButtonClicked() {
                    fragment.openGallery(easyImage)
                }
                override fun onRightButtonClicked() {
                    fragment.openCamera(easyImage)
                }
            }
        )
    }

    companion object {
        internal const val DIALOG_ID = "id"
    }
}