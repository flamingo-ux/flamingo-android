@file:Suppress("UnstableApiUsage")

package com.flamingo.lint

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.TextFormat
import com.flamingo.lint.TestTagDetector.Companion.uiTestingTagModifierFullName
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UastCallKind

/**
 * Suggests to the user, that in the flamingo design system, [uiTestingTagModifierFullName] must be
 * used instead of [testTagModifierFullName] to support UI testing through the Appium Framework.
 */
class TestTagDetector : Detector(), SourceCodeScanner {

    private val testTagModifierFullName = "androidx.compose.ui.platform.testTag"
    private val applicableMethodNames = listOf(testTagModifierFullName.substringAfterLast('.'))

    override fun getApplicableMethodNames(): List<String> = applicableMethodNames

    @Suppress("ReturnCount")
    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (node.kind != UastCallKind.METHOD_CALL) return
        val packageName = method.containingClass?.qualifiedName?.substringBeforeLast('.') ?: return
        // kotlin compiler mangles names of some functions in the following fashion:
        // androidx.compose.material.IconButton -> androidx.compose.material.IconButton-e0m98-2xdo
        val fullMethodName = "$packageName.${method.name.substringBefore('-')}"
        if (fullMethodName != testTagModifierFullName) return
        reportUsage(context, node)
    }

    private fun reportUsage(context: JavaContext, node: UCallExpression) {
        val location =
            context.getCallLocation(node, includeReceiver = false, includeArguments = false)

        val quickfix = LintFix.create()
            .replace()
            .range(location)
            .with(uiTestingTagModifierFullName.substringAfterLast('.'))
            .reformat(true)
            .build()

        context.report(
            issue = ISSUE,
            location = location,
            scope = node,
            message = ISSUE.getExplanation(TextFormat.TEXT),
            quickfixData = quickfix
        )
    }

    companion object {
        private val IMPLEMENTATION = Implementation(
            TestTagDetector::class.java,
            Scope.JAVA_FILE_SCOPE
        )

        private const val uiTestingTagModifierFullName = "com.flamingo.uiTestingTag"

        private const val explanation = """
            If you want to support UI testing with Appium using testing tags, you MUST use already 
            developed special function for that: $uiTestingTagModifierFullName
            """

        val ISSUE: Issue = Issue.create(
            id = "PossibleTestTagMisuse",
            briefDescription = explanation.trimIndent().replace("\n", ""),
            explanation = explanation.trimIndent().replace("\n", ""),
            category = Category.TESTING,
            priority = 7,
            severity = Severity.WARNING,
            implementation = IMPLEMENTATION,
        )
    }
}
