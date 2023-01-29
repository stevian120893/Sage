package com.mib.feature_home.utils

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.mib.feature_home.R
import kotlinx.coroutines.launch

class LoadingDialogFragment : DialogFragment() {

    private val fadeDuration = 300L
    private val interval = 1_000L

    private var imageIndex = 0
    private val images by lazy {
        listOf(
            VectorDrawableCompat.create(resources, R.drawable.ic_loading, null),
//            VectorDrawableCompat.create(resources, R.drawable.ic_loading_wink, null),
//            VectorDrawableCompat.create(resources, R.drawable.ic_loading_normal, null),
//            VectorDrawableCompat.create(resources, R.drawable.ic_loading_wink, null),
//            VectorDrawableCompat.create(resources, R.drawable.ic_loading_normal, null)
        )
    }

    private var linearLayout: LinearLayout? = null
    private var imageView: ImageView? = null

    init {
        isCancelable = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.BlockingDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        linearLayout = LinearLayout(requireContext()).also {
            it.orientation = LinearLayout.VERTICAL
            val padding = 16
            it.setPadding(padding, padding, padding, padding)
        }
        return linearLayout!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            imageView = AppCompatImageView(view.context).also {
                it.setImageDrawable(images[0])
                it.scaleType = ImageView.ScaleType.CENTER_INSIDE
            }
            val textView = TextView(view.context).also {
                it.typeface = Typeface.DEFAULT
                it.setText(R.string.shared_res_loading)
            }
            linearLayout?.let {
                val dp36 = 36
                it.addView(
                    imageView,
                    LayoutParams(dp36, dp36).also { param ->
                        param.gravity = Gravity.CENTER_HORIZONTAL
                    }
                )
                it.addView(
                    textView,
                    LayoutParams(WRAP_CONTENT, WRAP_CONTENT).also { param ->
                        param.topMargin = 8
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        linearLayout = null
        imageView = null
    }

    override fun onResume() {
        super.onResume()
        startAnimation()
    }

    override fun onPause() {
        super.onPause()
        stopAnimation()
    }

    private fun startAnimation() {
        imageView?.let { imageView ->
            val animationSet = getAnimation().also {
                it.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {}

                    override fun onAnimationEnd(p0: Animation?) {
                        stopAnimation()
                        if (imageView.isAttachedToWindow) {
                            imageIndex++
                            imageView.setImageDrawable(images[imageIndex % 1])
                            startAnimation()
                        }
                    }

                    override fun onAnimationRepeat(p0: Animation?) {}
                })
            }
            imageView.animation = animationSet
        }
    }

    private fun stopAnimation() = imageView?.let {
        it.animation?.setAnimationListener(null)
        it.clearAnimation()
    }

    private fun getAnimation(): AnimationSet {
        val animationFadeIn = AlphaAnimation(0f, 1f).apply {
            interpolator = CustomInterpolator.motionInOut
            duration = fadeDuration
        }

        val animationFadeOut = AlphaAnimation(1f, 0f).apply {
            interpolator = CustomInterpolator.motionInOut
            startOffset = (fadeDuration + interval)
            duration = fadeDuration
        }
        return AnimationSet(false).apply {
            addAnimation(animationFadeIn)
            addAnimation(animationFadeOut)
            repeatMode = Animation.RESTART
            repeatCount = Animation.INFINITE
        }
    }
}