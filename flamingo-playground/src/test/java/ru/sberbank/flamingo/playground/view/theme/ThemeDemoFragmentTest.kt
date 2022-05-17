package com.flamingo.playground.view.theme

import com.google.common.truth.Truth
import org.junit.jupiter.api.Test
import java.io.InputStream

internal class ThemeDemoFragmentTest {
    /**
     * Checks that all attributes declared in base/flamingo/src/main/res/values/themes.xml are
     * documented. For the documentation format specs, see themes.xml.
     */
    @Test
    fun `all theme attrs are documented`() {
        val theme = getDSTheme(openFile("themes.xml").bufferedReader().readText())
        val declaredAttrs = Regex("<item name=\"((?>\\w|:)+)\"")
            .findAll(theme)
            .mapNotNull { it.groups[1]?.value }
            .toList()
        val attrDocs = Regex("<!-- ...? ((?>\\w|:)+),")
            .findAll(theme)
            .mapNotNull { it.groups[1]?.value }
            .toList()

        Truth.assertThat(attrDocs).containsExactlyElementsIn(declaredAttrs).inOrder()
    }

    /** @param themesXml content of the themes.xml */
    private fun getDSTheme(themesXml: String): String {
        val themeNoHeader = themesXml.substring(
            startIndex = themesXml.indexOf("<style name=\"Theme.Flamingo\""),
            endIndex = themesXml.length
        )
        return themeNoHeader.substring(0, themeNoHeader.indexOf("</style>"))
    }

    private fun openFile(filename: String): InputStream {
        return runCatching { javaClass.classLoader!!.getResourceAsStream(filename)!! }.onFailure {
            error("Failed to open $filename")
        }.getOrThrow()
    }
}
