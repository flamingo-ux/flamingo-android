package com.flamingo.overlay

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class LocalDebugOverlayTest {

    @Test
    fun currentFunctionNameTest() = assertEquals(
        "com.flamingo.overlay.currentFunctionNameTest", currentFunctionName(0)
    )

    @Test
    fun currentFunctionNameDepthTest() = assertEquals(
        "com.flamingo.overlay.currentFunctionNameDepthTest", proxy()
    )

    private fun proxy(): String = currentFunctionName(callDepth = 1)
}
