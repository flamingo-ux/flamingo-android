package com.flamingo

import com.google.common.truth.Truth
import org.junit.jupiter.api.Test
import com.flamingo.theme.FlamingoIcon

internal class IconsConsistencyTest {

    @Test
    internal fun `compose icons are the same as android view icons`() {
        val composeIconsCount =
            FlamingoIcon.FlamingoIcons::class.java.declaredFields
                .filterNot { it.name == "DRAWABLE_NAME_PREFIX" }
                .filter { it.name[0].isUpperCase() }
                .size
        val androidViewIconsSize = loadIcons().size
        Truth.assertThat(composeIconsCount).isEqualTo(androidViewIconsSize)
    }

    private fun loadIcons(): IntArray {
        return Class.forName("com.flamingo.view.IconsKt")
            .getDeclaredField("designSystemIcons")
            .also { it.isAccessible = true }
            .get(null) as IntArray
    }
}
