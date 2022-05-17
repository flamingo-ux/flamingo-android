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
import com.google.common.annotations.VisibleForTesting
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UastCallKind
import com.flamingo.lint.WrongComponentAlternativeDetector.Companion.explanation

/**
 * See [com.flamingo.annotations.UsedInsteadOf] to learn the purpose of this lint check.
 *
 * ## How it works
 * 1. If there is a `gradle clean`, _`KspWrongComponentAlternatives.csv`_ is deleted from the
 * `resources` folder of the `flamingo-lint` module. (see `build.gradle` of this module)
 * 2. [crab](https://todo.ru/x/YA6oQwE) extracts @[UsedInsteadOf] from component
 * declarations and creates _`wrong component alternatives config`_ file for each component in the
 * `flamingo` module.
 * 3. Then, when processing demo module(s), [crab](https://todo.ru/x/YA6oQwE)
 * combines all configs into one compound config file - _`KspWrongComponentAlternatives.csv`_ and
 * saves it in the demo module(s).
 * 4. __Only after__ this, gradle's custom `copyConfigIntoResourcesDebug` task is launched (see
 * `build.gradle` of this module), which copies the compound config file
 * _`KspWrongComponentAlternatives.csv`_ into the `resources` folder of the `flamingo-lint` module.
 * 5. After that, resources processing and compilation of the `flamingo-lint` module begins
 * 6. Compiled lint.jar is added to the `flamingo.aar` and/or is picked up by the Android Studio to
 * perform __live__ checking of the code.
 *
 * [More info](https://todo.ru/x/UwcyeQE)
 *
 * @see com.flamingo.annotations.UsedInsteadOf
 * @see explanation
 */
class WrongComponentAlternativeDetector : Detector(), SourceCodeScanner {

    private val config = WrongComponentAlternatives()

    private class WrongComponentAlternatives {
        /**
         * To learn more about the mechanism of the generation of this file, see
         * [com.flamingo.annotations.UsedInsteadOf]
         */
        private val list: List<List<String>> = loadConfigFromResources()

        val wrongComponentNames = list.flatMap { it.drop(1) }

        fun rightComponentName(wrongComponentName: String): String? {
            return list.find { it.contains(wrongComponentName) }?.first()
        }
    }

    private val applicableMethodNames = config.wrongComponentNames
        .map { it.substringAfterLast('.') }

    override fun getApplicableMethodNames(): List<String> = applicableMethodNames

    @Suppress("ReturnCount")
    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (node.kind != UastCallKind.METHOD_CALL) return
        val packageName = method.containingClass?.qualifiedName?.substringBeforeLast('.') ?: return
        // kotlin compiler mangles names of some functions in the following fashion:
        // androidx.compose.material.IconButton -> androidx.compose.material.IconButton-e0m98-2xdo
        val fullMethodName = "$packageName.${method.name.substringBefore('-')}"
        if (!config.wrongComponentNames.contains(fullMethodName)) return
        reportUsage(context, node, fullMethodName)
    }

    private fun reportUsage(
        context: JavaContext,
        node: UCallExpression,
        wrongComponentName: String,
    ) {
        val location =
            context.getCallLocation(node, includeReceiver = true, includeArguments = false)
        val replaceWithFlamingo = LintFix.create()
            .replace()
            .range(location)
            .with(config.rightComponentName(wrongComponentName))
            .reformat(true)
            .build()

        val suppress = LintFix.create()
            .name("Suppress and provide an explanation")
            .annotate(
                "com.flamingo.annotations.UseNonFlamingoComponent(explanation = TODO())"
            )
            .build()

        val moreInfo = LintFix.create()
            .name("Z More info")
            .url(memeUrl)
            .build()

        val quickfix = LintFix.create()
            .alternatives(replaceWithFlamingo, suppress, moreInfo)

        context.report(
            issue = ISSUE,
            location = location,
            scope = node,
            message = ISSUE.getExplanation(TextFormat.TEXT),
            quickfixData = quickfix
        )
    }

    companion object {
        @VisibleForTesting
        internal fun loadConfigFromResources(): List<List<String>> =
            loadFromResources("KspWrongComponentAlternatives.csv")

        private fun loadFromResources(fileName: String): List<List<String>> {
            return (WrongComponentAlternativeDetector::class
                .java.classLoader
                .getResourceAsStream(fileName) ?: error(noConfigFoundErrorMsg(fileName)))
                .readBytes()
                .decodeToString()
                .lines()
                .map { it.split(",") }
        }

        private fun noConfigFoundErrorMsg(fileName: String) =
            "Couldn't find $fileName in resources folder. Something is wrong with the gradle " +
                    "task \"copyConfigIntoResourcesDebug\""

        private val IMPLEMENTATION = Implementation(
            WrongComponentAlternativeDetector::class.java,
            Scope.JAVA_FILE_SCOPE
        )

        private const val explanation = """
            This is a wrong alternative to the existing flamingo component. Use it instead.
            """

        private const val memeUrl = "https://todo.ru/pages/viewpage.action?pageId=" +
                "6328289107&preview=/6328289107/6328289137/meme.png"

        val ISSUE: Issue = Issue.create(
            id = "WrongComponentAlternative",
            briefDescription = explanation.trimIndent().replace("\n", ""),
            explanation = explanation.trimIndent().replace("\n", ""),
            moreInfo = memeUrl,
            category = Category.CORRECTNESS,
            priority = 7,
            severity = Severity.ERROR,
            implementation = IMPLEMENTATION,
            suppressAnnotations = listOf("com.flamingo.annotations.UseNonFlamingoComponent")
        )
    }
}
