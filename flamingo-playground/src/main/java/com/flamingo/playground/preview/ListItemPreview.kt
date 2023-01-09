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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flamingo.components.Avatar
import com.flamingo.components.AvatarBackground
import com.flamingo.components.AvatarContent
import com.flamingo.components.AvatarSize
import com.flamingo.components.listitem.AdvancedListItem
import com.flamingo.components.listitem.ListItem
import com.flamingo.playground.boast

@Preview
@Composable
fun ListItemPreview() {
    ListItem(
        start = {
            Avatar(
                content = AvatarContent.Letters('A', 'A', AvatarBackground.PINK),
                size = AvatarSize.SIZE_40,
                contentDescription = null
            )
        },
        title = "Title of the listItem",
        subtitle = "Subtitle of the listItem",
        end = { SwitchPreview() },
        isEndSlotClickableWhenDisabled = true,
        disabled = false,
        showDivider = true,
        onClick = boast("ListItem Click"),
    )
}

@Preview
@Composable
fun AdvancedListItemPreview() {
    AdvancedListItem(
        start = {
            Box(
                modifier = Modifier
                    .requiredSize(70.dp)
                    .background(Color.Red)
            )
        },
        title = {
            Box(
                modifier = Modifier
                    .requiredSize(width = 100.dp, height = 60.dp)
                    .background(Color.Green)
            )
        },
        subtitle = {
            Box(
                modifier = Modifier
                    .requiredSize(width = 110.dp, height = 60.dp)
                    .background(Color.Blue)
            )
        },
        end = {
            Box(
                modifier = Modifier
                    .requiredSize(50.dp)
                    .background(Color.Cyan)
            )
        },
        isEndSlotClickableWhenDisabled = true,
        disabled = false,
        showDivider = true,
        onClick = boast("ListItem Click"),
    )
}
