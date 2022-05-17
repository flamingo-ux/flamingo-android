package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.Checkbox
import com.flamingo.playground.showBoast

@Preview
@Composable
@Suppress("FunctionNaming")
fun CheckboxPreview() {
    val context = LocalContext.current
    var checked by remember { mutableStateOf(true) }
    Checkbox(checked, onCheckedChange = {
        checked = it
        context.showBoast("Click: checked = $checked")
    })
}
