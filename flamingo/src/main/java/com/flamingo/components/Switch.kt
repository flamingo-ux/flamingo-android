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

package com.flamingo.components

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.SwitchColors
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.compositeOver
import com.flamingo.ALPHA_DISABLED
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.theme.FlamingoTheme

/**
 * Switches toggle the state of a single item on or off.
 *
 * @param checked whether or not this component is checked
 *
 * @param onCheckedChange callback to be invoked when Switch is being clicked,
 * therefore the change of checked state is requested.  If null, then this is passive
 * and relies entirely on a higher-level component to control the "checked" state.
 *
 * @param disabled indicates to the user that this component is not interactive. Clicks will be
 * ignored
 *
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this Switch. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this Switch in different [Interaction]s.
 */
@OptIn(ExperimentalMaterialApi::class)
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.SwitchPreview",
    figma = "https://todo.com/file/qVO8jDuABDK9vsuLqRXeMx/Friend-UI-kit?node-id=6434%3A73432",
    specification = "https://todo.com/x/nBd5NgE",
    demo = ["com.flamingo.playground.components.SwitchStatesPlayroom"],
    supportsWhiteMode = true,
)
@UsedInsteadOf("androidx.compose.material.Switch")
@Composable
public fun Switch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    disabled: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
): Unit = FlamingoComponentBase {
    val switch: @Composable () -> Unit = {
        CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
            androidx.compose.material.Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = !disabled,
                interactionSource = interactionSource,
                colors = switchColors(),
            )
        }
    }
    if (Flamingo.isWhiteMode) FlamingoTheme(darkTheme = true, content = switch) else switch()
}

@Composable
private fun switchColors(): SwitchColors {
    val checkedThumbColor = Flamingo.colors.primary
    val checkedTrackColor = Flamingo.colors.primary
    val uncheckedThumbColor = Flamingo.colors.inverse.backgroundTertiary
    val uncheckedTrackColor = Flamingo.colors.inverse.backgroundTertiary

    return SwitchDefaults.colors(
        checkedThumbColor = checkedThumbColor,
        checkedTrackColor = checkedTrackColor,
        checkedTrackAlpha = 0.54f,
        uncheckedThumbColor = uncheckedThumbColor,
        uncheckedTrackColor = uncheckedTrackColor,
        uncheckedTrackAlpha = ALPHA_DISABLED,
        disabledCheckedThumbColor = checkedThumbColor.copy(alpha = ALPHA_DISABLED)
            .compositeOver(Flamingo.colors.background),
        disabledCheckedTrackColor = checkedTrackColor.copy(alpha = ALPHA_DISABLED)
            .compositeOver(Flamingo.colors.background),
        disabledUncheckedThumbColor = uncheckedThumbColor.copy(alpha = ALPHA_DISABLED)
            .compositeOver(Flamingo.colors.background),
        disabledUncheckedTrackColor = uncheckedTrackColor.copy(alpha = ALPHA_DISABLED)
            .compositeOver(Flamingo.colors.background),
    )
}
