package com.flamingo.components.socialgroup.button

import androidx.compose.runtime.Composable
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.FlamingoComponentBase
import com.flamingo.components.socialgroup.SocialGroup
import com.flamingo.components.socialgroup.SocialGroupSize

/**
 * @param disabled indicates to the user that this component is not interactive. Clicks will be
 * ignored
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.SharePreview",
    figma = "https://todo.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=628%3A18",
    specification = "https://todo.ru/x/74Eq9gE",
    demo = ["com.flamingo.playground.components.socialgroup.ShareStatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun SocialGroup.Share(
    onClick: () -> Unit,
    counter: String,
    disabled: Boolean = false,
    size: SocialGroupSize = SocialGroupSize.BIG,
): Unit = FlamingoComponentBase {
    SocialButton(onClick, counter, disabled, size, Flamingo.icons.Reply)
}