package com.flamingo.crab

import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.KSName
import com.google.devtools.ksp.symbol.Location

internal object Errors {
    @Suppress("MagicNumber")
    fun sampleSourceNotFound(funName: KSName, sample: KSName, throwable: Throwable): String {
        val attentionMagnet = ("\uD83D\uDEA8⬤".repeat(30) + "\n").repeat(4)
        return """
$attentionMagnet

Could not find source code for sample ${sample.asString()} while processing 
${funName.asString()}
${throwable.stackTraceToString()}

$attentionMagnet
"""
    }

    fun noRootProjectDirInOptions(prefix: String): Nothing = error(
        """
        Directory of the root project wasn't provided through options of the KSP processor.
        To fix, add this to the gradle file of the module:
        ksp {
            ...
            arg("${prefix}rootProjectDir", rootProject.projectDir.absolutePath)
        }""".trimIndent()
    )

    fun noModuleNameInOptions(prefix: String): Nothing = error(
        """
        Module name wasn't provided through options of the KSP processor.
        To fix, add this to the gradle file of the module:
        ksp {
            ...
            arg("${prefix}moduleName", project.name)
        }""".trimIndent()
    )

    fun cantDeleteMetadataFile(metadataFile: String): Nothing = error(
        "Could not delete metadata file, do a clean build: $metadataFile"
    )

    fun cantReadMetadataDir(metadataDir: String): Nothing = error(
        "Could not read metadata files: $metadataDir"
    )

    fun declarationWithoutName(location: Location): Nothing {
        val location = location as? FileLocation
        val filePath = location?.filePath
        val lineNumber = location?.lineNumber
        error("Declaration at $filePath:$lineNumber does not have a qualified name")
    }

    fun noContainingFile(qualifiedName: KSName): Nothing = error(
        "No containing file for $qualifiedName. Maybe because it does not come from a source file" +
                ", i.e. from a class file"
    )

    fun cantDetermineModuleMetadataDirPath(currentModuleName: String): Nothing = error(
        """
            Cant determine path of the metadata directory of the "$currentModuleName" module. 
            To fix this problem, do:
            1. ./gradlew clean
            2. fix implementation of com.flamingo.crab.path() function
            """.trimIndent()
    )
}
