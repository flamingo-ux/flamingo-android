package com.flamingo.playground.components.listitem

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.Flamingo
import com.flamingo.components.Avatar
import com.flamingo.components.AvatarBackground
import com.flamingo.components.AvatarContent
import com.flamingo.components.AvatarSize
import com.flamingo.components.listitem.ListItem
import com.flamingo.playground.boast
import com.flamingo.playground.preview.SwitchPreview
import com.flamingo.withStyle

@Preview
@Composable
@Suppress("FunctionNaming")
fun AnnotatedListItemSample() {
    ListItem(
        start = {
            Avatar(
                content = AvatarContent.Letters('A', 'A', AvatarBackground.PINK),
                size = AvatarSize.SIZE_40,
                contentDescription = null
            )
        },
        title = AnnotatedString("Title"),
        subtitle = buildAnnotatedString {
            append("Subtitle of the list item ")
            withStyle(SpanStyle(color = Flamingo.colors.info)) {
                append("with AnnotatedString")
            }
            append(".")

            withStyle(Flamingo.typography.body1) {
                append("Text with other typography style from DS: Body 1 ")
            }
        },
        end = { SwitchPreview() },
        isEndSlotClickableWhenDisabled = true,
        disabled = false,
        showDivider = true,
        onClick = boast("ListItem Click"),
    )
}
