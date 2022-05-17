package com.theater

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize

/**
 * Produces a single video file.
 */
public data class TheaterPlay<LA : Actor, S : Stage>(
    public val stage: S,

    /**
     * The leading [Actor] - an [Actor] that can be accessed by calling [PlotScope.leadActor]. It is
     * a convenience for writing a [plot]. Placed on top of other [cast] [Actor]s.
     */
    public val leadActor: LA,

    /**
     * List of [Actor]s used in the [TheaterPlay]. Can be accessed in [PlotScope.cast]. First
     * [Actor] is placed on top of others, last [Actor] is behind all others.
     */
    public val cast: List<Actor> = emptyList(),
    public val sizeConfig: SizeConfig,
    public val plot: Plot<LA, S>,

    /** @see [Backstage] */
    public val backstages: List<Backstage>,
) {
    /**
     * @param size of the visible [Stage] (in pixels), width x height
     * @param density that will be used for the [Stage] and all of it's content
     * @param stageSizeMultiplier is applied to the [size] to determine actual size of the [Stage].
     * If 1, size will be equal to the [size], if bigger - [Stage] will be centered. This multiplier
     * is needed to create [Stage]s that are larger than the visible part, so that when actors are
     * rotated, they still will be accurately displayed.
     */
    @Suppress("MagicNumber")
    public data class SizeConfig(
        val size: IntSize = IntSize(1920, 1080),
        val density: Density = Density(2f),
        val stageSizeMultiplier: Float = 3f,
    )
}
