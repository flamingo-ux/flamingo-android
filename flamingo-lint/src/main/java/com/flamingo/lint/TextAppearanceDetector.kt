@file:Suppress("UnstableApiUsage")

package com.flamingo.lint

import com.android.SdkConstants.ATTR_STYLE
import com.android.SdkConstants.ATTR_TEXT_APPEARANCE
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.TextFormat
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Attr
import com.flamingo.lint.TextAppearanceDetector.Companion.description

/**
 * Forces usage of `TextStyle` instead of `TextAppearance` in xml.
 *
 * @see description
 */
class TextAppearanceDetector : LayoutDetector() {

    override fun getApplicableAttributes() = listOf(ATTR_TEXT_APPEARANCE, ATTR_STYLE)

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        if (attribute.value.contains("TextAppearance.Flamingo.")) {
            context.reportTextAppearanceInsteadOfStyle(attribute)
        }
    }

    private fun XmlContext.reportTextAppearanceInsteadOfStyle(attribute: Attr) {
        report(
            issue = ISSUE,
            location = getLocation(attribute),
            scope = attribute,
            message = ISSUE.getExplanation(TextFormat.TEXT),
            quickfixData = LintFix.create()
                .name("Replace with text style")
                .replace()
                .pattern("(.*\\.DesignSystem\\.(\\w+)\")")
                .with("style=\"@style/TextStyle.Flamingo.\\k<2>\"")
                .robot(true)
                .reformat(true)
                .shortenNames()
                .build()
        )
    }

    companion object {
        private val IMPLEMENTATION = Implementation(
            TextAppearanceDetector::class.java,
            Scope.RESOURCE_FILE_SCOPE
        )

        private const val description = "Use `style` instead of `textAppearance`, " +
                "because `textAppearance` doesn't support certain attributes. If it is not possib" +
                "le, use com.flamingo.view.dsTextStyle()"

        val ISSUE: Issue = Issue.create(
            id = "TextAppearanceInsteadOfStyle",
            briefDescription = description,
            explanation = description,
            category = Category.CORRECTNESS,
            priority = 7,
            severity = Severity.FATAL,
            implementation = IMPLEMENTATION
        )
    }
}
