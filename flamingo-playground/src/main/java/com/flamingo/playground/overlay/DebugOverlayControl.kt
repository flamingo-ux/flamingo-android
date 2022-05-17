package com.flamingo.playground.overlay

import com.flamingo.Flamingo
import com.flamingo.overlay.DebugOverlay

/**
 * Enables [Flamingo.debugOverlay], using [DebugOverlayImpl].
 *
 * [Flamingo.debugOverlay] can be turned controlled in
 * [com.flamingo.playground.DesignDemosFragment.setupDebugOverlayToggle].
 *
 * You can create your own extension function with the same name in the different package, that will
 * use your implementation of [DebugOverlay].
 */
fun Flamingo.enableDebugOverlay() {
    debugOverlay = debugOverlayImpl
}

/**
 * Disables [Flamingo.debugOverlay]. Works for any [DebugOverlay] implementation.
 *
 * [Flamingo.debugOverlay] can be turned controlled in
 * [com.flamingo.playground.DesignDemosFragment.setupDebugOverlayToggle].
 */
fun Flamingo.disableDebugOverlay() {
    debugOverlay = null
}

private val debugOverlayImpl = DebugOverlayImpl()
