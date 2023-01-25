package com.flamingo.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.components.checkboxgroup.CheckBoxGroup
import com.flamingo.components.chipgroup.ChipGroup
import com.flamingo.components.radiogroup.RadioGroup
import com.flamingo.components.switchgroup.SwitchGroup

/**
 * Intended to use with components, that have groups of other components
 * such as [ChipGroup], [CheckBoxGroup], [RadioGroup], [SwitchGroup]
 */
@Composable
internal fun GroupComponentsBase(
    modifier: Modifier = Modifier,
    label: String? = null,
    required: Boolean = false,
    disabled: Boolean = false,
    description: String? = null,
    errorText: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier) {
        if (label != null && label.isNotBlank()) Label(
            label = label,
            required = required,
            disabled = disabled
        )

        content()

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
            .padding(bottom = 16.dp)
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
            .padding(vertical = 8.dp),
        text = text,
        color = if (isError) Flamingo.colors.error else Flamingo.colors.textSecondary,
        style = Flamingo.typography.caption2
    )
}

private const val SPRING_STIFFNESS = 700f
