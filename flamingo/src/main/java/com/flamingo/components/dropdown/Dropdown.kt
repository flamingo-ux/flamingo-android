package com.flamingo.components.dropdown

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.Card
import com.flamingo.components.CornerRadius
import com.flamingo.components.DropdownChip
import com.flamingo.components.Elevation
import com.flamingo.components.Icon
import com.flamingo.components.IconButton
import com.flamingo.components.Text
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.button.ButtonEndItem
import com.flamingo.components.button.ButtonSize
import com.flamingo.components.tabrow.Tab
import com.flamingo.components.tabrow.TabVariant
import com.flamingo.components.tabrow.getTabTextColor
import com.flamingo.theme.FlamingoIcon

/**
 * TODO
 *
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.DropdownPreview",
    figma = "https://f.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=666%3A371&t=XAfutUOhgSNQfY6x-1",
    specification = "https://confluence.companyname.ru/x/-YlCQgI",
    demo = [""],
    supportsWhiteMode = false,
)
@Composable
public fun Dropdown(
    baseComponent: BaseComponent,
    items: List<DropdownItem>,
    onDropdownItemSelected: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var offset by remember { mutableStateOf(IntOffset(0, 0)) }
    Box(modifier = Modifier.onSizeChanged {
        offset = IntOffset(0, it.height)
    }) {
        when (baseComponent) {
            is BaseComponent.Button -> {
                Button(
                    onClick = { isExpanded = true },
                    label = baseComponent.label,
                    startIcon = baseComponent.startIcon,
                    endItem = ButtonEndItem.Icon(
                        Flamingo.icons.ChevronDown,
                        if (isExpanded) 180f else 0f
                    ),
                    size = baseComponent.size,
                    color = baseComponent.color
                )
            }
            is BaseComponent.Chip -> {
                DropdownChip(
                    label = baseComponent.label,
                    onClick = { isExpanded = true },
                    icon = baseComponent.startIcon
                )
            }
            is BaseComponent.IconButton -> {
                IconButton(
                    onClick = { isExpanded = true },
                    icon = baseComponent.icon,
                    contentDescription = null
                )
            }
            is BaseComponent.Tab -> {
                Tab(
                    modifier = Modifier
                        .clip(
                            if (baseComponent.variant == TabVariant.Contained) CircleShape
                            else RoundedCornerShape(8.dp)
                        )
                        .alpha(baseComponent.disabled, animate = true),
                    selected = baseComponent.selected,
                    enabled = !baseComponent.disabled,
                    variant = baseComponent.variant,
                    onClick = {
                        if (baseComponent.selected) {
                            isExpanded = true
                        } else {
                            baseComponent.onTabSelect()
                        }
                    },
                ) {
                    val textColor by animateColorAsState(
                        getTabTextColor(baseComponent.variant, baseComponent.selected),
                        animationSpec = tween(300)
                    )
                    Text(
                        text = baseComponent.label.replace("\n", " "),
                        style = if (baseComponent.variant == TabVariant.Contained) {
                            Flamingo.typography.body1
                        } else {
                            Flamingo.typography.h6
                        },
                        color = textColor,
                    )

                    Icon(
                        icon = Flamingo.icons.ChevronDown,
                        tint = textColor,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .requiredSize(16.dp)
                            .rotate(animateFloatAsState(if (isExpanded) 180f else 0f).value)
                    )
                }
            }
        }

        val scrollState = rememberScrollState()



        if (isExpanded) {
            Popup(onDismissRequest = { isExpanded = false }, offset = offset) {

                Card(elevation = Elevation.Solid.Medium, cornerRadius = CornerRadius.SMALL) {
                    Column(
                        modifier = Modifier
                            .requiredSizeIn(
                                minWidth = 192.dp,
                                maxWidth = 224.dp,
                                maxHeight = 288.dp
                            )
                            .verticalScroll(scrollState)
                    ) {
                        Spacer(Modifier.size(8.dp))
                        for (item in items) {
                            DropdownItem(item, onDropdownItemSelected)
                        }
                        Spacer(Modifier.size(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DropdownItem(item: DropdownItem, onItemClick: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(item.disabled)
            .clickable(enabled = !item.disabled) {
                onItemClick(item.label)
            }
    ) {
        if (item.icon != null) {
            Icon(
                icon = item.icon,
                tint = Flamingo.colors.textPrimary,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 8.dp)
                    .requiredSize(16.dp)
            )
        }

        Text(
            text = item.label,
            style = Flamingo.typography.body1,
            color = Flamingo.colors.textPrimary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 8.dp,
                end = 16.dp,
                start = if (item.icon != null) 0.dp else 16.dp
            )
        )
    }
}

public sealed class BaseComponent {
    public data class Button(
        val label: String,
        val startIcon: FlamingoIcon? = null,
        val size: ButtonSize = ButtonSize.MEDIUM,
        val color: ButtonColor = ButtonColor.Primary
    ) : BaseComponent()

    public data class IconButton(val icon: FlamingoIcon) : BaseComponent()
    public data class Chip(val label: String, val startIcon: FlamingoIcon? = null) : BaseComponent()
    internal data class Tab(
        val label: String,
        val variant: TabVariant,
        val disabled: Boolean,
        val selected: Boolean,
        val onTabSelect: () -> Unit
    ) : BaseComponent()
}

public data class DropdownItem(
    val label: String,
    val icon: FlamingoIcon? = null,
    val disabled: Boolean = false
)