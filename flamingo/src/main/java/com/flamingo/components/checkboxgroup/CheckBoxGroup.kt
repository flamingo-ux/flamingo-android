package com.flamingo.components.checkboxgroup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.CheckBoxState
import com.flamingo.components.Checkbox
import com.flamingo.components.GroupComponentsBase
import com.flamingo.components.Text
import com.flamingo.theme.FlamingoRippleTheme

/**
 * TODO
 * __NOTE__. [checkBoxes] must have at least 2 items!
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.CheckBoxGroupPreview",
    figma = "https://www.f.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=29839%3A212347&t=L90EwFHSoHrWu4Bj-1",
    specification = "https://confluence.companyname.ru/x/", //todo
    demo = ["com.flamingo.playground.components.checkboxgroup.CheckBoxGroupStatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun CheckBoxGroup(
    checkBoxes: List<CheckBoxData>,
    label: String? = null,
    required: Boolean = false,
    disabled: Boolean = false,
    description: String? = null,
    errorText: String? = null
) {
    require(checkBoxes.size > 1) { "CheckBoxGroup must have at least 2 items!" }

    GroupComponentsBase(
        label = label,
        required = required,
        disabled = disabled,
        description = description,
        errorText = errorText
    ) {
        checkBoxes.forEach {
            CheckBoxItem(item = it, disabled = disabled)
        }
    }
}

@Composable
private fun CheckBoxItem(item: CheckBoxData, disabled: Boolean) {
    val isDisabled = if (disabled) disabled else item.disabled
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = !isDisabled,
                role = Role.Checkbox,
                indication = rememberRipple(
                    bounded = true,
                    color = FlamingoRippleTheme.defaultColor()
                ),
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    item.onCheckedChange?.invoke(!item.checked)
                }
            )
    ) {
        Box(modifier = Modifier.padding(vertical = 12.dp)) {
            Checkbox(
                checked = item.checked,
                onCheckedChange = null,
                disabled = isDisabled,
                state = item.state
            )
        }
        Text(
            modifier = Modifier
                .alpha(disabled = isDisabled, animate = true)
                .padding(vertical = 20.dp),
            text = item.label,
            style = Flamingo.typography.body1,
            color = Flamingo.colors.textPrimary
        )
    }
}

public data class CheckBoxData(
    val label: String,
    val checked: Boolean = false,
    val onCheckedChange: ((Boolean) -> Unit)? = null,
    val state: CheckBoxState = CheckBoxState.DEFAULT,
    val disabled: Boolean = false,
)
