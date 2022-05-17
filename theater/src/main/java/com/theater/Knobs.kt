package com.theater

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer

/**
 * [Animatable] graphical parameters of an [Actor], which can be used in [Plot]. This is the main
 * way of [Plot] construction.
 */
public class Knobs {
    public val rotationX: A = Animatable(0f)
    public val rotationY: A = Animatable(0f)
    public val rotationZ: A = Animatable(0f)

    public val scaleX: A = Animatable(1f)
    public val scaleY: A = Animatable(1f)

    /** in dp */
    public val translationX: A = Animatable(0f)

    /** in dp */
    public val translationY: A = Animatable(0f)

    public val alpha: A = Animatable(1f)
    public val transformOrigin: Animatable<TransformOrigin, AnimationVector2D> =
        Animatable(TransformOrigin.Center, transformOriginTwoWayConverter)

    /** in dp */
    public val shadowElevation: A = Animatable(0f)

    /** in dp */
    public val cameraDistance: A = Animatable(@Suppress("MagicNumber") 4f)

    public var clip: MutableState<Boolean> = mutableStateOf(false)
    public var shape: MutableState<Shape> = mutableStateOf(RectangleShape)

    internal val mirror = KnobsMirror(this)
}

private typealias A = Animatable<Float, AnimationVector1D>

internal class KnobsMirror(knobs: Knobs) {
    val rotationX: Float by knobs.rotationX.asState()
    val rotationY: Float by knobs.rotationY.asState()
    val rotationZ: Float by knobs.rotationZ.asState()

    val scaleX: Float by knobs.scaleX.asState()
    val scaleY: Float by knobs.scaleY.asState()

    val translationX: Float by knobs.translationX.asState()
    val translationY: Float by knobs.translationY.asState()

    val alpha: Float by knobs.alpha.asState()
    val transformOrigin: TransformOrigin by knobs.transformOrigin.asState()
    val shadowElevation: Float by knobs.shadowElevation.asState()
    val cameraDistance: Float by knobs.cameraDistance.asState()

    val clip: Boolean by knobs.clip
    val shape: Shape by knobs.shape
}

internal fun Modifier.graphicsLayer(knobsMirror: KnobsMirror) = graphicsLayer {
    rotationX = knobsMirror.rotationX
    rotationY = knobsMirror.rotationY
    rotationZ = knobsMirror.rotationZ

    scaleX = knobsMirror.scaleX
    scaleY = knobsMirror.scaleY

    translationX = knobsMirror.translationX * density
    translationY = knobsMirror.translationY * density

    alpha = knobsMirror.alpha
    transformOrigin = knobsMirror.transformOrigin
    shadowElevation = knobsMirror.shadowElevation * density
    cameraDistance = knobsMirror.cameraDistance * density

    clip = knobsMirror.clip
    shape = knobsMirror.shape
}

private val transformOriginTwoWayConverter = TwoWayConverter<TransformOrigin, AnimationVector2D>(
    convertToVector = { AnimationVector2D(it.pivotFractionX, it.pivotFractionY) },
    convertFromVector = { TransformOrigin(it.v1, it.v2) }
)
