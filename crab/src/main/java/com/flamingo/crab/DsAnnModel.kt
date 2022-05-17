package com.flamingo.crab

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSName

internal interface DsAnnModel {
    val displayName: String
    val preview: String
    val figma: String?
    val specification: String?
    val viewImplementation: String?
    val theaterPackage: String?
    val demo: List<String>
    val supportsWhiteMode: Boolean
    val usedInsteadOf: List<String>
    val internal: Boolean
    val extractKDocs: Boolean

    companion object {
        @Suppress("FunctionName")
        fun DsAnnModel(
            dsAnn: DsAnnotation,
            annotatedFQN: KSName,
            isInternalDsComponent: Boolean,
            usedInsteadOfAnn: KSAnnotation?,
        ): DsAnnModel {
            val annotatedShortName = annotatedFQN.getShortName()
            val annotatedFQN = annotatedFQN.asString()
            return object : DsAnnModel {
                override val displayName = (dsAnn.arguments
                    .singleOrNull { it.name?.asString() == "displayName" }
                    ?.value as? String?)
                    .takeIf { !it.isNullOrBlank() }
                    ?: annotatedShortName

                override val preview = dsAnn.arguments
                    .singleOrNull { it.name?.asString() == "preview" }
                    ?.value as? String?
                    ?: error("No preview value found in @$DS_ANN_NAME at $annotatedFQN")

                override val figma = (dsAnn.arguments
                    .singleOrNull { it.name?.asString() == "figma" }
                    ?.value as? String?)
                    ?.validateFigmaUrl(isInternalDsComponent, annotatedFQN)
                    ?.takeIf { it.isNotBlank() }
                    ?: if (isInternalDsComponent) null
                    else error("No figma value found in @$DS_ANN_NAME at $annotatedFQN")

                override val specification = (dsAnn.arguments
                    .singleOrNull { it.name?.asString() == "specification" }
                    ?.value as? String?)
                    ?.validateSpecUrl(isInternalDsComponent, annotatedFQN)
                    ?.takeIf { it.isNotBlank() }
                    ?: if (isInternalDsComponent) null
                    else error("No specification value found in @$DS_ANN_NAME at $annotatedFQN")

                override val viewImplementation = (dsAnn.arguments
                    .singleOrNull { it.name?.asString() == "viewImplementation" }
                    ?.value as? String?)
                    ?.takeIf { it.isNotBlank() }

                override val theaterPackage = (dsAnn.arguments
                    .singleOrNull { it.name?.asString() == "theaterPackage" }
                    ?.value as? String?)
                    ?.takeIf { it.isNotBlank() }

                override val demo = (dsAnn.arguments
                    .singleOrNull { it.name?.asString() == "demo" }
                    ?.value as? List<String>)
                    ?.validateDemos(hasViewImpl = viewImplementation != null, annotatedFQN)
                    ?: error("No demos list found in @$DS_ANN_NAME at $annotatedFQN")

                override val usedInsteadOf = (usedInsteadOfAnn?.arguments
                    ?.singleOrNull { it.name?.asString() == "wrongComponentName" }
                    ?.value as? List<String>)
                    ?: emptyList()

                override val supportsWhiteMode = dsAnn.arguments
                    .singleOrNull { it.name?.asString() == "supportsWhiteMode" }
                    ?.value as? Boolean? ?: true

                override val internal: Boolean = isInternalDsComponent

                override val extractKDocs = dsAnn.arguments
                    .singleOrNull { it.name?.asString() == "extractKDocs" }
                    ?.value as? Boolean? ?: true
            }
        }

        private fun String.validateFigmaUrl(
            isInternalDsComponent: Boolean,
            annotatedFQN: String,
        ): String {
            val prefix1 = "https://www.todo.com/"
            val prefix2 = "https://todo.com/"
            val prefix3 = "https://todo."
            require((isInternalDsComponent && isEmpty()) ||
                    startsWith(prefix1) || startsWith(prefix2) || startsWith(prefix3)) {
                "Figma url value found in @$DS_ANN_NAME at $annotatedFQN is not " +
                        "valid: it doesn't start with $prefix1 or $prefix2"
            }
            return this
        }

        private fun String.validateSpecUrl(
            isInternalDsComponent: Boolean,
            annotatedFQN: String,
        ): String {
            val prefix = "https://"
            require((isInternalDsComponent && isEmpty()) || startsWith(prefix)) {
                "Specification url value found in @$DS_ANN_NAME at $annotatedFQN is " +
                        "not valid: it doesn't start with $prefix"
            }
            return this
        }

        private fun List<String>.validateDemos(
            hasViewImpl: Boolean,
            annotatedFQN: String,
        ): List<String> {
            // dsComposables that have viewImpl can have no demos, because it is needed to create
            // new demos for those composable, but time for this task is not present at the moment
            if (hasViewImpl) return this
            require(isNotEmpty()) {
                "Demos list found in @$DS_ANN_NAME at $annotatedFQN is not valid: it is empty"
            }
            return this
        }
    }
}
