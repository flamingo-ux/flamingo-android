package com.flamingo.components.chipgroup

import androidx.compose.runtime.Composable
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.Chip
import com.flamingo.components.GroupComponentsBase
import com.flamingo.components.dropdown.BaseDropdownComponent
import com.flamingo.components.dropdown.Dropdown
import com.flamingo.components.dropdown.DropdownItem
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
    GroupComponentsBase(
        label = label,
        required = required,
        disabled = disabled,
        description = description,
        errorText = errorText
    ) {
        ChipGroupContentLayout() {
            chips.forEach {
                if (it.dropdownItems.isNotEmpty()) {
                    Dropdown(
                        baseDropdownComponent = BaseDropdownComponent.Chip(
                            label = it.label,
                            startIcon = it.icon,
                            selected = it.selected,
                            disabled = it.disabled
                        ),
                        items = it.dropdownItems,
                        onDropdownItemSelected = it.onDropdownItemSelected ?: {}
                    )
                } else {
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
        }
    }
}

/**
 * if [dropdownItems] is not empty, [onDelete] will be ignored
 */
public data class ChipData(
    val label: String,
    val selected: Boolean = false,
    val onClick: (() -> Unit)? = null,
    val onDelete: (() -> Unit)? = null,
    val icon: FlamingoIcon? = null,
    val disabled: Boolean = false,
    val dropdownItems: List<DropdownItem> = listOf(),
    val onDropdownItemSelected: ((DropdownItem) -> Unit)? = null
)
