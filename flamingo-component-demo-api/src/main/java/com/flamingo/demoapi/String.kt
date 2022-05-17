package com.flamingo.demoapi

/** @return `"value"` or `""  (empty string)` */
public fun String.wrapWithBraces(): String = buildString {
    append("\"")
    append(this@wrapWithBraces)
    append("\"")
    if (this@wrapWithBraces.isEmpty()) append(" (empty string)")
}

public fun String.parceNull(): String? = if (this == "null") null else this
