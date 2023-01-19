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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx/Friend-UI-kit?node-id=6434%3A73424",
    specification = "https://confluence.companyname.ru/x/qhd5NgE",
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
        Box(
            modifier = Modifier
                .requiredSize(40.dp)
                .alpha(disabled, animate = true)
                .run {
                    if (onClick != null) {
                        clickable(
                            onClick = onClick,
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
                icon = if (selected) Flamingo.icons.CheckCircleV3 else Flamingo.icons.CircleV2,
                tint = radioButtonColors(selected = selected)
            )
        }
    }
}

@Composable
private fun radioButtonColors(selected: Boolean) = with(Flamingo) {
    if (isWhiteMode) {
        palette.white
    } else {
        if (selected) colors.primary else colors.outlineDark
    }.animateButtonColor()
}
