@file:Suppress("NOTHING_TO_INLINE")

package com.flamingo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import kotlin.math.roundToInt

/**
 * Example usages:
 * ```
 * val pixels: Int = 16.dp                // from dp to pixels
 * val pixels: Int = 16.dpNoRounding      // from dp to pixels, no rounding to Int
 * val pixels: Float = 16f.dp             // from dp to pixels
 * val pixels: Int = 16f.dp.roundToInt()  // (or toInt()) from dp to pixels
 *
 * val pixels: Int = 16.sp                // from sp to pixels
 * val pixels: Int = 16.spNoRounding      // from sp to pixels, no rounding to Int
 * val pixels: Float = 16f.sp             // from sp to pixels
 * val pixels: Int = 16f.sp.roundToInt()  // (or toInt()) from sp to pixels
 *
 * val dps: Int = 82.pxToDp()  //  from pixels to dp's
 * ```
 */
@SuppressLint("StaticFieldLeak")
public object UnitConversions {

    private lateinit var context: Context

    public fun init(ctx: Context) {
        require(!UnitConversions::context.isInitialized) { "Init was already called." }
        context = ctx
    }

    /** Converts dp's to pixels and rounds to the nearest Int [roundToInt] */
    public val Int.dp: Int get() = toFloat().dpToPx().roundToInt()

    /** Converts dp's to pixels and drops the fraction */
    public val Int.dpNoRounding: Int get() = toFloat().dpToPx().toInt()

    /** Converts dp's to pixels */
    public val Float.dp: Float get() = dpToPx()

    /** Converts sp's to pixels and rounds to the nearest Int [roundToInt] */
    public val Int.sp: Int get() = toFloat().spToPx().roundToInt()

    /** Converts sp's to pixels and drops the fraction */
    public val Int.spNoRounding: Int get() = toFloat().spToPx().toInt()

    /** Converts sp's to pixels */
    public val Float.sp: Float get() = spToPx()

    /** Converts pixels to dp's */
    public fun Int.pxToDp(round: Boolean = true): Int {
        return (this / context.resources.displayMetrics.density)
            .run { if (round) roundToInt() else toInt() }
    }

    private inline fun Float.dpToPx(): Float {
        if (this == 0f) return 0f
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            context.resources.displayMetrics
        )
    }

    private inline fun Float.spToPx(): Float {
        if (this == 0f) return 0f
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            context.resources.displayMetrics
        )
    }
}
