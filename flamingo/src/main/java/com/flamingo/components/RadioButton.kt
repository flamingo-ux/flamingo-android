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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flamingo.ALPHA_DISABLED
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf

/**
 * Radio buttons allow users to select one option from a set.
 *
 * @sample com.flamingo.playground.components.radiobutton.InGroup
 *
 * [RadioButton]s can be combined with [Text] in the desired layout (e.g. [Column] or [Row]) to
 * achieve radio group-like behaviour, where the entire layout is selectable:
 *
 * @sample com.flamingo.playground.components.radiobutton.InGroupInsideListItem
 *
 * @param selected boolean state for this button: either it is selected or not
 *
 * @param onClick callback to be invoked when the [RadioButton] is being clicked.  If null,
 * then this is passive and relies entirely on a higher-level component to control the state.
 *
 * @param disabled indicates to the user that this component is not interactive. Clicks will be
 * ignored
 */
@FlamingoComponent(
    displayName = "Radio Button",
    preview = "com.flamingo.playground.preview.RadioButtonPreview",
    figma = "https://todo.com/file/qVO8jDuABDK9vsuLqRXeMx/Friend-UI-kit?node-id=6434%3A73424",
    specification = "https://todo.com/x/qhd5NgE",
    demo = ["com.flamingo.playground.components.radiobutton.RadioButtonStatesPlayroom"],
    supportsWhiteMode = true,
)
@UsedInsteadOf("androidx.compose.material.RadioButton")
@OptIn(ExperimentalMaterialApi::class)
@Composable
public fun RadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    disabled: Boolean = false,
): Unit = FlamingoComponentBase {
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        androidx.compose.material.RadioButton(
            modifier = Modifier.requiredSize(40.dp),
            selected = selected,
            onClick = onClick,
            enabled = !disabled,
            colors = radioButtonColors()
        )
    }
}

@Composable
private fun radioButtonColors() = with(Flamingo) {
    if (isWhiteMode) {
        RadioButtonDefaults.colors(
            selectedColor = palette.white,
            unselectedColor = palette.white,
            disabledColor = palette.white.copy(alpha = ALPHA_DISABLED),
        )
    } else {
        RadioButtonDefaults.colors(
            selectedColor = colors.primary,
            unselectedColor = colors.textSecondary,
            disabledColor = colors.textSecondary.copy(alpha = ALPHA_DISABLED),
        )
    }
}
