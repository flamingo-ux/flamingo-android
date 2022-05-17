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
    "SpacingAroundParens"
)

package com.flamingo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.ChipColor.DEFAULT
import com.flamingo.components.ChipColor.PRIMARY
import com.flamingo.components.ChipVariant.CONTAINED
import com.flamingo.components.ChipVariant.OUTLINED
import com.flamingo.theme.FlamingoIcon

/**
 * Intended to be used in [Row]. Does not support truncating text, will always display full length
 * of the content (text and icons). '\n's are replaced with spaces.
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.ChipPreview",
    figma = "https://todo.com/file/qVO8jDuABDK9vsuLqRXeMx?node-id=6750%3A71617",
    specification = "https://todo.com/x/6KBGNQE",
    theaterPackage = "com.flamingo.playground.components.chip.TheaterPkg",
    demo = ["com.flamingo.playground.components.chip.ChipStatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun Chip(
    label: String,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    variant: ChipVariant = CONTAINED,
    color: ChipColor = DEFAULT,
    icon: FlamingoIcon? = null,
    disabled: Boolean = false,
): Unit = FlamingoComponentBase {
    val backgroundColor = calculateBackgroundColor(selected, variant, color)
    val contentColor = calculateContentColor(selected, variant, color)
    val borderColor = calculateBorderColor(selected, variant, color)

    Row(
        modifier = Modifier
            .requiredHeight(intrinsicSize = IntrinsicSize.Min)
            .alpha(disabled, animate = true)
            .clip(CircleShape)
            .background(backgroundColor, CircleShape)
            .border(1.dp, borderColor, CircleShape)
            .run {
                if (onClick != null) {
                    clickable(enabled = !disabled, role = Role.Checkbox, onClick = onClick)
                } else this
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.requiredHeight(32.dp)) // for min size of the chip

        if (icon != null) {
            Icon(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .requiredSize(16.dp),
                icon = icon,
                tint = contentColor
            )
        }

        Text(
            modifier = Modifier
                .padding(
                    start = if (icon == null) 12.dp else 8.dp,
                    end = if (onDelete == null) 12.dp else 0.dp,
                )
                .align(BiasAlignment.Vertical(-0.18f)),
            text = label.replace("\n", " "),
            color = contentColor,
            style = Flamingo.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (onDelete != null) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .clickable(
                        role = Role.Button,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onDelete
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.requiredSize(16.dp),
                    icon = Flamingo.icons.XCircle,
                    tint = contentColor
                )
            }
        }
    }
}

public enum class ChipVariant { CONTAINED, OUTLINED, }
public enum class ChipColor { DEFAULT, PRIMARY, }

@Composable
private fun calculateBorderColor(
    selected: Boolean,
    variant: ChipVariant,
    color: ChipColor
) = when (variant) {
    CONTAINED -> Color.Transparent
    OUTLINED -> if (selected) when (color) {
        DEFAULT -> Flamingo.colors.outlineDark
        PRIMARY -> Flamingo.colors.primary
    } else Flamingo.colors.outlineDark
}

@Composable
private fun calculateContentColor(
    selected: Boolean,
    variant: ChipVariant,
    color: ChipColor
) = if (selected) {
    when (variant) {
        CONTAINED -> Flamingo.colors.inverse.textPrimary
        OUTLINED -> when (color) {
            DEFAULT -> Flamingo.colors.textPrimary
            PRIMARY -> Flamingo.colors.primary
        }
    }
} else Flamingo.colors.textPrimary

@Composable
private fun calculateBackgroundColor(
    selected: Boolean,
    variant: ChipVariant,
    color: ChipColor
) = when (variant) {
    CONTAINED -> if (selected) when (color) {
        DEFAULT -> Flamingo.colors.inverse.backgroundTertiary
        PRIMARY -> Flamingo.colors.primary
    } else Flamingo.colors.backgroundQuaternary
    OUTLINED -> if (selected) when (color) {
        DEFAULT -> Flamingo.colors.backgroundQuaternary
        PRIMARY -> Flamingo.colors.extensions.background.success
    } else Color.Transparent
}
