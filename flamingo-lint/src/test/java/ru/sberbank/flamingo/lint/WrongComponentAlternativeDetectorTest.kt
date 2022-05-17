package com.flamingo.lint

import org.junit.Assert.assertTrue
import org.junit.Test

class WrongComponentAlternativeDetectorTest {
    @Test
    fun `Config is present and content is valid`() {
        val config = WrongComponentAlternativeDetector.loadConfigFromResources()
        assertTrue(config.first().first().contains("com.flamingo.components"))
    }
}
