package com.flamingo.crab

@Suppress("UnnecessaryAbstractClass")
internal abstract class Options(options: Map<String, String>) {
    protected val prefix = "crab."

    val moduleName =
        options.getOrElse("${prefix}moduleName") { Errors.noModuleNameInOptions(prefix) }

    val rootProjectDir: String = options.getOrElse("${prefix}rootProjectDir") {
        Errors.noRootProjectDirInOptions(prefix)
    }

    val isDemoModule: Boolean = options.getOrElse("${prefix}demoModule") { "noo" } == "yaas"
}
