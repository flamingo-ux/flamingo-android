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
import androidx.compose.ui.unit.dp
import com.flamingo.components.Skeleton
import com.flamingo.components.SkeletonShape

@Composable
internal fun RectangleNoRoundCorners() = Skeleton(
    modifier = Modifier.requiredSize(80.dp, 40.dp),
    shape = SkeletonShape.Rectangle(cornerRadiusMultiplier = 0)
)
