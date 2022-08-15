package com.flamingo.components.socialgroup.likedislike

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.components.Icon
import com.flamingo.components.Text
import com.flamingo.components.socialgroup.SocialGroupColor
import com.flamingo.components.socialgroup.SocialGroupLayout
import com.flamingo.components.socialgroup.SocialGroupSize

@Composable
internal fun LikeDislike(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    counter: String,
    disabled: Boolean,
    mode: LikeDislikeMode,
    size: SocialGroupSize,
    color: SocialGroupColor,
) = SocialGroupLayout(
    modifier = Modifier
        .alpha(disabled)
        .toggleable(
            value = checked,
            onValueChange = onCheckedChange,
            role = Role.Checkbox,
            enabled = !disabled
        ),
    size = size
) {
    val tint = with(Flamingo.colors) {
        if (checked) when (color) {
            SocialGroupColor.DEFAULT -> textTertiary
            SocialGroupColor.PRIMARY -> primary
        } else textTertiary
    }
    Icon(
        modifier = Modifier.requiredSize(size.iconSize),
        icon = with(Flamingo.icons) {
            when (mode) {
                LikeDislikeMode.LIKE -> if (checked) ThumbsUpFilled else ThumbsUp
                LikeDislikeMode.DISLIKE -> if (checked) ThumbsDownFilled else ThumbsDown
            }
        },
        tint = tint,
    )

    Text(text = counter, color = tint, style = size.textStyle())
}

internal enum class LikeDislikeMode {
    LIKE, DISLIKE
}
