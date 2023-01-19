package com.flamingo.components.chipgroup

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
internal fun ChipGroupContentLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Layout(content, modifier) { measurables, constraints ->
    layout(width = 0, height = 0) { //todo

    }
}
