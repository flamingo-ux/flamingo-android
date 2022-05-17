package com.flamingo.crab

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.flamingo.crab.codegen.componentRecord
import com.flamingo.crab.codegen.registry
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.div

@Suppress("NewApi")
internal class RegistryGenerator(
    private val logger: KSPLogger,
    private val currentModuleName: String,
    private val isDemoModule: Boolean,
    private val codeGenerator: CodeGenerator,
    private val samplesFinder: SamplesFinder,
    private val rootProjectDir: Path,
) : SymbolProcessor {

    private var invoked = false

    @Suppress("ReturnCount")
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (!isDemoModule || invoked) return emptyList()

        val wrongComponentAlternativesConfig = StringBuilder()

        val registrySourceCode = resolver.findMetadataDirs().map { metadataDir ->
            /**
             * [metadataDir] is an absolute path to the module's build directory, where metadata
             * files, extracted by [MetadataExtractor], are located.
             */
            val moduleMetadataDir = metadataDir.toFile()
            val moduleMetadataFiles = moduleMetadataDir.listFiles()
                ?: Errors.cantReadMetadataDir(moduleMetadataDir.absolutePath)

            wrongComponentAlternativesConfig.append(
                createWrongComponentAlternativesConfig(moduleMetadataDir)
            )

            // todo use it in FlamingoRegistry
            val moduleName = moduleMetadataDir.resolve("module-name").readText()

            moduleMetadataFiles.mapNotNull { metadataGroup ->
                if (metadataGroup.isFile) return@mapNotNull null
                if (metadataGroup.name == WRONG_COMPONENT_ALTERNATIVES_DIR) return@mapNotNull null
                /**
                 * [metadataGroup] is a collection of files, created by [MetadataExtractor].
                 */
                val metadataGroup = metadataGroup.toPath()
                val funName = resolver.getKSNameFromString(
                    (metadataGroup / "fqn").toFile().readText()
                )
                val docs = runCatching {
                    (metadataGroup / "docs").toFile().readText()
                }.getOrNull()
                val annSource = (metadataGroup / "ann").toFile().readText()

                componentRecord(
                    funName = funName,
                    docs = docs,
                    samples = docs?.let { samplesFinder.findSamples(resolver, docs, funName) }
                        ?: emptyList(),
                    annSource = annSource,
                )
            }
            // combine records from all modules
        }.flatten().let(::registry)

        createFlamingoRegistry(registrySourceCode)

        createWrongComponentAlternativesConfig(wrongComponentAlternativesConfig.toString())

        invoked = true
        return emptyList()
    }

    private fun createWrongComponentAlternativesConfig(config: String) {
        codeGenerator.createNewFile(
            dependencies = Dependencies(true),
            packageName = PACKAGE_NAME,
            fileName = "KspWrongComponentAlternatives",
            extensionName = "csv"
        ).use { it.write(config.toByteArray()) }
    }

    private fun createFlamingoRegistry(registrySourceCode: String) {
        codeGenerator.createNewFile(
            dependencies = Dependencies(true),
            packageName = PACKAGE_NAME,
            fileName = "FlamingoRegistry",
            extensionName = "kt"
        ).use { it.write(registrySourceCode.toByteArray()) }
    }

    /**
     * @see com.flamingo.lint.WrongComponentAlternativeDetector
     */
    private fun createWrongComponentAlternativesConfig(moduleMetadataDir: File) =
        moduleMetadataDir
            .resolve(WRONG_COMPONENT_ALTERNATIVES_DIR)
            .listFiles()
            ?.joinToString(separator = "\n", postfix = "\n") { it.readText() }
            ?: ""

    @OptIn(KspExperimental::class)
    private fun Resolver.findMetadataDirs(): List<Path> {
        val locatorsDefaultPackageName = "$PACKAGE_NAME.F${
            sha256(DEFAULT_DEMO_MODULE_NAME).take(METADATA_LOCATOR_PACKAGE_NAME_LENGTH)
        }"

        val locatorsPackageName = "$PACKAGE_NAME.F${
            sha256(currentModuleName).take(METADATA_LOCATOR_PACKAGE_NAME_LENGTH)
        }"

        val locatorClasses = (getDeclarationsFromPackage(locatorsPackageName) +
                getDeclarationsFromPackage(locatorsDefaultPackageName))
                as Sequence<KSClassDeclaration>

        return locatorClasses.toList().map {
            it.annotations
                .first()
                .arguments
                .single { it.name?.asString() == "message" }
                .value!!
                .toString()
                .let { relativePath -> rootProjectDir / Path(relativePath) }
        }
    }
}

internal class RegistryGeneratorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val options = RegistryGeneratorOptions(environment.options)
        return RegistryGenerator(
            logger = environment.logger,
            currentModuleName = options.moduleName,
            isDemoModule = options.isDemoModule,
            codeGenerator = environment.codeGenerator,
            samplesFinder = SamplesFinder(environment.logger),
            rootProjectDir = Path(options.rootProjectDir),
        )
    }
}

internal class RegistryGeneratorOptions(options: Map<String, String>) : Options(options)
