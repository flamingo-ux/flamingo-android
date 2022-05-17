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
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.flamingo.InternalComponents
import com.flamingo.alpha
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.internalComponents
import com.flamingo.theme.FlamingoIcon
import com.flamingo.theme.FlamingoIcon.FlamingoIcons

/**
 * If icon is [FlamingoIcons.Star], use [RatingToggleButton].
 * If icon is [FlamingoIcons.Bookmark], use [BookmarkToggleButton].
 *
 * @sample com.flamingo.playground.components.icontogglebutton.Heart
 * @sample com.flamingo.playground.components.icontogglebutton.Camera
 */
@FlamingoComponent(
    displayName = "Icon Toggle Button",
    preview = "com.flamingo.playground.preview.IconToggleButtonPreview",
    figma = "https://todo.com/file/qVO8jDuABDK9vsuLqRXeMx?node-id=17133%3A133327",
    specification = "https://todo.com/x/kK_7fgE",
    demo = ["com.flamingo.playground.components.icontogglebutton.TypicalUsage"],
    supportsWhiteMode = false,
)
@UsedInsteadOf("androidx.compose.material.IconToggleButton")
@Composable
public fun InternalComponents.IconToggleButton(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    disabled: Boolean = false,
    checkedIcon: FlamingoIcon,
    uncheckedIcon: FlamingoIcon,
    checkedTint: Color,
    uncheckedTint: Color,
    crossfadeAnimSpec: FiniteAnimationSpec<Float> = tween(),
): Unit = internalComponents.IconToggleButton(
    checked = checked,
    onCheckedChange = onCheckedChange,
    disabled = disabled,
    checkedIcon = checkedIcon,
    uncheckedIcon = uncheckedIcon,
    checkedTint = checkedTint,
    uncheckedTint = uncheckedTint,
    crossfadeAnimSpec = crossfadeAnimSpec,
    contentModifier = Modifier,
)

@Suppress("ModifierParameter")
@Composable
public fun InternalComponents.IconToggleButton(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    disabled: Boolean = false,
    checkedIcon: FlamingoIcon,
    uncheckedIcon: FlamingoIcon,
    checkedTint: Color,
    uncheckedTint: Color,
    crossfadeAnimSpec: FiniteAnimationSpec<Float> = tween(),
    contentModifier: Modifier,
): Unit = FlamingoComponentBase {
    val tint = if (checked) checkedTint else uncheckedTint
    Box(
        modifier = Modifier
            .requiredSize(40.dp)
            .clip(CircleShape)
            .alpha(disabled, animate = true)
            .run {
                if (onCheckedChange != null) toggleable(
                    value = checked,
                    onValueChange = onCheckedChange,
                    interactionSource = remember { MutableInteractionSource() },
                    enabled = !disabled,
                    role = Role.Checkbox,
                    indication = rememberRipple(color = tint.copy(alpha = .12f)),
                ) else this
            },
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            modifier = contentModifier,
            targetState = checked,
            animationSpec = crossfadeAnimSpec
        ) { checked ->
            Icon(
                modifier = Modifier.requiredSize(24.dp),
                icon = if (checked) checkedIcon else uncheckedIcon,
                tint = tint,
            )
        }
    }
}
