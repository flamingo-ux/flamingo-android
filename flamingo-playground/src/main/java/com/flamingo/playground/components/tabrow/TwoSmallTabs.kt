@file:Suppress("FunctionNaming")

package com.flamingo.playground.components.tabrow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.tabrow.TabRow

@Preview
@Composable
fun TwoSmallTabs() {
    var index by rememberSaveable { mutableStateOf(0) }
    TabRow(tabs = listOf("One", "Two"), selectedTabIndex = index, onTabSelect = { index = it })
}
