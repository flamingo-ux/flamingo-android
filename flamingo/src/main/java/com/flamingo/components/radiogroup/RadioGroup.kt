package com.flamingo.components.radiogroup

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
import com.flamingo.components.RadioButton
import com.flamingo.components.Text
import com.flamingo.theme.FlamingoRippleTheme

/**
 * [RadioGroup] displays RadioButtons in a column, in order they were declared
 *
 * @param radioButtons list of params, used to display each [RadioButton].
 * __NOTE__ [radioButtons] must have at least 2 items!
 *
 * @param label optional, may contain asterisk if [required] == true
 * @param description optional, displays additional info below Chips
 * @param errorText optional. If not empty, it will replace [description] text and
 * color it as an error
 *
 * @sample com.flamingo.playground.preview.RadioGroupPreview
 *
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.RadioGroupPreview",
    figma = "https://www.f.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=29839%3A212347&t=uYoTklJt7UFANDED-1",
    specification = "https://confluence.companyname.ru/x/cQeUWAI",
    theaterPackage = "com.flamingo.playground.components.radiogroup.TheaterPkg",
    demo = ["com.flamingo.playground.components.radiogroup.RadioGroupStatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun RadioGroup(
    radioButtons: List<RadioButtonData>,
    label: String? = null,
    required: Boolean = false,
    disabled: Boolean = false,
    description: String? = null,
    errorText: String? = null
) {
    require(radioButtons.size > 1) { "RadioGroup must have at least 2 items!" }
    GroupComponentsBase(
        label = label,
        required = required,
        disabled = disabled,
        description = description,
        errorText = errorText
    ) {
        radioButtons.forEach {
            RadioButtonItem(item = it, disabled = disabled)
        }
    }
}

@Composable
private fun RadioButtonItem(item: RadioButtonData, disabled: Boolean) {
    val isDisabled = if (disabled) disabled else item.disabled
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .run {
                if (item.onClick != null) {
                    clickable(
                        enabled = !isDisabled,
                        role = Role.Checkbox,
                        indication = rememberRipple(
                            bounded = true,
                            color = FlamingoRippleTheme.defaultColor()
                        ),
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = item.onClick
                    )
                } else {
                    this
                }
            }
    ) {
        Box(modifier = Modifier.padding(vertical = 12.dp)) {
            RadioButton(selected = item.selected, onClick = null, disabled = isDisabled)
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

public data class RadioButtonData(
    val label: String,
    val selected: Boolean = false,
    val onClick: (() -> Unit)? = null,
    val disabled: Boolean = false,
)
