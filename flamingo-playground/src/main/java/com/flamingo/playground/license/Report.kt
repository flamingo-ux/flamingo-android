package com.flamingo.playground.license

import android.content.Context
import com.mikepenz.aboutlibraries.entity.Library
import com.flamingo.playground.R

/**
 * Generates text report containing information about all libraries and their licenses.
 */
@OptIn(ExperimentalStdlibApi::class)
internal fun generateLibsReport(context: Context, libraries: List<Library>): String {
    val descriptionLabel = context.getString(R.string.licenses_report_description)
    val identifierLabel = context.getString(R.string.licenses_report_identifier)
    val licenseLabel = context.getString(R.string.licenses_report_license)
    val websiteLabel = context.getString(R.string.licenses_report_website)

    return libraries.mapIndexed { index, library ->
        with(library) {
            val description = description?.takeIf { it.isNotBlank() }
            val licenses = licenses
                .takeIf { it.isNotEmpty() }
                ?.joinToString { "${it.name} (${it.url})" }
            val websiteStr = website
                ?.takeIf { it.isNotBlank() }
                ?.let { "$websiteLabel: $it" }

            var i = 0
            buildList {
                add(context.getString(R.string.licenses_report_linking))
                add(context.getString(R.string.licenses_report_no_source_code_modification))
                description?.let { add("$descriptionLabel: $it") }
                add("$identifierLabel: $uniqueId")
                licenses?.let { add("$licenseLabel: $it") }
                websiteStr?.let { add(it) }
            }.joinToString(prefix = "${index + 1}. $name\n", separator = "\n") { "\t${++i}. $it" }
        }
    }.joinToString(separator = "\n")
}
