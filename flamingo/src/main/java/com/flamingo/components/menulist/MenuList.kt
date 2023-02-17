package com.flamingo.components.menulist

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
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.theme.FlamingoIcon
import com.flamingo.theme.FlamingoRippleTheme

/**
 * Displays the dialog with list of items that can be selected and displayed as the button outside
 * dialog. If none of the items inside [items] is selected, the first item is selected by default,
 * with first subItem selected as well if applicable.
 *
 * @param dialogTitle text inside header of the dialog
 * @param items list of MenuList items, __should not__ be empty!
 * @param selectedItem default selected item. It is __not__ necessary to update this after
 * new item has been selected
 * @param disabled button disabled flag
 * @param textButtonParams parameters of the optional button inside MenuList dialog
 * @param onItemSelected callback, that returns selected item with its subItem if it exists
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.MenuListPreview",
    figma = "https://f.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=23080%3A121341&t=M6lVgZqL4gNEVkdG-0",
    specification = "https://confluence.companyname.ru/x/7YEq9gE",
    demo = ["com.flamingo.playground.components.menulist.MenuListStatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun MenuList(
    dialogTitle: String?,
    items: List<MenuListItem>,
    selectedItem: SelectedMenuListItem = SelectedMenuListItem(
        items.first().mainItem,
        items.first().subItems.firstOrNull()
    ),
    disabled: Boolean = false,
    textButtonParams: TextButtonParams? = null,
    onItemSelected: (SelectedMenuListItem) -> Unit
) {
    require(items.isNotEmpty()) { "MenuList should contain at least 1 item!" }

    var isVisible by remember { mutableStateOf(false) }
    var selectedMenuItem by remember(selectedItem) { mutableStateOf(selectedItem) }

    MenuListButton(
        icon = selectedMenuItem.mainItem.icon,
        title = selectedMenuItem.mainItem.label,
        disabled = disabled
    ) {
        isVisible = true
    }

    if (isVisible) {
        MenuListDialog(
            dialogTitle = dialogTitle,
            items = items,
            selectedItem = selectedMenuItem,
            textButtonParams = textButtonParams,
            onDismissRequest = {
                isVisible = false
            },
            onItemClick = {
                selectedMenuItem = it
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
    selectedItem: SelectedMenuListItem,
    textButtonParams: TextButtonParams?,
    onDismissRequest: () -> Unit,
    onItemClick: (SelectedMenuListItem) -> Unit
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
                        icon = Flamingo.icons.ArrowLeft,
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

            val expandedItem: MutableState<MenuListItemParams?> =
                remember { mutableStateOf(selectedItem.mainItem) }

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
                                widthPolicy = ButtonWidthPolicy.TRUNCATING,
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
    selectedItem: SelectedMenuListItem,
    expandedItem: MutableState<MenuListItemParams?>,
    onItemClick: (SelectedMenuListItem) -> Unit
) {
    var expanded by remember { mutableStateOf(expandedItem.value?.label == item.mainItem.label) }
    if (expandedItem.value?.label != item.mainItem.label) {
        expanded = false
    }
    val isMainItemSelected =
        selectedItem.mainItem.label == item.mainItem.label && item.subItems.isEmpty()
    Row(
        modifier = Modifier
            .alpha(item.mainItem.disabled, animate = true)
            .padding(horizontal = 16.dp)
            .clip(ItemShape)
            .run {
                if (isMainItemSelected) background(Flamingo.colors.selected) else this
            }
            .fillMaxWidth()
            .clickable(
                onClick = {
                    if (item.subItems.isNotEmpty()) {
                        expanded = !expanded
                        if (expanded) {
                            expandedItem.value = item.mainItem
                        }
                    } else {
                        onItemClick(SelectedMenuListItem(mainItem = item.mainItem, subItem = null))
                    }
                },
                enabled = !item.mainItem.disabled,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = FlamingoRippleTheme.defaultColor())
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.mainItem.icon != null) {
            Icon(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp)
                    .requiredSize(24.dp),
                icon = item.mainItem.icon,
                tint = Flamingo.colors.textSecondary
            )
        }

        Text(
            modifier = Modifier
                .padding(start = 16.dp, top = 18.dp, bottom = 18.dp)
                .weight(1f),
            text = item.mainItem.label,
            color = Flamingo.colors.textPrimary,
            style = if (isMainItemSelected) Flamingo.typography.subtitle2 else Flamingo.typography.body2,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )

        if (item.subItems.isNotEmpty()) {
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
            val selectedSubItem = selectedItem.subItem
                ?: if (selectedItem.mainItem.label == item.mainItem.label) item.subItems.firstOrNull() else null
            item.subItems.forEach { subItem ->
                val isSelected =
                    selectedItem.mainItem.label == item.mainItem.label && selectedSubItem?.label == subItem.label
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
                                onItemClick(
                                    SelectedMenuListItem(
                                        mainItem = item.mainItem,
                                        subItem = subItem
                                    )
                                )
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

/**
 * [MenuListItemParams.label] serves as the unique ID for the item!
 * __NOTE__ in [subItems], [MenuListItemParams.icon] is ignored!
 */
public data class MenuListItem(
    val mainItem: MenuListItemParams,
    val subItems: List<MenuListItemParams> = listOf()
)

public data class MenuListItemParams(
    val label: String,
    val disabled: Boolean = false,
    val icon: FlamingoIcon? = null
)

public data class SelectedMenuListItem(
    val mainItem: MenuListItemParams,
    val subItem: MenuListItemParams?
)

public data class TextButtonParams(
    val label: String,
    val icon: FlamingoIcon? = null,
    val iconAtStart: Boolean = true,
    val disabled: Boolean = false,
    val onClick: () -> Unit
)

private val ItemShape = RoundedCornerShape(8.dp)
