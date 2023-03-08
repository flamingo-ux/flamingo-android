package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.chipgroup.ChipData
import com.flamingo.components.chipgroup.ChipGroup

@Preview
@Composable
fun ChipGroupPreview() {
    var selectedIndex by remember { mutableStateOf(0) }
    ChipGroup(
        chips = listOf(
            ChipData(
                label = "long long long long long long long long long long chip",
                selected = selectedIndex == 0,
                onClick = { selectedIndex = 0 }),
            ChipData(
                label = "long chip",
                selected = selectedIndex == 1,
                onClick = { selectedIndex = 1 }),
            ChipData(
                label = "long long chip",
                selected = selectedIndex == 2,
                onClick = { selectedIndex = 2 }),
            ChipData(
                label = "chip",
                selected = selectedIndex == 3,
                onClick = { selectedIndex = 3 }),
            ChipData(
                label = "long long chip",
                selected = selectedIndex == 4,
                onClick = { selectedIndex = 4 }),
            ChipData(
                label = "long chip",
                selected = selectedIndex == 5,
                onClick = { selectedIndex = 5 }),
        ),
        label = "label",
        required = false,
        disabled = false,
    )
}