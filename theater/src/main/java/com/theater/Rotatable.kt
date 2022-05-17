@file:Suppress("MagicNumber", "MatchingDeclarationName")

package com.theater

import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize

@Composable
internal fun rememberRotatableState(
    maxAngle: Float = 70f,
    authority: Int = 1,
    angleX: MutableState<Float> = mutableStateOf(0f),
    angleY: MutableState<Float> = mutableStateOf(0f),
): RotatableState = remember { RotatableState(maxAngle, authority, angleX, angleY) }

internal class RotatableState(
    internal val maxAngle: Float,
    internal val authority: Int,
    val angleX: MutableState<Float>,
    val angleY: MutableState<Float>,
) {
    internal var start by mutableStateOf(Offset(-1f, -1f))
    internal var viewSize by mutableStateOf(IntSize.Zero)
}

/**
 * Drag gestures on the content translate to the rotation angles that can be applied to anything to
 * rotate it.
 *
 * You must read rotation angles from [RotatableState.angleX] and [RotatableState.angleY] and apply
 * them to whatever you want to rotate using [graphicsLayer].
 *
 * __DO NOT__ apply them to this content!
 *
 * Example:
 * ```
 * graphicsLayer {
 *      rotationX = rotatableState.angleX
 *      rotationY = rotatableState.angleY
 *  }
 * ```
 */
@OptIn(ExperimentalComposeUiApi::class)
internal fun Modifier.rotatable(state: RotatableState): Modifier = with(state) {
    this@rotatable
        .onSizeChanged { size -> viewSize = size }
        .pointerInteropFilter { m ->
            when (m.action) {
                MotionEvent.ACTION_UP -> {
                    start = Offset(-1f, -1f)
                }
                MotionEvent.ACTION_DOWN -> {
                    start = Offset(m.rawX, m.rawY)
                }
                MotionEvent.ACTION_MOVE -> @Suppress("UNUSED_VALUE") {
                    if (viewSize == IntSize.Zero) return@pointerInteropFilter true
                    var angleX by angleX
                    var angleY by angleY
                    val end = Offset(m.rawX, m.rawY)
                    val newAngle = getRotationAngles(start, end, viewSize, maxAngle, authority)
                    var x: Float = -angleY + newAngle.first
                    var y: Float = angleX + newAngle.second

                    if (x > maxAngle) x = maxAngle
                    else if (x < -maxAngle) x = -maxAngle

                    if (y > maxAngle) y = maxAngle
                    else if (y < -maxAngle) y = -maxAngle

                    angleX = y
                    angleY = -x
                    start = end
                }
            }
            true
        }
}

/**
 * This method converts the current touch input to rotation values based on the original point
 * at which the touch event started.
 *
 * @param start : coordinates of first touch event
 * @param end : coordinates of final touch event
 */
private inline fun getRotationAngles(
    start: Offset,
    end: Offset,
    size: IntSize,
    maxAngle: Float,
    authority: Int
): Pair<Float, Float> {
    val distances = getDistances(end, start)
    val rotationX = (distances.x / size.width) * maxAngle * authority
    val rotationY = (distances.y / size.height) * maxAngle * authority
    return Pair(rotationX, rotationY)
}

private inline fun getDistances(p1: Offset, p2: Offset) = Offset(
    p2.x - p1.x,
    p2.y - p1.y
)
