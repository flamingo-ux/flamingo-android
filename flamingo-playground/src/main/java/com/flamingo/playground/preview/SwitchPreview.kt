@file:Suppress("MagicNumber")

package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.Switch
import com.flamingo.playground.showBoast

@Preview
@Composable
@Suppress("FunctionNaming")
fun SwitchPreview() {
    var on by remember { mutableStateOf(true) }
    val context = LocalContext.current
    Switch(checked = on, onCheckedChange = {
        on = it
        context.showBoast("Click: isChecked = $it")
    })
}
