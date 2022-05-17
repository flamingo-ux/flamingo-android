package com.theater

/**
 * [Plot] of the [TheaterPlay]. See [PlotScope] to learn about APIs, available to write [Plot]s.
 */
public fun interface Plot<LA : Actor, S : Stage> {
    public suspend fun PlotScope<LA, S>.plot()
}
