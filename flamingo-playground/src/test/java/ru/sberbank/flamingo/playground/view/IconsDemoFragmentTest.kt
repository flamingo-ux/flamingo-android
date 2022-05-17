package com.flamingo.playground.view

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import com.flamingo.playground.IconsDemoFragment

internal class IconsDemoFragmentTest {
    @Test
    fun `load icons using reflection`() = assertTrue(IconsDemoFragment.loadIcons().isNotEmpty())
}
