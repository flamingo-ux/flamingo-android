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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.components.button.animateButtonColor
import com.flamingo.theme.FlamingoRippleTheme
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
    theaterPackage = "com.flamingo.playground.components.checkbox.TheaterPkg",
    demo = ["com.flamingo.playground.components.checkbox.CheckboxStatesPlayroom"],
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
    Box(
        modifier = Modifier
            .requiredSize(40.dp)
            .alpha(disabled, animate = true)
            .run {
                if (onCheckedChange != null) {
                    clickable(
                        onClick = {
                            onCheckedChange.invoke(!checked)
                        },
                        enabled = !disabled,
                        role = Role.Button,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            bounded = false,
                            color = FlamingoRippleTheme.defaultColor()
                        ),
                    )
                } else {
                    this
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.requiredSize(24.dp),
            icon = if (checked) {
                if (state == CheckBoxState.DEFAULT) {
                    Flamingo.icons.CheckSquareV3
                } else {
                    Flamingo.icons.CheckSquareMinusV3
                }
            } else {
                Flamingo.icons.SquareV2
            },
            tint = checkboxColors(checked)
        )
    }
}

@Composable
private fun checkboxColors(checked: Boolean) = with(Flamingo) {
    (if (checked) colors.primary else colors.textSecondary).animateButtonColor()
}

public enum class CheckBoxState {
    DEFAULT,
    MINUS
}