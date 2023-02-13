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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.components.Icon
import com.flamingo.components.IconButton
import com.flamingo.components.IconButtonColor
import com.flamingo.components.IconButtonSize
import com.flamingo.components.IconButtonVariant
import com.flamingo.components.Text
import com.flamingo.theme.FlamingoIcon
import com.flamingo.theme.FlamingoRippleTheme

@Composable
public fun MenuList(
    modifier: Modifier = Modifier,
    icon: FlamingoIcon? = null,
    disabled: Boolean = false,
    dialogTitle: String?,
) {
    val isVisible by remember { mutableStateOf(false) }

    MenuListButton(modifier = modifier, icon = icon, disabled = disabled)

    if (isVisible) {

    }
}

@Composable
private fun MenuListButton(
    modifier: Modifier,
    icon: FlamingoIcon?,
    disabled: Boolean
) {
    Row(
        modifier = modifier
            .alpha(disabled, animate = true)
            .padding(horizontal = 16.dp)
            .clip(ItemShape)
            .fillMaxWidth()
            .clickable(
                onClick = {
                    //todo
                },
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
            text = "Title", //todo
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
    onDismissRequest: () -> Unit
) {
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
        LazyColumn {
            items(items) {
                MenuListItem(item = it)
            }
        }
    }
}

@Composable
private fun ColumnScope.MenuListItem(item: MenuListItem) {
    val expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .alpha(item.disabled, animate = true)
            .padding(horizontal = 16.dp)
            .clip(ItemShape)
            .fillMaxWidth()
            .clickable(
                onClick = {
                    //todo
                },
                enabled = item.disabled,
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
        } else {
            Spacer(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 16.dp).size(24.dp))
        }

        Text(
            modifier = Modifier
                .padding(start = 16.dp, top = 18.dp, bottom = 18.dp)
                .weight(1f),
            text = item.label,
            color = Flamingo.colors.textPrimary,
            style = Flamingo.typography.body2,
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
        Column() {
            item.list.forEach {
                Box(
                    modifier = Modifier
                        .alpha(it.disabled, animate = true)
                        .padding(horizontal = 16.dp)
                        .clip(ItemShape)
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                //todo
                            },
                            enabled = !it.disabled,
                            role = Role.Button,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(color = FlamingoRippleTheme.defaultColor())
                        )
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 18.dp, horizontal = 56.dp),
                        text = it.label,
                        color = Flamingo.colors.textSecondary,
                        style = Flamingo.typography.body2,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

//todo maybe rework (мб добавить поле selected чтобы определять выбранный пункт и в колбеке возвращать сразу MenuListItem)
public data class MenuListItem(
    val icon: FlamingoIcon? = null,
    val label: String,
    val disabled: Boolean = false,
    val list: List<MenuListSubItem> = listOf()
)

public data class MenuListSubItem(
    val label: String,
    val disabled: Boolean = false
)

private val ItemShape = RoundedCornerShape(12.dp)


@Preview
@Composable
public fun MenuListPreview() {
    MenuList(
        icon = Flamingo.icons.Menu,
        disabled = false,
        dialogTitle = null
    )
}

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
        )
    )
}
