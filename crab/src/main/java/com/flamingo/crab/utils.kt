package com.flamingo.crab

import java.security.MessageDigest

internal fun String.escapeForSourceCode(): String = replace(THREE_QUOTES, "'''")

internal fun sha256(str: String): String {
    return MessageDigest.getInstance("SHA-256").digest(str.toByteArray()).toHex()
}

internal fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }

internal const val THREE_QUOTES = "\"\"\""
