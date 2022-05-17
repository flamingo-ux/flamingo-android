package com.flamingo.utils

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

@Suppress("MagicNumber")
public fun generateProgressDrawable(
    context: Context,
    progressColor: Int,
    progressRadiusPx: Float,
    progressStrokePx: Float,
): CircularProgressDrawable = CircularProgressDrawable(context).apply {
    setStyle(CircularProgressDrawable.DEFAULT)
    setColorSchemeColors(progressColor)
    centerRadius = progressRadiusPx
    strokeWidth = progressStrokePx
    val size = (centerRadius + strokeWidth).toInt() * 2
    setBounds(0, -3, size, size)
}
