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

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.flamingo.components.AvatarSize
import com.flamingo.components.Skeleton
import com.flamingo.components.SkeletonShape

@Composable
internal fun Circle() {
    Skeleton(
        modifier = Modifier.requiredSize(AvatarSize.SIZE_56.avatar),
        shape = SkeletonShape.Circle
    )
}
