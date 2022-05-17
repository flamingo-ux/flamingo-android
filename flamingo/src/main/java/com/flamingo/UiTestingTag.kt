package com.flamingo

import androidx.compose.ui.Modifier

/**
 * When [Flamingo.uiTestingTagsEnabled] is true, content description is set to the special [tag],
 * used only in automated ui testing.
 *
 * All flamingo components already have a tag, which is equal to their function's name.
 *
 * If calculation of the tag is somewhat expansive, use overload with lambda param, which will be
 * executed __only__ if [Flamingo.uiTestingTagsEnabled] is true.
 */
public fun Modifier.uiTestingTag(tag: String): Modifier = contentDescriptionSemantics(
    if (Flamingo.uiTestingTagsEnabled) tag else null
)

/**
 * When [Flamingo.uiTestingTagsEnabled] is true, content description is set to the special [tag],
 * used only in automated ui testing.
 *
 * All flamingo components already have a tag, which is equal to their function's name.
 *
 * Lambda [tag] will be executed __only__ if [Flamingo.uiTestingTagsEnabled] is true. If calculation
 * of the tag is cheap (e.g. tag is string literal), use overload with [String] param.
 */
public fun Modifier.uiTestingTag(tag: () -> String): Modifier = contentDescriptionSemantics(
    if (Flamingo.uiTestingTagsEnabled) tag() else null
)
