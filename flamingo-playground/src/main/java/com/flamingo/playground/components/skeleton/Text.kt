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
    "ComplexMethod",
    "SpacingAroundParens"
)

package com.flamingo.playground.components.skeleton

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.flamingo.Flamingo
import com.flamingo.components.Skeleton
import com.flamingo.components.SkeletonShape

@Composable
internal fun Text() {
    Skeleton(
        modifier = Modifier.fillMaxWidth(),
        shape = SkeletonShape.Text(textStyle = Flamingo.typography.body1)
    )
}
