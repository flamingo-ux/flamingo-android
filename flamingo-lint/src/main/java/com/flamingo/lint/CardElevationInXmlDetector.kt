@file:Suppress("UnstableApiUsage")

package com.flamingo.lint

import com.android.SdkConstants.CARD_VIEW
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.TextFormat
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Element
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node

/**
 * Enforces exclusive usage of the
 * [com.flamingo.ExtKt.setCardElevationWithBackground] function.
 *
 * For behavior demo and more explanation, open
 * [com.flamingo.playground.CardElevationDemoFragment].
 */
class CardElevationInXmlDetector : LayoutDetector() {

    private val forbiddenAttrs = listOf(
        "cardElevation",
        "cardMaxElevation",
        "elevation",
    )

    override fun getApplicableElements(): Collection<String> = listOf(CARD_VIEW.newName())

    override fun visitElement(context: XmlContext, element: Element) = with(context) {
        if (element.tagName != CARD_VIEW.newName()) return
        val invalidAttrs = element.attributes.toList().filter { node ->
            forbiddenAttrs.contains(node.localName)
        }
        invalidAttrs.forEach { reportUsage(it) }
    }

    private fun NamedNodeMap.toList(): List<Node> {
        val copy = ArrayList<Node>(length)
        for (i in 0 until length) copy.add(item(i))
        return copy
    }

    private fun XmlContext.reportUsage(node: Node) {
        report(
            issue = ISSUE,
            location = getLocation(node),
            scope = node,
            message = ISSUE.getExplanation(TextFormat.TEXT),
            quickfixData = LintFix.create()
                .replace()
                .all()
                .with("")
                .robot(false)
                .reformat(true)
                .shortenNames()
                .build()
        )
    }

    companion object {
        private val IMPLEMENTATION = Implementation(
            CardElevationInXmlDetector::class.java,
            Scope.RESOURCE_FILE_SCOPE
        )

        private const val errorMsg = "Design System prohibits setting CardView elevation from " +
                "xml. Instead, use CardView.setCardElevationWithBackground function"
        val ISSUE: Issue = Issue.create(
            id = "CardElevationInXml",
            briefDescription = errorMsg,
            explanation = errorMsg,
            category = Category.CORRECTNESS,
            priority = 7,
            severity = Severity.FATAL,
            implementation = IMPLEMENTATION
        )
    }
}
