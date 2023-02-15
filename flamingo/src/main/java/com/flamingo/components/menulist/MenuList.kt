package com.flamingo.components.menulist

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.Icon
import com.flamingo.components.IconButton
import com.flamingo.components.IconButtonColor
import com.flamingo.components.IconButtonSize
import com.flamingo.components.IconButtonVariant
import com.flamingo.components.Text
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.button.ButtonEndItem
import com.flamingo.components.button.ButtonSize
import com.flamingo.components.button.ButtonVariant
import com.flamingo.theme.FlamingoIcon
import com.flamingo.theme.FlamingoRippleTheme

/**
 * Displays the dialog with list of items that can be selected and displayed as the button outside
 * dialog. If none of the items inside [items] is selected, the first item is selected by default
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.MenuListPreview",
    figma = "https://f.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=23080%3A121341&t=M6lVgZqL4gNEVkdG-0",
    specification = "https://confluence.companyname.ru/x/-YlCQgI",
    demo = [
        "",
    ],
    supportsWhiteMode = false,
)
@Composable
public fun MenuList(
    dialogTitle: String?,
    items: List<MenuListItem>,
    icon: FlamingoIcon? = null,
    disabled: Boolean = false,
    textButtonParams: TextButtonParams? = null,
    onItemSelected: (MenuListItem) -> Unit
) {
    require(items.isNotEmpty()) { "MenuList should contain at least 1 item!" }

    var isVisible by remember { mutableStateOf(false) }
    var selectedItem by remember {
        mutableStateOf(items.firstOrNull { it.selected } ?: items.first())
    }

    MenuListButton(
        icon = icon,
        title = selectedItem.label,
        disabled = disabled
    ) {
        isVisible = true
    }

    if (isVisible) {
        MenuListDialog(
            dialogTitle = dialogTitle,
            items = items,
            selectedItem = selectedItem,
            textButtonParams = textButtonParams,
            onDismissRequest = {
                isVisible = false
            },
            onItemClick = {
                selectedItem = it
                isVisible = false
                onItemSelected(it)
            })
    }
}

@Composable
private fun MenuListButton(
    icon: FlamingoIcon?,
    title: String,
    disabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .alpha(disabled, animate = true)
            .padding(horizontal = 16.dp)
            .clip(ItemShape)
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                enabled = !disabled,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = FlamingoRippleTheme.defaultColor())
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, end = 16.dp)
                    .requiredSize(24.dp),
                icon = icon,
                tint = Flamingo.colors.textPrimary
            )
        }

        Text(
            text = title,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .weight(1f, false),
            color = Flamingo.colors.textPrimary,
            style = Flamingo.typography.h5,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Icon(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 12.dp),
            icon = Flamingo.icons.ChevronDown,
            tint = Flamingo.colors.textPrimary
        )
    }
}

@Composable
private fun MenuListDialog(
    dialogTitle: String?,
    items: List<MenuListItem>,
    selectedItem: MenuListItem,
    textButtonParams: TextButtonParams?,
    onDismissRequest: () -> Unit,
    onItemClick: (MenuListItem) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Flamingo.colors.background)
        ) {
            MenuListHeaderLayout(Modifier.padding(horizontal = 8.dp)) {
                Box(
                    modifier = Modifier
                        .layoutId("back")
                        .padding(vertical = 4.dp)
                ) {
                    IconButton(
                        onClick = onDismissRequest,
                        icon = Flamingo.icons.ChevronLeft,
                        contentDescription = null,
                        variant = IconButtonVariant.TEXT,
                        size = IconButtonSize.MEDIUM,
                        color = IconButtonColor.DEFAULT
                    )
                }

                if (dialogTitle != null) Text(
                    modifier = Modifier
                        .layoutId("title")
                        .padding(vertical = 14.dp, horizontal = 8.dp),
                    text = dialogTitle,
                    style = Flamingo.typography.h6,
                    color = Flamingo.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            val expandedItem: MutableState<MenuListItem?> =
                remember { mutableStateOf(selectedItem) }

            LazyColumn {
                items(items) {
                    MenuListItem(
                        item = it,
                        selectedItem = selectedItem,
                        expandedItem = expandedItem,
                        onItemClick = onItemClick
                    )
                }
                if (textButtonParams != null) {
                    item {
                        Box(
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            )
                        ) {
                            Button(
                                onClick = textButtonParams.onClick,
                                label = textButtonParams.label,
                                size = ButtonSize.MEDIUM,
                                color = ButtonColor.Primary,
                                variant = ButtonVariant.TEXT,
                                disabled = textButtonParams.disabled,
                                startIcon = if (textButtonParams.iconAtStart) textButtonParams.icon else null,
                                endItem = if (!textButtonParams.iconAtStart && textButtonParams.icon != null)
                                    ButtonEndItem.Icon(textButtonParams.icon) else null,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.MenuListItem(
    item: MenuListItem,
    selectedItem: MenuListItem,
    expandedItem: MutableState<MenuListItem?>,
    onItemClick: (MenuListItem) -> Unit
) {
    var expanded by remember { mutableStateOf(expandedItem.value?.label == item.label) }
    if (expandedItem.value?.label != item.label) {
        expanded = false
    }
    val isMainItemSelected = selectedItem.label == item.label && item.list.isEmpty()
    Row(
        modifier = Modifier
            .alpha(item.disabled, animate = true)
            .padding(horizontal = 16.dp)
            .clip(ItemShape)
            .run {
                if (isMainItemSelected) background(Flamingo.colors.selected) else this
            }
            .fillMaxWidth()
            .clickable(
                onClick = {
                    if (item.list.isNotEmpty()) {
                        expanded = !expanded
                        if (expanded) {
                            expandedItem.value = item
                        }
                    } else {
                        onItemClick(item.copy(selected = true))
                    }
                },
                enabled = !item.disabled,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = FlamingoRippleTheme.defaultColor())
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.icon != null) {
            Icon(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
                    .requiredSize(24.dp),
                icon = item.icon,
                tint = Flamingo.colors.textSecondary
            )
        }

        Text(
            modifier = Modifier
                .padding(start = 16.dp, top = 18.dp, bottom = 18.dp)
                .weight(1f),
            text = item.label,
            color = Flamingo.colors.textPrimary,
            style = if (isMainItemSelected) Flamingo.typography.subtitle2 else Flamingo.typography.body2,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )

        if (item.list.isNotEmpty()) {
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .rotate(animateFloatAsState(if (expanded) 180f else 0f).value)
                    .requiredSize(24.dp),
                icon = Flamingo.icons.ChevronDown,
                tint = Flamingo.colors.textSecondary
            )
        }
    }

    AnimatedVisibility(visible = expanded) {
        Column {
            val selectedSubItem = selectedItem.list.find { it.selected }
            item.list.forEachIndexed { index, subItem ->
                val isSelected =
                    selectedItem.label == item.label && selectedSubItem?.label == subItem.label
                Box(
                    modifier = Modifier
                        .alpha(subItem.disabled, animate = true)
                        .padding(horizontal = 16.dp)
                        .clip(ItemShape)
                        .run {
                            if (isSelected) background(Flamingo.colors.selected) else this
                        }
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                val newList = item.list.mapIndexed { mapIndex, menuListSubItem ->
                                    menuListSubItem.copy(selected = mapIndex == index)
                                }

                                onItemClick(item.copy(selected = true, list = newList))
                            },
                            enabled = !subItem.disabled,
                            role = Role.Button,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(color = FlamingoRippleTheme.defaultColor())
                        )
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 18.dp, horizontal = 56.dp),
                        text = subItem.label,
                        color = if (isSelected) Flamingo.colors.textPrimary else Flamingo.colors.textSecondary,
                        style = if (isSelected) Flamingo.typography.subtitle2 else Flamingo.typography.body2,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

//todo maybe rework (возможно избавиться от selected поля и возвращать в колбеке основной элемент и выбранный подпункт, если он есть)
/**
 * [label] serves as the unique ID for the item!
 *
 * if [selected] == true, then either the main item is selected (if [list] is empty),
 * or one of the sub-items is selected and the corresponding [MenuListSubItem.selected] field
 * will be true
 */
