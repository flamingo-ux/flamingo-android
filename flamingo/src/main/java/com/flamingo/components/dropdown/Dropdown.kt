package com.flamingo.components.dropdown

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
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
import com.flamingo.components.IconButtonColor
import com.flamingo.components.IconButtonShape
import com.flamingo.components.IconButtonSize
import com.flamingo.components.IconButtonVariant
import com.flamingo.components.Text
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.button.ButtonEndItem
import com.flamingo.components.button.ButtonSize
import com.flamingo.components.button.ButtonVariant
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.components.tabrow.Tab
import com.flamingo.components.tabrow.TabVariant
import com.flamingo.components.tabrow.getTabTextColor
import com.flamingo.theme.FlamingoIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @param baseDropdownComponent component to which dropdown is anchored. Supported components are
 * [Button], [IconButton], [Chip] and [Tab]. [Tab] can't be accessed directly, only inside [TabRow]
 * to match the behavior of this component
 *
 * @param items Items to be displayed inside [Dropdown]. If the amount of items is more than can
 * be displayed [Dropdown] will be scrollable
 *
 * @param onDropdownItemSelected callback that is called when item is selected. Returns clicked item
 *
 * @sample com.flamingo.playground.components.tabrow.TabsWithDropdown
 * @sample com.flamingo.playground.components.dropdown.ButtonDropdownWithChangingLabel
 * @sample com.flamingo.playground.components.dropdown.IconButtonDropdown
 * @sample com.flamingo.playground.components.dropdown.ChipDropdown
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.DropdownPreview",
    figma = "https://f.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=666%3A371&t=XAfutUOhgSNQfY6x-1",
    specification = "https://confluence.companyname.ru/x/-YlCQgI",
    demo = [
        "com.flamingo.playground.components.dropdown.DropdownStatesPlayroom",
        "com.flamingo.playground.components.dropdown.DropdownTypicalUsage"
    ],
    supportsWhiteMode = false,
)
@Composable
public fun Dropdown(
    modifier: Modifier = Modifier,
    baseDropdownComponent: BaseDropdownComponent,
    items: List<DropdownItem>,
    onDropdownItemSelected: (DropdownItem) -> Unit
) {
    require(items.isNotEmpty()) { "items should not be empty!" }

    val isExpanded = remember { mutableStateOf(false) }
    var offset by remember { mutableStateOf(IntOffset(0, 0)) }

    Box(modifier = modifier.onSizeChanged {
        offset = IntOffset(0, it.height)
    }) {
        BaseActionComponent(baseDropdownComponent, isExpanded.value) {
            isExpanded.value = true
        }

        if (isExpanded.value) {
            DropdownMenu(offset, isExpanded, items, onDropdownItemSelected)
        }
    }
}

