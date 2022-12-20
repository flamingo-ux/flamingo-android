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

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.ChipColor.PRIMARY
import com.flamingo.components.ChipVariant.CONTAINED
import com.flamingo.theme.FlamingoIcon

/**
 * Intended to be used in [Row]. Does not support truncating text, will always display full length
 * of the content (text and icons). '\n's are replaced with spaces.
 *
 * [variant] and [color] params are not used anymore, they remained for backwards compatibility
 * only [ChipVariant.CONTAINED] and [ChipColor.PRIMARY] are used now
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.ChipPreview",
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx?node-id=6750%3A71617",
    specification = "https://confluence.companyname.ru/x/6KBGNQE",
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
    color: ChipColor = PRIMARY,
    icon: FlamingoIcon? = null,
    disabled: Boolean = false,
) {
    Chip(label, selected, onClick, onDelete, false, variant, color, icon, disabled)
}

/**
 * Intended to be used ONLY in [Dropdown]
 */
@Composable
internal fun DropdownChip(
    label: String,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    icon: FlamingoIcon? = null,
    disabled: Boolean = false,
    isDropdownExpanded: Boolean = false
) {
    Chip(
        label = label,
        selected = selected,
        onClick = onClick,
        isDropdown = true,
        icon = icon,
        disabled = disabled,
        isDropdownExpanded = isDropdownExpanded
    )
}

@Composable
private fun Chip(
    label: String,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    isDropdown: Boolean = false,
    variant: ChipVariant = CONTAINED,
    color: ChipColor = PRIMARY,
    icon: FlamingoIcon? = null,
    disabled: Boolean = false,
    isDropdownExpanded: Boolean = false
): Unit = FlamingoComponentBase {

    require(!(isDropdown && onDelete != null)) {
        "Chip doesn't support onDelete and dropdown functionality simultaneously"
    }

    val backgroundColor = calculateBackgroundColor(selected)
    val contentColor = calculateContentColor(selected)

    Row(
        modifier = Modifier
            .requiredHeight(intrinsicSize = IntrinsicSize.Min)
            .alpha(disabled, animate = true)
            .clip(CircleShape)
            .background(backgroundColor, CircleShape)
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
                    end = if (onDelete == null && !isDropdown) 12.dp else 0.dp,
                )
                .align(BiasAlignment.Vertical(-0.18f)),
            text = label.replace("\n", " "),
            color = contentColor,
            style = Flamingo.typography.body2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (isDropdown) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f, matchHeightConstraintsFirst = true),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .requiredSize(16.dp)
                        .rotate(animateFloatAsState(if (isDropdownExpanded) 180f else 0f).value),
                    icon = Flamingo.icons.ChevronDown,
                    tint = contentColor
                )
            }
        }

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
private fun calculateContentColor(
    selected: Boolean
) = if (selected) Flamingo.colors.primary else Flamingo.colors.textPrimary

@Composable
private fun calculateBackgroundColor(
    selected: Boolean
) = if (selected) Flamingo.colors.backgroundQuinary else Flamingo.colors.backgroundQuaternary
