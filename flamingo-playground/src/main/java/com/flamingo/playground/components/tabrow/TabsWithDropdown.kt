package com.flamingo.playground.components.tabrow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.Flamingo
import com.flamingo.components.BadgeColor
import com.flamingo.components.dropdown.DropdownItem
import com.flamingo.components.tabrow.Tab
import com.flamingo.components.tabrow.TabRow
import com.flamingo.components.tabrow.TabWithDropdown

@Preview
@Composable
fun TabsWithDropdown() {
    var index by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf(
        TabWithDropdown(label = "One"),
        TabWithDropdown(label = "Two", disabled = true),
        TabWithDropdown(label = "Dropdown", dropdownItems = listOf(
            DropdownItem(label = "item 1"),
            DropdownItem(label = "item 2", disabled = true),
            DropdownItem(label = "item with icon", icon = Flamingo.icons.Bell),
        )),
        TabWithDropdown(label = "Three", badge = Tab.Badge("Badge text", BadgeColor.Primary)),
        TabWithDropdown(label = "Four", badge = Tab.Badge("Badge text", BadgeColor.Info), disabled = true),
        TabWithDropdown(label = "Bird"),
    )
    TabRow(tabs = tabs, selectedTabIndex = index, onTabSelect = { newIndex, dropdownIndex ->
        index = newIndex
    })
}