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
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flamingo.components.AvatarSize
import com.flamingo.components.Skeleton
import com.flamingo.components.SkeletonShape
import com.flamingo.Flamingo

@Composable
internal fun Square() {
    Skeleton(modifier = Modifier.requiredSize(60.dp, 60.dp))
}

@Composable
internal fun RectangleNoRoundCorners() = Skeleton(
    modifier = Modifier.requiredSize(80.dp, 40.dp),
    shape = SkeletonShape.Rectangle(cornerRadiusMultiplier = 0)
)

@Composable
internal fun Rectangle() {
    Skeleton(modifier = Modifier.requiredSize(80.dp, 40.dp))
}

@Composable
internal fun Text() {
    Skeleton(
        modifier = Modifier.fillMaxWidth(),
        shape = SkeletonShape.Text(textStyle = Flamingo.typography.body1)
    )
}

@Composable
internal fun Circle() {
    Skeleton(
        modifier = Modifier.requiredSize(AvatarSize.SIZE_56.avatar),
        shape = SkeletonShape.Circle
    )
}
