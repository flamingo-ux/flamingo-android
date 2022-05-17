@file:Suppress(
    "ModifierOrdering",
    "CommentSpacing",
    "NoBlankLineBeforeRbrace",
    "ModifierOrdering",
    "NoConsecutiveBlankLines",
    "MagicNumber",
    "FunctionName",
    "MatchingDeclarationName",
    "LongParameterList",
    "LongMethod",
    "SpacingAroundParens"
)

package com.flamingo.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import com.flamingo.Flamingo

@Composable
internal fun Placeholder(placeholder: String?, transformedText: AnnotatedString) {
    if (!placeholder.isNullOrBlank() && transformedText.isEmpty()) {
        Text(
            text = placeholder,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = Flamingo.typography.body1,
            color = Flamingo.colors.textTertiary
        )
    }
}
