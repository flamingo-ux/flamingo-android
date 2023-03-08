@file:Suppress("FunctionNaming")

package com.flamingo.playground.components.tabrow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.BadgeColor
import com.flamingo.components.tabrow.Tab
import com.flamingo.components.tabrow.TabRow
import com.flamingo.components.tabrow.TabVariant

@Preview
@Composable
fun ComplexLinedTabs() {
    var index by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf(
        Tab(label = "One"),
        Tab(label = "Two", disabled = true),
        Tab(label = "Three", badge = Tab.Badge("Badge text", BadgeColor.Primary)),
        Tab(label = "Four", badge = Tab.Badge("Badge text", BadgeColor.Info), disabled = true),
        Tab(label = "Bird"),
    )
    TabRow(
        tabs = tabs,
        selectedTabIndex = index,
        onTabSelect = { index = it },
        variant = TabVariant.Lined,
    )
}
