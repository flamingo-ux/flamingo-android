package com.flamingo

import com.flamingo.lint.WrongComponentAlternativeDetector
import org.junit.Assert
import org.junit.Test

class WrongComponentAlternativeDetectorTest {
    @Test
    fun `Config is present and content is valid`() {
        val config = WrongComponentAlternativeDetector.loadConfigFromResources()
        Assert.assertTrue(config.first().first().contains("com.flamingo.components"))
    }
}