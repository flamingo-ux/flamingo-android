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

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.flamingo.ALPHA_DISABLED
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.theme.FlamingoTheme

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
 *
 * @param state defines the appearance of the active (checked) state of the CheckBox
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.CheckboxPreview",
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx/Friend-UI-kit?node-id=6434%3A73416",
    specification = "https://confluence.companyname.ru/x/sBd5NgE",
    demo = ["com.flamingo.playground.components.CheckboxStatesPlayroom"],
    supportsWhiteMode = true,
)
@UsedInsteadOf("androidx.compose.material.Checkbox")
@OptIn(ExperimentalMaterialApi::class)
@Composable
public fun Checkbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    disabled: Boolean = false,
    state: CheckBoxState = CheckBoxState.DEFAULT
): Unit = FlamingoComponentBase {
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        if (Flamingo.isWhiteMode) FlamingoTheme(darkTheme = true) {
            InternalCheckBox(checked, onCheckedChange, disabled, state)
        }
        else {
            InternalCheckBox(checked, onCheckedChange, disabled, state)
        }
    }
}

@Composable
private fun InternalCheckBox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    disabled: Boolean,
    state: CheckBoxState
) {
    TriStateCheckbox(
        modifier = Modifier.requiredSize(40.dp),
        state = if (checked) state.toggleableState else ToggleableState.Off,
        onClick = if (onCheckedChange != null) ({ onCheckedChange.invoke(!checked) }) else null,
        enabled = !disabled,
        colors = checkboxColors()
    )
}

@Composable
private fun checkboxColors() = with(Flamingo) {
    CheckboxDefaults.colors(
        checkedColor = colors.primary,
        uncheckedColor = colors.textSecondary,
        checkmarkColor = colors.global.light.backgroundPrimary,
        disabledColor = colors.textSecondary.copy(alpha = ALPHA_DISABLED),
        disabledIndeterminateColor = colors.textSecondary.copy(alpha = ALPHA_DISABLED),
    )
}

public enum class CheckBoxState(public val toggleableState: ToggleableState) {
    DEFAULT(ToggleableState.On),
    MINUS(ToggleableState.Indeterminate)
}