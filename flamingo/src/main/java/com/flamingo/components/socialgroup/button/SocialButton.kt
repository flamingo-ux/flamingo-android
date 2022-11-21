package com.flamingo.components.socialgroup.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.components.Icon
import com.flamingo.components.Text
import com.flamingo.components.socialgroup.SocialGroupLayout
import com.flamingo.components.socialgroup.SocialGroupSize
import com.flamingo.theme.FlamingoIcon

@Composable
internal fun SocialButton(
    onClick: () -> Unit,
    counter: String,
    disabled: Boolean,
    size: SocialGroupSize,
    icon: FlamingoIcon,
) = SocialGroupLayout(
    modifier = Modifier
        .alpha(disabled)
        .clickable(enabled = !disabled, onClick = onClick),
    size = size,
) {
    Icon(
        modifier = Modifier.requiredSize(size.iconSize),
        icon = icon,
        tint = Flamingo.colors.textTertiary,
    )

    Text(text = counter, style = size.textStyle(), color = Flamingo.colors.textTertiary)
}
