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

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf

/**
 * Radio buttons allow users to select one option from a set.
 *
 * @sample com.flamingo.playground.components.radiobutton.Sample1
 *
 * [RadioButton]s can be combined together with [Text] in the desired layout (e.g. [Column] or
 * [Row]) to achieve radio group-like behaviour, where the entire layout is selectable:
 *
 * @sample com.flamingo.playground.components.radiobutton.Sample2
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
    demo = [
        "com.flamingo.playground.components.radiobutton.RadioButtonTypicalUsage",
        "com.flamingo.playground.components.radiobutton.RadioButtonStatesPlayroom",
    ],
    supportsWhiteMode = true,
)
@UsedInsteadOf("androidx.compose.material.RadioButton")
@Composable
public fun RadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    disabled: Boolean = false,
): Unit = FlamingoComponentBase {
    val selectableModifier =
        if (onClick != null) {
            Modifier.selectable(
                selected = selected,
                onClick = onClick,
                enabled = !disabled,
                role = Role.RadioButton,
            )
        } else {
            Modifier
        }

    Box(
        modifier = Modifier
            .requiredSize(40.dp)
            .clip(CircleShape)
            .then(selectableModifier)
            .alpha(disabled),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(targetState = selected) { selected ->
            Icon(
                modifier = Modifier.requiredSize(24.dp),
                icon = if (selected) Flamingo.icons.CheckCircle else Flamingo.icons.Circle,
                tint = if (Flamingo.isWhiteMode) Flamingo.palette.white else {
                    if (selected) Flamingo.colors.primary else Flamingo.colors.textSecondary
                },
                contentDescription = null,
            )
        }
    }
}