@Composable
private fun BaseActionComponent(
    baseDropdownComponent: BaseDropdownComponent,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    when (baseDropdownComponent) {
        is BaseDropdownComponent.Button -> {
            Button(
                onClick = onClick,
                label = baseDropdownComponent.label,
                startIcon = baseDropdownComponent.startIcon,
                endItem = ButtonEndItem.Icon(
                    Flamingo.icons.ChevronDown,
                    if (isExpanded) 180f else 0f
                ),
                size = baseDropdownComponent.size,
                color = baseDropdownComponent.color,
                loading = baseDropdownComponent.loading,
                variant = baseDropdownComponent.variant,
                disabled = baseDropdownComponent.disabled,
                fillMaxWidth = baseDropdownComponent.fillMaxWidth,
                widthPolicy = baseDropdownComponent.widthPolicy
            )
        }
        is BaseDropdownComponent.Chip -> {
            DropdownChip(
                label = baseDropdownComponent.label,
                onClick = onClick,
                icon = baseDropdownComponent.startIcon,
                selected = baseDropdownComponent.selected,
                disabled = baseDropdownComponent.disabled,
                isDropdownExpanded = isExpanded
            )
        }
        is BaseDropdownComponent.IconButton -> {
            IconButton(
                onClick = onClick,
                icon = baseDropdownComponent.icon,
                contentDescription = baseDropdownComponent.contentDescription,
                size = baseDropdownComponent.size,
                variant = baseDropdownComponent.variant,
                color = baseDropdownComponent.color,
                shape = baseDropdownComponent.shape,
                loading = baseDropdownComponent.loading,
                disabled = baseDropdownComponent.disabled
            )
        }
        is BaseDropdownComponent.Tab -> {
            Tab(
                modifier = Modifier
                    .clip(
                        if (baseDropdownComponent.variant == TabVariant.Contained) CircleShape
                        else RoundedCornerShape(8.dp)
                    )
                    .alpha(baseDropdownComponent.disabled, animate = true),
                selected = baseDropdownComponent.selected,
                enabled = !baseDropdownComponent.disabled,
                variant = baseDropdownComponent.variant,
                onClick = {
                    if (baseDropdownComponent.selected) {
                        onClick()
                    } else {
                        baseDropdownComponent.onTabSelect()
                    }
                },
            ) {
                val textColor by animateColorAsState(
                    getTabTextColor(baseDropdownComponent.variant, baseDropdownComponent.selected),
                    animationSpec = tween(300)
                )
                Text(
                    text = baseDropdownComponent.label.replace("\n", " "),
                    style = if (baseDropdownComponent.variant == TabVariant.Contained) {
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
}

@Composable
private fun DropdownMenu(
    offset: IntOffset,
    isExpanded: MutableState<Boolean>,
    items: List<DropdownItem>,
    onDropdownItemSelected: (DropdownItem) -> Unit
) {
    val scope = rememberCoroutineScope()
    var isContentVisible by remember { mutableStateOf(false) }

    val onDismiss = remember<() -> Unit> {
        {
            scope.launch {
                isContentVisible = false
                delay(animationDuration) // needed to properly show closing animation
                isExpanded.value = false
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        //delay(popupSetupDelay)
        isContentVisible = true
    }

    Popup(onDismissRequest = onDismiss, offset = offset) {
        AnimatedVisibility(
            visible = isContentVisible,
            enter = expandVertically(dropdownAnimation),
            exit = shrinkVertically(dropdownAnimation)
        ) {
            Card(
                elevation = Elevation.Solid.Medium,
                cornerRadius = CornerRadius.SMALL
            ) {
                Column(
                    modifier = Modifier
                        .requiredSizeIn(
                            minWidth = minDropdownWidth,
                            maxWidth = maxDropdownWidth,
                            maxHeight = maxDropdownHeight
                        )
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.size(8.dp))
                    for (item in items) {
                        DropdownItem(item) {
                            onDismiss()
                            onDropdownItemSelected(it)
                        }
                    }
                    Spacer(Modifier.size(8.dp))
                }
            }
        }
    }
}

@Composable
private fun DropdownItem(item: DropdownItem, onItemClick: (DropdownItem) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(item.disabled)
            .clickable(enabled = !item.disabled) {
                onItemClick(item)
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

public sealed class BaseDropdownComponent {
    public data class Button(
        val label: String,
        val startIcon: FlamingoIcon? = null,
        val size: ButtonSize = ButtonSize.MEDIUM,
        val color: ButtonColor = ButtonColor.Primary,
        val loading: Boolean = false,
        val variant: ButtonVariant = ButtonVariant.CONTAINED,
        val disabled: Boolean = false,
        val fillMaxWidth: Boolean = false,
        val widthPolicy: ButtonWidthPolicy = ButtonWidthPolicy.MULTILINE
    ) : BaseDropdownComponent()

    public data class IconButton(
        val icon: FlamingoIcon,
        val contentDescription: String? = null,
        val size: IconButtonSize = IconButtonSize.MEDIUM,
        val variant: IconButtonVariant = IconButtonVariant.CONTAINED,
        val color: IconButtonColor = IconButtonColor.DEFAULT,
        val shape: IconButtonShape = IconButtonShape.CIRCLE,
        val loading: Boolean = false,
        val disabled: Boolean = false
    ) : BaseDropdownComponent()

    public data class Chip(
        val label: String,
        val startIcon: FlamingoIcon? = null,
        val selected: Boolean = false,
        val disabled: Boolean = false
    ) : BaseDropdownComponent()

    internal data class Tab(
        val label: String,
        val variant: TabVariant = TabVariant.Contained,
        val disabled: Boolean = false,
        val selected: Boolean = false,
        val onTabSelect: () -> Unit
    ) : BaseDropdownComponent()
}

public data class DropdownItem(
    val label: String,
    val icon: FlamingoIcon? = null,
    val disabled: Boolean = false
)

private const val popupSetupDelay = 10L
private const val animationDuration = 100L
private val dropdownAnimation = spring(
    stiffness = Spring.StiffnessMedium,
    visibilityThreshold = IntSize.VisibilityThreshold
)
private val minDropdownWidth = 192.dp
private val maxDropdownWidth = 224.dp
private val maxDropdownHeight = 288.dp