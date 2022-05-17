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

import androidx.compose.runtime.Composable
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.internalComponents

/**
 * Checkboxes allow users to select one or more items from a set. Checkboxes can turn an option on
 * or off.
 *
 * @param checked whether Checkbox is checked or unchecked
 *
 * @param onCheckedChange callback to be invoked when checkbox is being clicked,
 * therefore the change of checked state in requested. If null, then this is passive
 * and relies entirely on a higher-level component to control the "checked" state.
 *
 * @param disabled indicates to the user that this component is not interactive. Clicks will be
 * ignored
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.CheckboxPreview",
    figma = "https://todo.com/file/qVO8jDuABDK9vsuLqRXeMx/Friend-UI-kit?node-id=6434%3A73416",
    specification = "https://todo.com/x/sBd5NgE",
    demo = ["com.flamingo.playground.components.CheckboxStatesPlayroom"],
    supportsWhiteMode = true,
)
@UsedInsteadOf("androidx.compose.material.Checkbox")
@Composable
public fun Checkbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    disabled: Boolean = false,
): Unit = FlamingoComponentBase {
    val white = Flamingo.palette.white
    internalComponents.IconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        disabled = disabled,
        checkedIcon = Flamingo.icons.CheckSquare,
        uncheckedIcon = Flamingo.icons.Square,
        checkedTint = if (Flamingo.isWhiteMode) white else Flamingo.colors.primary,
        uncheckedTint = if (Flamingo.isWhiteMode) white else Flamingo.colors.textSecondary,
    )
}
