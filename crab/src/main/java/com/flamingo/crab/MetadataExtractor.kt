package com.flamingo.crab

import com.flamingo.crab.DsAnnModel.Companion.DsAnnModel
import com.flamingo.crab.codegen.generateSourceCode
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassifierReference
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSName
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.file.Path
import kotlin.io.path.Path

@Suppress("NewApi")
internal class MetadataExtractor(
    private val logger: KSPLogger,
    private val currentModuleName: String,
    private val codeGenerator: CodeGenerator,
    private val targetDemoModuleName: String,
    private val rootProjectDir: Path,
) : SymbolProcessor {

    private var invoked = false

    @Suppress("ReturnCount")
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked) return emptyList()

        val dsAnnName = resolver.getKSNameFromString(DS_ANN_NAME)
        val usedInsteadOfAnnName = resolver.getKSNameFromString(USED_INSTEAD_OF_ANN_NAME)

        val annotatedSymbols = resolver
            .getSymbolsWithAnnotation(DS_ANN_NAME)
            .toList()

        if (annotatedSymbols.isEmpty()) return emptyList()

        annotatedSymbols.processDsComposables(dsAnnName, usedInsteadOfAnnName)

        val moduleMetadataDirPath = writeModuleName()
        writeMetadataDirLocator(moduleMetadataDirPath)
        invoked = true
        return emptyList()
    }

    /**
     * Creates a kotlin class (MetadataDirLocator) which is used in [RegistryGenerator] to locate
     * paths of metadata directories of all modules. This file is included in the build artifact
     * (apk).
     */
    private fun writeMetadataDirLocator(moduleMetadataDirPath: Path) {
        val locatorClassName = "F" + sha256(currentModuleName).take(METADATA_LOCATOR_NAME_LENGTH)
        val packageName = "$PACKAGE_NAME.F${
            sha256(targetDemoModuleName).take(METADATA_LOCATOR_PACKAGE_NAME_LENGTH)
        }"

        codeGenerator.createNewFile(
            Dependencies(aggregating = true), packageName, locatorClassName, "kt",
        ).use {
            val locatorSourceCode = """
                package $packageName
                
                import kotlin.DeprecationLevel.HIDDEN
                
                @Deprecated($THREE_QUOTES$moduleMetadataDirPath$THREE_QUOTES, level = HIDDEN)
                private class $locatorClassName
            """.trimIndent()
            it.write(locatorSourceCode.toByteArray())
        }
    }

    /**
     * @return path to the metadata directory of the current module, relative to the project root.
     * Relativization is performed because absolute path can contain OS username and other
     * semi-sensitive information, and this path is included in MetadataDirLocator file, which is
     * included in the build artifact (apk).
     */
    private fun writeModuleName(): Path {
        return codeGenerator.createNewFile(
            dependencies = Dependencies(aggregating = true),
            packageName = "$PACKAGE_NAME.$targetDemoModuleName",
            fileName = "module-name",
            extensionName = "",
        ).use {
            it.write(currentModuleName.toByteArray())

            it.path()?.parent?.let(rootProjectDir::relativize)
                ?: Errors.cantDetermineModuleMetadataDirPath(currentModuleName)
        }
    }

    private fun List<KSAnnotated>.processDsComposables(
        dsAnnName: KSName,
        usedInsteadOfAnnName: KSName,
    ) = forEach { annotated ->
        val location = annotated.location
        val annotated = annotated as? KSDeclaration ?: Errors.declarationWithoutName(location)
        val qualifiedName = annotated.qualifiedName ?: Errors.declarationWithoutName(location)

        val metadataGroup = sha256(qualifiedName.asString()).take(METADATA_GROUP_NAME_LENGTH)

        val containingFile = annotated.containingFile ?: Errors.noContainingFile(qualifiedName)
        val dependencies = Dependencies(true, containingFile)
        val packageName = "$PACKAGE_NAME.$targetDemoModuleName.$metadataGroup"

        val dsAnn: DsAnnotation = annotated.annotations
            .singleOrNull { it.shortName.getShortName() == dsAnnName.getShortName() }
            ?: error("No @${dsAnnName.getShortName()} found on $qualifiedName. Unreachable.")

        val usedInsteadOfAnn: KSAnnotation? = annotated.annotations
            .singleOrNull { it.shortName.getShortName() == usedInsteadOfAnnName.getShortName() }

        var docString = annotated.docString?.ifBlank { null }
        val dsAnnModel = DsAnnModel(
            dsAnn = dsAnn,
            annotatedFQN = qualifiedName,
            isInternalDsComponent = annotated.isInternalDsComponent(),
            usedInsteadOfAnn = usedInsteadOfAnn,
            docString = docString,
            logger = logger
        )
        if (!dsAnnModel.extractKDocs) docString = null

        createMetadataFile(dependencies, packageName, "fqn", qualifiedName.asString())
        if (docString != null) createMetadataFile(dependencies, packageName, "docs", docString)
        createMetadataFile(dependencies, packageName, "ann", dsAnnModel.generateSourceCode())

        if (usedInsteadOfAnn != null) createWrongComponentAlternativeConfig(
            dependencies = dependencies,
            usedInsteadOfAnn = usedInsteadOfAnn,
            componentQualifiedName = qualifiedName.asString()
        )
    }

    /**
     * @see com.flamingo.lint.WrongComponentAlternativeDetector
     */
    private fun createWrongComponentAlternativeConfig(
        dependencies: Dependencies,
        usedInsteadOfAnn: KSAnnotation,
        componentQualifiedName: String,
    ) {
        val content = (usedInsteadOfAnn.arguments.first().value as Iterable<String>)
            .joinToString(separator = ",", prefix = "$componentQualifiedName,")
        codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName = "$PACKAGE_NAME.$targetDemoModuleName.wrongComponentAlternatives",
            fileName = componentQualifiedName,
            extensionName = "csv"
        ).use { it.write(content.toByteArray()) }
    }

    @Suppress("ReturnCount")
    private fun KSDeclaration.isInternalDsComponent(): Boolean {
        if (this !is KSFunctionDeclaration) return false
        val element = this.extensionReceiver?.element
        if (element !is KSClassifierReference) return false
        return element.referencedName() == "InternalComponents"
    }

    @Suppress("MaxLineLength")
    /**
     * Files, created by this function, are included in the build artifact (apk). To exclude them,
     * follow [this](https://todo.ru/x/YA6oQwE#id-%F0%9F%A6%80CrabAnnotationProcessor-For':app'module).
     */
    private fun createMetadataFile(
        dependencies: Dependencies,
        packageName: String,
        fileName: String,
        fileContent: String,
    ) {
        codeGenerator.createNewFile(dependencies, packageName, fileName, "")
            .use { it.write(fileContent.toByteArray()) }
    }
}

