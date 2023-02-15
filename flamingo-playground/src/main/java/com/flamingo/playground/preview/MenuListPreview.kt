package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.Flamingo
import com.flamingo.components.menulist.MenuList
import com.flamingo.components.menulist.MenuListItem
import com.flamingo.components.menulist.MenuListSubItem
import com.flamingo.components.menulist.TextButtonParams

@Preview
@Composable
fun MenuListPreview() {
    MenuList(
        icon = Flamingo.icons.Menu,
        disabled = false,
        dialogTitle = "dialog title",
        items = listOf(
            //MenuListItem(label = "first"),
            MenuListItem(
                label = "second",
                list = listOf(
                    MenuListSubItem("sub item 1"),
                    MenuListSubItem("long long long long long long long long long long long sub item 2", disabled = true),
                    MenuListSubItem("sub item 3")
                )
            ),
            MenuListItem(label = "first"),
            MenuListItem(icon = Flamingo.icons.Menu, label = "third"),
            MenuListItem(
                label = "fourth", list = listOf(
                    MenuListSubItem("sub item 1"),
                    MenuListSubItem("long long long long long long long long long long long long long long long long long long long long long sub item 2"),
                    MenuListSubItem("sub item 3")
                )
            ),
            MenuListItem(label = "fifth"),
        ),
        textButtonParams = TextButtonParams(
            label = "asdsadasd",

            ) {

        },
        onItemSelected = {

        }
    )
}