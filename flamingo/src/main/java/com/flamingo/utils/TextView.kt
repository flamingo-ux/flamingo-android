package com.flamingo.utils

import android.graphics.Paint.FontMetricsInt
import android.widget.TextView
import kotlin.math.abs

/**
 * Compat version of [TextView.getFirstBaselineToTopHeight] and
 * [TextView.setFirstBaselineToTopHeight]
 */
public var TextView.firstBaselineToTopHeightCompat: Int
    get() = paddingTop - paint.fontMetricsInt.top
    set(value) {
        require(value >= 0)
        val fontMetrics: FontMetricsInt = paint.fontMetricsInt
        val fontMetricsTop: Int = if (includeFontPadding) fontMetrics.top else fontMetrics.ascent
        if (value > abs(fontMetricsTop)) {
            val paddingTop = value - -fontMetricsTop
            setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        }
    }

/**
 * Compat version of [TextView.getLastBaselineToBottomHeight] and
 * [TextView.setLastBaselineToBottomHeight]
 */
public var TextView.lastBaselineToBottomHeightCompat: Int
    get() = paddingTop - paint.fontMetricsInt.top
    set(value) {
        require(value >= 0)
        val fontMetricsBottom: Int = with(paint.fontMetricsInt) {
            if (includeFontPadding) bottom else descent
        }

        if (value > abs(fontMetricsBottom)) {
            val paddingBottom = value - fontMetricsBottom
            setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        }
    }
