package com.flamingo.components.chipgroup

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.Chip
import com.flamingo.components.Text
import com.flamingo.theme.FlamingoIcon

/**
 * [ChipGroup] displays Chips in an uneven grid to use as few rows as possible.
 * Keep in mind that Chips display in order they were declared, and not in the order using the
 * least amount of space.
 *
 * @param chips list of params, used in each [Chip]
 * @param label optional, may contain asterisk if [required] == true
 * @param description optional, displays additional info below Chips
 * @param errorText optional. If not empty, it will replace [description] text and
 * color it as an error
 *
 * @sample com.flamingo.playground.preview.ChipGroupPreview
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.ChipGroupPreview",
    figma = "https://www.f.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=29772%3A212196&t=bMX2TdeC3B6yZcWi-0",
    specification = "https://confluence.companyname.ru/x/5AA0VQI",
    demo = ["com.flamingo.playground.components.chipgroup.ChipGroupStatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun ChipGroup(
    chips: List<ChipData>,
    label: String? = null,
    required: Boolean = false,
    disabled: Boolean = false,
    description: String? = null,
    errorText: String? = null
) {
    Column {
        if (label != null && label.isNotBlank()) Label(
            label = label,
            required = required,
            disabled = disabled
        )

        ChipGroupContentLayout(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            chips.forEach {
                Chip(
                    label = it.label,
                    selected = it.selected,
                    onClick = it.onClick,
                    onDelete = it.onDelete,
                    icon = it.icon,
                    disabled = if (disabled) disabled else it.disabled
                )
            }
        }

        if ((description != null && description.isNotBlank()) ||
            (errorText != null && errorText.isNotBlank())
        ) {
            // NOTE! We are sure one of these strings is not empty and will be displayed
            Description(
                text = errorText ?: description!!,
                isError = errorText != null,
                disabled = disabled
            )
        }
    }
}

@Composable
private fun Label(label: String, required: Boolean, disabled: Boolean) {
    Text(
        modifier = Modifier
            .alpha(disabled = disabled, animate = true)
            .padding(bottom = 8.dp)
            .animateContentSize(spring(stiffness = SPRING_STIFFNESS)),
        text = buildAnnotatedString {
            append(label)
            if (required) withStyle(SpanStyle(color = Flamingo.colors.error)) { append("*") }
        },
        color = Flamingo.colors.textSecondary,
        style = Flamingo.typography.caption2
    )
}

@Composable
private fun Description(text: String, isError: Boolean, disabled: Boolean) {
    Text(
        modifier = Modifier
            .alpha(disabled = disabled, animate = true)
            .padding(bottom = 8.dp),
        text = text,
        color = if (isError) Flamingo.colors.error else Flamingo.colors.textSecondary,
        style = Flamingo.typography.caption2
    )
}

public data class ChipData(
    val label: String,
    val selected: Boolean = false,
    val onClick: (() -> Unit)? = null,
    val onDelete: (() -> Unit)? = null,
    val icon: FlamingoIcon? = null,
    val disabled: Boolean = false,
)

private const val SPRING_STIFFNESS = 700f
