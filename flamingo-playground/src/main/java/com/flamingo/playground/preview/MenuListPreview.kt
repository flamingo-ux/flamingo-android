package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.Flamingo
import com.flamingo.components.menulist.MenuList
import com.flamingo.components.menulist.MenuListItem
import com.flamingo.components.menulist.MenuListItemParams
import com.flamingo.components.menulist.SelectedMenuListItem
import com.flamingo.components.menulist.TextButtonParams

@Preview
@Composable
fun MenuListPreview() {
    var selectedItem by remember {
        mutableStateOf(
            SelectedMenuListItem(
                MenuListItemParams(label = "second"),
                null
            )
        )
    }
    MenuList(
        disabled = false,
        dialogTitle = "dialog title",
        items = listOf(
            MenuListItem(params = MenuListItemParams(label = "first")),
            MenuListItem(
                params = MenuListItemParams(label = "second"),
                list = listOf(
                    MenuListItemParams("sub item 1"),
                    MenuListItemParams(
                        "long long long long long long long long long long long sub item 2",
                        disabled = true
                    ),
                    MenuListItemParams("sub item 3")
                )
            ),
            MenuListItem(params = MenuListItemParams(label = "third", icon = Flamingo.icons.Menu)),
            MenuListItem(
                params = MenuListItemParams(label = "fourth"), list = listOf(
                    MenuListItemParams("sub item 1"),
                    MenuListItemParams("long long long long long long long long long long long long long long long long long long long long long sub item 2"),
                    MenuListItemParams("sub item 3")
                )
            ),
            MenuListItem(params = MenuListItemParams(label = "fifth")),
        ),
        selectedItem = selectedItem,
        textButtonParams = TextButtonParams(
            label = "asdsadasd",

            ) {

        },
        onItemSelected = {
            selectedItem = it
        }
    )
}