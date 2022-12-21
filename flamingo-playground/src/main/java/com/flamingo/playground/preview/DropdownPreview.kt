package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.Flamingo
import com.flamingo.components.dropdown.BaseComponent
import com.flamingo.components.dropdown.Dropdown
import com.flamingo.components.dropdown.DropdownItem

@Preview
@Composable
fun DropdownPreview() {
    Dropdown(
        baseComponent = BaseComponent.Button("dropdown"),
        items = listOf(
            DropdownItem("item 1", Flamingo.icons.Bell),
            DropdownItem("long long long long long long long long item", Flamingo.icons.Aperture),
            DropdownItem("item 3"),
            DropdownItem("item 4 long long long long long long long long"),
            DropdownItem("item 5", Flamingo.icons.Bell),
            DropdownItem("item 6", disabled = true),
            DropdownItem("item 7", Flamingo.icons.Share),
            DropdownItem("item 8", Flamingo.icons.Bell),
            DropdownItem("item 9", Flamingo.icons.Bell),
            DropdownItem("item 10", Flamingo.icons.Bell, true),
            DropdownItem("item 11", Flamingo.icons.Bell),
            DropdownItem("item 12", Flamingo.icons.Bell),
            DropdownItem("item 13", Flamingo.icons.Bell),
        )
    ) {

    }
}