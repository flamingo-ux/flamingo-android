package com.flamingo.components.socialgroup.likedislike

import androidx.compose.runtime.Composable
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.FlamingoComponentBase
import com.flamingo.components.socialgroup.SocialGroup
import com.flamingo.components.socialgroup.SocialGroupColor
import com.flamingo.components.socialgroup.SocialGroupSize

/**
 * @param checked whether [Dislike] is checked or unchecked
 *
 * @param onCheckedChange callback to be invoked when [Dislike] is being clicked,
 * therefore the change of checked state in requested
 *
 * @param disabled indicates to the user that this component is not interactive. Clicks will be
 * ignored
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.DislikePreview",
    figma = "https://todo.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=628%3A18",
    specification = "https://todo.ru/x/74Eq9gE",
    demo = ["com.flamingo.playground.components.socialgroup.DislikeStatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun SocialGroup.Dislike(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    counter: String,
    disabled: Boolean = false,
    size: SocialGroupSize = SocialGroupSize.BIG,
    color: SocialGroupColor = SocialGroupColor.DEFAULT,
): Unit = FlamingoComponentBase {
    LikeDislike(checked, onCheckedChange, counter, disabled, LikeDislikeMode.DISLIKE, size, color)
}