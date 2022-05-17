package com.flamingo.crab

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSName
import com.flamingo.crab.codegen.sample
import java.io.File

internal class SamplesFinder(private val logger: KSPLogger) {
    private val sampleRegex = "@sample (.*)".toRegex()

    /**
     * Finds samples referenced by `@sample` tag in KDocs:
     * ```
     * @sample com.flamingo.playground.components.radiobutton.Sample1 no code
     * @sample com.flamingo.playground.components.radiobutton.Sample2 no preview
     * dfds
     *
     * @sample com.flamingo.playground.components.radiobutton.Sample3
     * ```
     * where referenced item is a @Composable function, declared in a separate file.
     *
     * If sample reference is followed by:
     * - `no code`: source code of the sample will not be included in the registry
     * - `no preview`: sample preview will not be included in the registry
     * - `no code no preview`: whole sample will not be included in the registry
     *
     * @return empty list, if no samples was found
     */
    fun findSamples(resolver: Resolver, kdocs: String, funName: KSName): List<String> {
        return findSampleRefsInKDocs(kdocs).mapNotNull { sample ->
            val name = resolver.getKSNameFromString(sample.takeWhile { it != ' ' })
            val noCode = sample.contains("no code")
            val noPreview = sample.contains("no preview")
            if (noCode && noPreview) return@mapNotNull null
            val sourceCode = if (noCode) null else resolver.findSampleSourceCode(name, funName)
                ?: return@mapNotNull null
            sample(
                sourceCode = sourceCode,
                composableFunName = if (noPreview) null else name,
            )
        }
    }

    private fun findSampleRefsInKDocs(kdocs: String): List<String> {
        return sampleRegex.findAll(kdocs).mapNotNull {
            it.groupValues.getOrNull(1).takeIf { !it.isNullOrBlank() }?.trim()
        }.toList()
    }

    private fun Resolver.findSampleSourceCode(sample: KSName, funName: KSName): String? {
        return runCatching { findFunctionBody(sample) }.getOrElse {
            logger.warn(Errors.sampleSourceNotFound(funName, sample, it))
            return null
        }
    }

    /**
     * @return source code of the file, where function with [name] is placed.
     */
    private fun Resolver.findFunctionBody(name: KSName): String {
        val filePath = getFunctionDeclarationsByName(name, true).first().containingFile?.filePath
        return filePath?.let { File(it).readText() }
            ?: error("No containingFile for function ${name.asString()}")
    }
}
