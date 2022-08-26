package com.flamingo.components.socialgroup.likedislike

import androidx.compose.runtime.Composable
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.FlamingoComponentBase
import com.flamingo.components.socialgroup.SocialGroup
import com.flamingo.components.socialgroup.SocialGroupColor
import com.flamingo.components.socialgroup.SocialGroupSize

/**
 * @param checked whether [Like] is checked or unchecked
 *
 * @param onCheckedChange callback to be invoked when [Like] is being clicked,
 * therefore the change of checked state in requested
 *
 * @param disabled indicates to the user that this component is not interactive. Clicks will be
 * ignored
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.LikePreview",
    figma = "https://f.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=628%3A18",
    specification = "https://confluence.companyname.ru/x/74Eq9gE",
    demo = ["com.flamingo.playground.components.socialgroup.LikeStatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun SocialGroup.Like(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    counter: String,
    disabled: Boolean = false,
    size: SocialGroupSize = SocialGroupSize.BIG,
    color: SocialGroupColor = SocialGroupColor.DEFAULT,
): Unit = FlamingoComponentBase {
    LikeDislike(checked, onCheckedChange, counter, disabled, LikeDislikeMode.LIKE, size, color)
}