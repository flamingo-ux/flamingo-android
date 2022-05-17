package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.RatingToggleButton

@Preview
@Composable
@Suppress("FunctionNaming")
fun RatingToggleButtonPreview() {
    var state by remember { mutableStateOf(true) }
    RatingToggleButton(
        checked = state,
        onCheckedChange = { state = it },
    )
}
