package com.flamingo.components.fab

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.FloatingActionButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.flamingo.Flamingo
import com.flamingo.Shadow
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.components.IconButton
import com.flamingo.components.IconButtonColor
import com.flamingo.components.IconButtonColor.WHITE
import com.flamingo.components.IconButtonIndicator
import com.flamingo.components.IconButtonShape
import com.flamingo.components.IconButtonSize
import com.flamingo.components.IconButtonVariant
import com.flamingo.internalComponents
import com.flamingo.theme.FlamingoIcon

/**
 * Simply the [IconButton] with elevation.
 *
 * If [loading] is true, [disabled] is also true (even if false is passed to the function).
 *
 * [WHITE] [color] MUST BE used when [IconButton] will be placed on dark backgrounds, gradients or
 * images/videos. White will not be switched to black in the dark theme
 *
 * @param indicator is always shown at the corner of the centered box with size
 * [IconButtonSize.SMALL], regardless of the [size]
 *
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this IconButton. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this IconButton in different [Interaction]s.
 *
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents. This should always be provided unless this icon is used for decorative purposes,
 * and does not represent a meaningful action that a user can take. This text should be
 * localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 */
@FlamingoComponent(
    displayName = "Floating Action Button",
    preview = "com.flamingo.playground.preview.FloatingActionButtonPreview",
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx/UI-kit?node-id=628%3A5",
    specification = "https://confluence.companyname.ru/x/iREnKQE",
    demo = ["com.flamingo.playground.components.fab.FloatingActionButtonStatesPlayroom"],
    supportsWhiteMode = true,
)
@UsedInsteadOf("androidx.compose.material.FloatingActionButton")
@Composable
public fun FloatingActionButton(
    onClick: () -> Unit,
    icon: FlamingoIcon,
    size: IconButtonSize = IconButtonSize.MEDIUM,
    color: IconButtonColor = if (Flamingo.isWhiteMode) IconButtonColor.WHITE else IconButtonColor.DEFAULT,
    shape: IconButtonShape = IconButtonShape.CIRCLE,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    indicator: IconButtonIndicator? = null,
    loading: Boolean = false,
    disabled: Boolean = false,
    contentDescription: String?,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    internalComponents.Shadow(
        elevation = elevation.elevation(interactionSource = interactionSource).value,
        opacity = 0.85f,
        shape = if (shape == IconButtonShape.CIRCLE) CircleShape else RoundedCornerShape(size.squareCorners)
    ) {
        IconButton(
            onClick = onClick,
            icon = icon,
            contentDescription = contentDescription,
            size = size,
            variant = IconButtonVariant.CONTAINED,
            color = color,
            shape = shape,
            indicator = indicator,
            loading = loading,
            disabled = disabled,
            interactionSource = interactionSource
        )
    }
}
