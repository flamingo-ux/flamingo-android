@file:Suppress("FunctionNaming")

package com.theater

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity

/**
 * A single layer of content, [Knobs] of which can be animated separately from other [Actor]s
 */
public interface Actor {
    @Composable
    public fun ActorScope.Actor()

    public object EmptyActor : Actor {
        @Composable
        override fun ActorScope.Actor(): Unit = Unit
    }
}

public interface ActorScope {
    /** null if [Actor] is in the [Backstage] */
    public val theaterPackage: TheaterPackage?
    public val sizeConfig: TheaterPlay.SizeConfig
}

/**
 * Value of [LocalDensity] in the [Actor.Actor] function is not equal to the
 * [TheaterPlay.SizeConfig.density], because [Actor]s must be of the same:
 * 1. pixel size on all screens in render mode;
 * 2. size, relative to the [Stage] size.
 *
 * Thus, value of [LocalDensity] is [TheaterPlay.SizeConfig.density] * [densityMultiplier].
 *
 * If some [Actor]s want to override value of [LocalDensity] for a part of the composable
 * hierarchy, it is __required__ to do it like so:
 * ```
 * CompositionLocalProvider(LocalDensity provides Density(densityMultiplier * yourDensity)) {
 *     // ...
 * }
 * ```
 */
public val ActorScope.densityMultiplier: Float
    @Composable @ReadOnlyComposable
    get() = LocalDensity.current.density / sizeConfig.density.density

internal class ActorScopeImpl(override val theaterPackage: TheaterPackage) : ActorScope {
    override val sizeConfig: TheaterPlay.SizeConfig = theaterPackage.play.sizeConfig
}