internal class MetadataExtractorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val options = MetadataExtractorOptions(environment.options)
        return MetadataExtractor(
            logger = environment.logger,
            currentModuleName = options.moduleName,
            codeGenerator = environment.codeGenerator,
            targetDemoModuleName = options.targetDemoModuleName,
            rootProjectDir = Path(options.rootProjectDir),
        )
    }
}

internal class MetadataExtractorOptions(options: Map<String, String>) : Options(options) {
    val targetDemoModuleName: String = options.getOrElse("${prefix}targetDemoModuleName") {
        DEFAULT_DEMO_MODULE_NAME
    }
}

@Suppress("DiscouragedPrivateApi")
private fun OutputStream.path(): Path? = runCatching {
    val pathField = FileOutputStream::class.java.getDeclaredField("path")
    pathField.isAccessible = true
    Path(pathField.get(this as FileOutputStream) as String)
}.getOrNull()

internal const val DEFAULT_DEMO_MODULE_NAME = "defaultDemoModule"
internal const val DS_ANN_NAME = "com.flamingo.annotations.FlamingoComponent"
internal const val USED_INSTEAD_OF_ANN_NAME = "com.flamingo.annotations.UsedInsteadOf"
internal const val METADATA_LOCATOR_PACKAGE_NAME_LENGTH = 30
internal const val PACKAGE_NAME = "com.flamingo.crab.generated"
internal const val WRONG_COMPONENT_ALTERNATIVES_DIR = "wrongComponentAlternatives"
internal typealias DsAnnotation = KSAnnotation

private const val METADATA_GROUP_NAME_LENGTH = 30
private const val METADATA_LOCATOR_NAME_LENGTH = 30
