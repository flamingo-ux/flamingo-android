package com.flamingo.components.switchgroup

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
import com.flamingo.components.GroupComponentsBase
import com.flamingo.components.Switch
import com.flamingo.components.Text
import com.flamingo.theme.FlamingoRippleTheme

/**
 * [SwitchGroup] displays Switches in a column, in order they were declared
 *
 * @param switches list of params, used to display each [Switch].
 * __NOTE__ [switches] must have at least 2 items!
 *
 * @param label optional, may contain asterisk if [required] == true
 * @param description optional, displays additional info below Chips
 * @param errorText optional. If not empty, it will replace [description] text and
 * color it as an error
 *
 * @sample com.flamingo.playground.preview.SwitchGroupPreview
 *
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.SwitchGroupPreview",
    figma = "https://www.f.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=29839%3A212374&t=28iWuWmTI1Aitya2-4",
    specification = "https://confluence.companyname.ru/x/cQeUWAI",
    demo = ["com.flamingo.playground.components.switchgroup.SwitchGroupStatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun SwitchGroup(
    switches: List<SwitchData>,
    label: String? = null,
    required: Boolean = false,
    disabled: Boolean = false,
    description: String? = null,
    errorText: String? = null
) {
    require(switches.size > 1) { "SwitchGroup must have at least 2 items!" }

    GroupComponentsBase(
        label = label,
        required = required,
        disabled = disabled,
        description = description,
        errorText = errorText
    ) {
        switches.forEach {
            SwitchItem(item = it, disabled = disabled)
        }
    }
}

@Composable
private fun SwitchItem(item: SwitchData, disabled: Boolean) {
    val isDisabled = if (disabled) disabled else item.disabled
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .run {
                if (item.onCheckedChange != null) {
                    clickable(
                        enabled = !isDisabled,
                        role = Role.Checkbox,
                        indication = rememberRipple(
                            bounded = true,
                            color = FlamingoRippleTheme.defaultColor()
                        ),
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            item.onCheckedChange.invoke(!item.checked)
                        }
                    )
                } else {
                    this
                }
            }
    ) {
        Text(
            modifier = Modifier
                .alpha(disabled = isDisabled, animate = true)
                .padding(vertical = 20.dp)
                .weight(1f),
            text = item.label,
            style = Flamingo.typography.body1,
            color = Flamingo.colors.textPrimary
        )
        Box(modifier = Modifier.padding(top = 12.dp, bottom = 12.dp, start = 8.dp)) {
            Switch(checked = item.checked, onCheckedChange = null, disabled = isDisabled)
        }
    }
}

public data class SwitchData(
    val label: String,
    val checked: Boolean = false,
    val onCheckedChange: ((Boolean) -> Unit)? = null,
    val disabled: Boolean = false,
)