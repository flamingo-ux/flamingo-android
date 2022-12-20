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
        baseComponent = BaseComponent.Button("sdfdsfsd"),
        items = listOf(
            DropdownItem("sfsdfsdf", Flamingo.icons.Bell),
            DropdownItem("erw", Flamingo.icons.Aperture),
            DropdownItem("sdfsd"),
            DropdownItem("sfsdfsdf"),
            DropdownItem("ffv", Flamingo.icons.Bell),
            DropdownItem("1234254", disabled = true),
            DropdownItem("fgs", Flamingo.icons.Share),
            DropdownItem("fvbhg", Flamingo.icons.Bell),
            DropdownItem("fvbhg", Flamingo.icons.Bell),
            DropdownItem("fvbhg", Flamingo.icons.Bell, true),
            DropdownItem("fvbhg", Flamingo.icons.Bell),
            DropdownItem("fvbhg", Flamingo.icons.Bell),
            DropdownItem("fvbhg", Flamingo.icons.Bell),
            DropdownItem("fvbhg", Flamingo.icons.Bell),
            DropdownItem("fvbhg", Flamingo.icons.Bell),
        )
    ) {

    }
}