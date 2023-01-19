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
import com.flamingo.components.Text

@Composable
public fun ChipGroup(
    label: String? = null,
    required: Boolean = false,
    disabled: Boolean = false,
    description: String? = null,
    errorText: String? = null
) {
    Column(Modifier.alpha(disabled = disabled, animate = true)) {
        if (label != null && label.isNotBlank()) Label(label = label, required = required)

        ChipGroupContentLayout(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            // todo
        }

        if ((description != null && description.isNotBlank()) ||
            (errorText != null && errorText.isNotBlank())
        ) {
            // NOTE! We are sure one of these strings is not empty and will be displayed
            Description(text = errorText ?: description!!, isError = errorText != null)
        }
    }
}

@Composable
private fun Label(label: String, required: Boolean) {
    Text(
        modifier = Modifier
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
private fun Description(text: String, isError: Boolean) {
    Text(
        modifier = Modifier.padding(bottom = 8.dp),
        text = text,
        color = if (isError) Flamingo.colors.error else Flamingo.colors.textSecondary,
        style = Flamingo.typography.caption2
    )
}

private const val SPRING_STIFFNESS = 700f
