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

package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.AvatarShape
import com.flamingo.components.AvatarSize
import com.flamingo.components.listitem.AvatarSkeleton
import com.flamingo.components.listitem.ListItemSkeleton
import com.flamingo.components.listitem.TextBlocksSkeleton

@Suppress("FunctionNaming")
@Preview
@Composable
fun ListItemSkeletonPreview() {
    ListItemSkeleton(
        avatar = AvatarSkeleton(AvatarSize.SIZE_40, AvatarShape.CIRCLE),
        textBlocks = TextBlocksSkeleton.TWO,
    )
}
