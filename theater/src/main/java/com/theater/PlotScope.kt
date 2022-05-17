@file:Suppress("MagicNumber")

package com.theater

import android.content.Context
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

public class PlotScope<LA : Actor, S : Stage> internal constructor(
    public val stage: S,
    public val cast: List<Actor>,
    private val knobs: List<List<Knobs>>,
    public val density: Density,
    public val coroutineScope: CoroutineScope,
    public val leadActor: LA,
    public val context: Context,
) {

    /** First [Knobs] layer for the lead [Actor] */
    public val layer0: Knobs = leadActor.layer()

    /** Second [Knobs] layer for the lead [Actor] */
    public val layer1: Knobs = leadActor.layer(n = 1)

    public val orchestra: Orchestra = Orchestra(context)

    /** @see DebugMarker */
    public val debugMarkers: SnapshotStateList<DebugMarker> = mutableStateListOf()

    public inline fun <reified T> actorOfType(): T = cast.first { it is T } as T

    /**
     * @return [Knobs] layer for [Actor]
     * @param n layer index. E.g layer 1 is wrapped by layer 0 and so forth.
     */
    public fun Actor.layer(n: Int = 0): Knobs {
        val index = if (this == leadActor) 0 else cast.indexOf(this) + 1
        return knobs[index].getOrElse(n) { error("Layer $n doesn't exist") }
    }

    internal fun onPlotEnd() {
        runCatching {
            orchestra.player.release()
        }
    }

    /**
     * [blocks] are executed in parallel, function is suspended until all [blocks] are finished
     */
    public suspend inline fun parallel(vararg blocks: suspend () -> Unit) {
        blocks.map { coroutineScope.async { it.invoke() } }.awaitAll()
    }

    public inline fun skip(crossinline block: suspend () -> Any?): Unit = Unit

    /**
     * Animates all non-null parameters to their values with [spec]. [trSpec] is used for
     * [transformOrigin].
     *
     * @param translationX in dp
     * @param translationY in dp
     * @param shadowElevation in dp
     * @param cameraDistance in dp
     */
    @Suppress("LongParameterList")
    public suspend fun Knobs.animateTo(
        rotationX: Float? = null,
        rotationY: Float? = null,
        rotationZ: Float? = null,
        scaleX: Float? = null,
        scaleY: Float? = null,
        translationX: Float? = null,
        translationY: Float? = null,
        alpha: Float? = null,
        transformOrigin: TransformOrigin? = null,
        shadowElevation: Float? = null,
        cameraDistance: Float? = null,
        spec: AnimationSpec<Float> = tween(2000),
        trSpec: AnimationSpec<TransformOrigin> = tween(2000),
    ) {
        val l = ArrayList<Deferred<*>>(11)
        val knob = this
        with(coroutineScope) {
            rotationX?.let { l += async { knob.rotationX.animateTo(it, spec) } }
            rotationY?.let { l += async { knob.rotationY.animateTo(it, spec) } }
            rotationZ?.let { l += async { knob.rotationZ.animateTo(it, spec) } }
            scaleX?.let { l += async { knob.scaleX.animateTo(it, spec) } }
            scaleY?.let { l += async { knob.scaleY.animateTo(it, spec) } }
            translationX?.let { l += async { knob.translationX.animateTo(it, spec) } }
            translationY?.let { l += async { knob.translationY.animateTo(it, spec) } }
            alpha?.let { l += async { knob.alpha.animateTo(it, spec) } }
            transformOrigin?.let { l += async { knob.transformOrigin.animateTo(it, trSpec) } }
            shadowElevation?.let { l += async { knob.shadowElevation.animateTo(it, spec) } }
            cameraDistance?.let { l += async { knob.cameraDistance.animateTo(it, spec) } }
        }
        l.awaitAll()
    }

    /**
     * Animates all non-null parameters to their values with [AnimValueWithSpec.s]. Use [with] infix
     * function to create [AnimValueWithSpec].
     *
     * @param translationX in dp
     * @param translationY in dp
     * @param shadowElevation in dp
     * @param cameraDistance in dp
     */
    @Suppress("LongParameterList")
    public suspend fun Knobs.animateTo(
        rotationX: AnimValueWithSpec<Float>? = null,
        rotationY: AnimValueWithSpec<Float>? = null,
        rotationZ: AnimValueWithSpec<Float>? = null,
        scaleX: AnimValueWithSpec<Float>? = null,
        scaleY: AnimValueWithSpec<Float>? = null,
        translationX: AnimValueWithSpec<Float>? = null,
        translationY: AnimValueWithSpec<Float>? = null,
        alpha: AnimValueWithSpec<Float>? = null,
        transformOrigin: AnimValueWithSpec<TransformOrigin>? = null,
        shadowElevation: AnimValueWithSpec<Float>? = null,
        cameraDistance: AnimValueWithSpec<Float>? = null,
    ) {
        val l = ArrayList<Deferred<*>>(11)
        val knob = this
        @Suppress("MaxLinesNumber")
        with(coroutineScope) {
            rotationX?.run { l += async { knob.rotationX.animateTo(v, s) } }
            rotationY?.run { l += async { knob.rotationY.animateTo(v, s) } }
            rotationZ?.run { l += async { knob.rotationZ.animateTo(v, s) } }
            scaleX?.run { l += async { knob.scaleX.animateTo(v, s) } }
            scaleY?.run { l += async { knob.scaleY.animateTo(v, s) } }
            translationX?.run { l += async { knob.translationX.animateTo(v, s) } }
            translationY?.run { l += async { knob.translationY.animateTo(v, s) } }
            alpha?.run { l += async { knob.alpha.animateTo(v, s) } }
            transformOrigin?.run { l += async { knob.transformOrigin.animateTo(v, s) } }
            shadowElevation?.run { l += async { knob.shadowElevation.animateTo(v, s) } }
            cameraDistance?.run { l += async { knob.cameraDistance.animateTo(v, s) } }
        }
        l.awaitAll()
    }

    /**
     * Differs from [Pair]:
     * 1. short property names; needed to keep [animateTo] function body compact
     * 2. [to] replaced with [with] for more semantic meaning, e.g. "animate value __with__ spec"
     *
     * @param v target animation value
     * @param s [AnimationSpec] with which animate to [v]
     */
    public data class AnimValueWithSpec<V> internal constructor(
        public val v: V,
        public val s: AnimationSpec<V>,
    ) {
        public override fun toString(): String = "($v, $s)"
    }

    public infix fun <V> V.with(that: AnimationSpec<V>): AnimValueWithSpec<V> =
        AnimValueWithSpec(this, that)

    @Suppress("FunctionNaming")
    @Composable
    internal fun DebugMarkers(diameter: Dp = 8.dp) = debugMarkers.forEach { marker ->
        Box(
            modifier = Modifier
                .absoluteOffset {
                    val radius = diameter / 2
                    marker.offset - IntOffset(radius.roundToPx(), radius.roundToPx())
                }
                .requiredSize(diameter)
                .background(marker.color, shape = CircleShape)
        )
    }
}

/**
 * Used to mark the position in the [Stage] with a dot.
 *
 * @param offset of the dot
 * @param color of the dot
 */
public data class DebugMarker(val offset: IntOffset, val color: Color)
