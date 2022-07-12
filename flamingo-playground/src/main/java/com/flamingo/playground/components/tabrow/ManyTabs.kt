@file:Suppress("FunctionNaming")

package com.flamingo.playground.components.tabrow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.tabrow.TabRow
import com.flamingo.playground.R

@Preview
@Composable
fun ManyTabs() {
    var index by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf(
        "One",
        "Two",
        "Three",
        "Section 420",
        stringResource(R.string.tab_sample_very_long_tab_label),
        "Bird"
    )
    TabRow(tabs = tabs, selectedTabIndex = index, onTabSelect = { index = it })
}
