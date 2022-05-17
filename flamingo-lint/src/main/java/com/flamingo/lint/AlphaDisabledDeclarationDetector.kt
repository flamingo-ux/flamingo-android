@file:Suppress("UnstableApiUsage")

package com.flamingo.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Location
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.TextFormat
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UField
import org.jetbrains.uast.getContainingUFile
import com.flamingo.lint.AlphaDisabledDeclarationDetector.Companion.ALPHA_DISABLED_REFERENCE
import com.flamingo.lint.AlphaDisabledDeclarationDetector.Companion.explanation

/**
 * Prevents creation of any fields with a name that contains words _alpha_ __and__ _disabled_.
 * There is already such property in the flamingo library: [ALPHA_DISABLED_REFERENCE]. It should
 * always be used, instead of creating duplicates.
 *
 * @see explanation
 */
class AlphaDisabledDeclarationDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(UField::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {
        override fun visitField(node: UField) {
            if (node.name.contains("alpha", ignoreCase = true) &&
                node.name.contains("disabled", ignoreCase = true)
            ) {
                reportUsage(context, node)
            }
        }
    }

    private fun reportUsage(context: JavaContext, node: UElement) {
        val lastImportStatementLocation: Location? =
            node.getContainingUFile()?.imports?.lastOrNull()?.let(context::getLocation)

        val removeDeclarationFix = LintFix.create()
            .name("Remove the declaration")
            .replace()
            .range(context.getLocation(node))
            .with(null)
            .build()

        val quickfix = if (lastImportStatementLocation != null) {
            val addImportFix = LintFix.create()
                .replace()
                .end()
                .range(lastImportStatementLocation)
                .with("\nimport $ALPHA_DISABLED_REFERENCE")
                .reformat(true)
                .build()

            LintFix.create()
                .name("Remove and add import for $ALPHA_DISABLED_REFERENCE")
                .composite(addImportFix, removeDeclarationFix)
        } else removeDeclarationFix

        context.report(
            issue = ISSUE,
            location = context.getLocation(node),
            scope = node,
            message = ISSUE.getExplanation(TextFormat.TEXT),
            quickfixData = quickfix
        )
    }

    companion object {
        private val IMPLEMENTATION = Implementation(
            AlphaDisabledDeclarationDetector::class.java,
            Scope.JAVA_FILE_SCOPE
        )

        private const val ALPHA_DISABLED_REFERENCE =
            "com.flamingo.ALPHA_DISABLED"

        private const val explanation = """
                Reuse already declared $ALPHA_DISABLED_REFERENCE 
                instead of creating duplication.
            """
        val ISSUE: Issue = Issue.create(
            id = "AlphaDisabledRedeclaration",
            briefDescription = explanation.trimIndent().replace("\n", ""),
            explanation = explanation.trimIndent().replace("\n", ""),
            category = Category.CORRECTNESS,
            priority = 7,
            severity = Severity.ERROR,
            implementation = IMPLEMENTATION
        )
    }
}
