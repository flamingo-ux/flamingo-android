package com.flamingo.playground.components.dropdown

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.components.IconButtonColor
import com.flamingo.components.IconButtonSize
import com.flamingo.components.IconButtonVariant
import com.flamingo.components.dropdown.BaseDropdownComponent
import com.flamingo.components.dropdown.Dropdown
import com.flamingo.components.dropdown.DropdownItem
import com.flamingo.theme.FlamingoIcon

@Composable
fun ButtonDropdownWithChangingLabel() {
    var label by remember { mutableStateOf("button") }
    var icon: FlamingoIcon? by remember { mutableStateOf(null) }
    Dropdown(
        modifier = Modifier.padding(start = 16.dp),
        baseDropdownComponent = BaseDropdownComponent.Button(label, icon),
        items = listOf(
            DropdownItem("item 1"),
            DropdownItem("item 2", Flamingo.icons.Bell),
            DropdownItem("long long long item")
        ).filter { it.label != label },
        onDropdownItemSelected = {
            label = it.label
            icon = it.icon
        }
    )
}

@Composable
fun IconButtonDropdown() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 16.dp)
    ) {
        Dropdown(
            baseDropdownComponent = BaseDropdownComponent.IconButton(Flamingo.icons.MoreHorizontal),
            items = listOf(
                DropdownItem("item 1"),
                DropdownItem("item 2", Flamingo.icons.Bell, true),
                DropdownItem("item 3", Flamingo.icons.Bell),
                DropdownItem("long long long item")
            ),
            onDropdownItemSelected = {}
        )

        Dropdown(
            baseDropdownComponent = BaseDropdownComponent.IconButton(
                icon = Flamingo.icons.MoreVertical,
                size = IconButtonSize.LARGE,
                color = IconButtonColor.PRIMARY
            ),
            items = listOf(
                DropdownItem("item 1"),
                DropdownItem("item 2", Flamingo.icons.Bell, true),
                DropdownItem("long long long item"),
                DropdownItem("second long long item", Flamingo.icons.Edit)
            ),
            onDropdownItemSelected = {}
        )

        Dropdown(
            baseDropdownComponent = BaseDropdownComponent.IconButton(
                icon = Flamingo.icons.Menu,
                variant = IconButtonVariant.TEXT,
                color = IconButtonColor.WARNING
            ),
            items = listOf(
                DropdownItem("item 1"),
                DropdownItem("item 2", Flamingo.icons.Bell, true),
                DropdownItem("item 3", Flamingo.icons.Bell),
                DropdownItem("long long long item")
            ),
            onDropdownItemSelected = {}
        )
    }
}

@Composable
fun ChipDropdown() {
    var label by remember { mutableStateOf("button") }
    var icon: FlamingoIcon? by remember { mutableStateOf(null) }
    Dropdown(
        modifier = Modifier.padding(start = 16.dp),
        baseDropdownComponent = BaseDropdownComponent.Chip(label, icon),
        items = listOf(
            DropdownItem("item 1"),
            DropdownItem("item 2", Flamingo.icons.Bell),
            DropdownItem("long long long item")
        ).filter { it.label != label },
        onDropdownItemSelected = {
            label = it.label
            icon = it.icon
        }
    )
}
