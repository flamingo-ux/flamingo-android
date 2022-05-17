package com.flamingo.crab.codegen

import com.flamingo.crab.DsAnnModel
import com.flamingo.crab.THREE_QUOTES
import com.flamingo.crab.escapeForSourceCode

internal fun DsAnnModel.generateSourceCode(): String = buildString(@Suppress("MagicNumber") 500) {
    appendStringProperty("displayName", displayName)
    appendComposableLambdaProperty("preview", preview)
    appendURLProperty("figma", figma)
    appendURLProperty("specification", specification)
    viewImplementation?.let { appendKClassProperty("viewImplementation", it) }
    theaterPackage?.let { appendKClassProperty("theaterPackage", it) }
    appendStringArrayProperty("demo", demo)
    appendStringArrayProperty("usedInsteadOf", usedInsteadOf)
    append("supportsWhiteMode=$supportsWhiteMode,")
    append("internal=$internal,")
}

private fun StringBuilder.appendComposableLambdaProperty(name: String, value: String) {
    append(name)
    append("={ $value() },")
}

private fun StringBuilder.appendStringArrayProperty(name: String, values: List<String>) {
    append(name)
    append("=listOf(")
    values.forEach {
        append(THREE_QUOTES)
        append(it.escapeForSourceCode())
        append(THREE_QUOTES)
        append(',')
    }
    append("),")
}

private fun StringBuilder.appendStringProperty(name: String, value: String) {
    append(name)
    append('=')
    append(THREE_QUOTES)
    append(value.escapeForSourceCode())
    append(THREE_QUOTES)
    append(',')
}

private fun StringBuilder.appendURLProperty(name: String, value: String?) {
    append(name)
    if (value == null) {
        append("=null,")
        return
    }
    append("=URL(")
    append(THREE_QUOTES)
    append(value.escapeForSourceCode())
    append(THREE_QUOTES)
    append("),")
}

private fun StringBuilder.appendKClassProperty(name: String, value: String) {
    append(name)
    append('=')
    append(value)
    append("::class,")
}
