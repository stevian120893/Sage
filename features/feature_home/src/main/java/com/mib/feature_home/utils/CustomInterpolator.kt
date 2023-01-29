package com.mib.feature_home.utils

import android.view.animation.Interpolator
import androidx.core.view.animation.PathInterpolatorCompat

object CustomInterpolator {

    private val motionCurveIn get() = floatArrayOf(0.64f, 0.04f, 1f, 1f)
    private val motionCurveOut get() = floatArrayOf(0f, 0f, 0.35f, 1f)
    private val motionCurveInOut get() = floatArrayOf(
        motionCurveIn[0], motionCurveIn[1],
        motionCurveOut[2], motionCurveOut[3])

    private fun getPathInterpolator(curve: FloatArray): Interpolator =
        PathInterpolatorCompat.create(
            curve[0], curve[1],
            curve[2], curve[3])

    val motionIn: Interpolator
        get() = getPathInterpolator(motionCurveIn)

    val motionOut: Interpolator
        get() = getPathInterpolator(motionCurveOut)

    val motionInOut: Interpolator
        get() = getPathInterpolator(motionCurveInOut)
}
