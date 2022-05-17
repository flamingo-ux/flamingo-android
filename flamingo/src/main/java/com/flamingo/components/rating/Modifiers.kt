@file:Suppress("NOTHING_TO_INLINE", "LongParameterList")

package com.flamingo.components.rating

import android.content.Context
import android.os.Build
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.VibrationEffect.createOneShot
import android.os.Vibrator
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp

internal inline fun Modifier.ratingDrag(
    noinline onSelected: ((UInt) -> Unit)?,
    stepDragIndex: MutableState<UInt>,
    dragEnabled: Boolean,
    spaceBetweenIcons: Dp,
    totalWidth: Dp,
    wholeSteps: UInt,
    halves: Boolean,
    vibrationEnabled: Boolean,
    context: Context,
): Modifier = pointerInput(
    onSelected,
    dragEnabled,
    spaceBetweenIcons,
    totalWidth,
    wholeSteps,
    halves,
    vibrationEnabled,
    context,
) {
    @Suppress("NAME_SHADOWING")
    var stepDragIndex by stepDragIndex
    detectHorizontalDragGestures(
        onDragEnd = {
            onSelected?.invoke(stepDragIndex)
            stepDragIndex = 0u
            println("BBBBB: resultFromDrag")
        }
    ) { change, _ ->
        val percent = calculatePositionPercent(
            spaceBetweenIcons.toPx(),
            totalWidth.toPx(),
            positionPx = change.position.x
        )

        println("BBBBB: halves            = $halves")
        println("BBBBB: percentFromDrag   = $percent")
        val stepIndex = positionPercentToStepIndex(percent, wholeSteps, halves)
        println("BBBBB: stepIndexFromDrag = $stepIndex")
        if (vibrationEnabled && stepDragIndex != stepIndex) context.vibrateTick(
            weak = halves && stepIndex % 2u != 0u
        )
        stepDragIndex = stepIndex
    }
}

internal inline fun Modifier.ratingTap(
    noinline onSelected: ((UInt) -> Unit)?,
    spaceBetweenIcons: Dp,
    totalWidth: Dp,
    wholeSteps: UInt,
    halves: Boolean,
    vibrationEnabled: Boolean,
    context: Context,
): Modifier = pointerInput(
    onSelected,
    spaceBetweenIcons,
    totalWidth,
    wholeSteps,
    halves,
    vibrationEnabled,
    context,
) {
    detectTapGestures {
        val percent = calculatePositionPercent(
            spaceBetweenIcons.toPx(),
            totalWidth.toPx(),
            positionPx = it.x
        )
        val stepIndex = positionPercentToStepIndex(percent, wholeSteps, halves)
        if (vibrationEnabled) context.vibrateTick()
        onSelected?.invoke(stepIndex)
        println("BBBBB: resultFromTap")
    }
}

private fun calculatePositionPercent(
    spaceBetweenIconsPx: Float,
    totalWidthPx: Float,
    positionPx: Float,
): Float {
    // this padding does not actually exist, is just added to more easily calculate step index
    val startPaddingPx = spaceBetweenIconsPx / 2
    return ((positionPx + startPaddingPx) / (totalWidthPx + startPaddingPx)).coerceIn(0f, 1f)
}

private fun positionPercentToStepIndex(
    percent: Float,
    wholeSteps: UInt,
    halves: Boolean,
): UInt {
    require(percent in 0f..1f) { "percent = $percent, should be in [0..1]" }
    val steps = (if (halves) 2u * wholeSteps else wholeSteps).toFloat()
    return (percent / (1f / steps) + 1).toUInt().coerceIn(0u, steps.toUInt())
}

@Suppress("DEPRECATION", "MissingPermission", "MagicNumber")
private fun Context.vibrateTick(weak: Boolean = false) {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (!vibrator.hasVibrator()) return
    val durationMs = if (weak) 25L else 50L
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(createOneShot(durationMs, DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(durationMs)
    }
}
