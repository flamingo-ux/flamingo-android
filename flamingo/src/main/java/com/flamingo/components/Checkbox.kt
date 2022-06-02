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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.CheckboxPreview",
    figma = "https://todo.com/file/qVO8jDuABDK9vsuLqRXeMx/Friend-UI-kit?node-id=6434%3A73416",
    specification = "https://todo.com/x/sBd5NgE",
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
): Unit = FlamingoComponentBase {
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        if (Flamingo.isWhiteMode) FlamingoTheme(darkTheme = true) {
            InternalCheckbox(checked, onCheckedChange, disabled)
        }
        else {
            InternalCheckbox(checked, onCheckedChange, disabled)
        }
    }
}

@Composable
private fun InternalCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    disabled: Boolean,
) = androidx.compose.material.Checkbox(
    modifier = Modifier.requiredSize(40.dp),
    checked = checked,
    onCheckedChange = onCheckedChange,
    enabled = !disabled,
    colors = checkboxColors()
)

@Composable
private fun checkboxColors() = with(Flamingo) {
    CheckboxDefaults.colors(
        checkedColor = colors.primary,
        uncheckedColor = colors.textSecondary,
        checkmarkColor = colors.global.light.backgroundPrimary,
        disabledColor = colors.textSecondary.copy(alpha = ALPHA_DISABLED),
        disabledIndeterminateColor = Color.Magenta,
    )
}