public data class MenuListItem(
    val label: String,
    val icon: FlamingoIcon? = null,
    val disabled: Boolean = false,
    val selected: Boolean = false,
    val list: List<MenuListSubItem> = listOf()
)

public data class MenuListSubItem(
    val label: String,
    val disabled: Boolean = false,
    val selected: Boolean = false
)

public data class TextButtonParams(
    val label: String,
    val icon: FlamingoIcon? = null,
    val iconAtStart: Boolean = true,
    val disabled: Boolean = false,
    val onClick: () -> Unit
)

private val ItemShape = RoundedCornerShape(8.dp)

@Preview
@Composable
public fun MenuListDialogPreview() {
    MenuListDialog(
        dialogTitle = "title",
        onDismissRequest = { },
        items = listOf(
            MenuListItem(label = "first"),
            MenuListItem(
                label = "second",
                list = listOf(
                    MenuListSubItem("adasdasd"),
                    MenuListSubItem("adasdasd"),
                    MenuListSubItem("adasdasd")
                )
            ),
            MenuListItem(icon = Flamingo.icons.Menu, label = "third"),
            MenuListItem(
                label = "fourth", list = listOf(
                    MenuListSubItem("adasdasd"),
                    MenuListSubItem("adasdasd"),
                    MenuListSubItem("adasdasd")
                )
            ),
            MenuListItem(label = "fifth"),
        ),
        selectedItem = MenuListItem(label = "first"),
        onItemClick = {

        },
        textButtonParams = TextButtonParams(
            label = "asdsadasd",

            ) {

        }
    )
}
