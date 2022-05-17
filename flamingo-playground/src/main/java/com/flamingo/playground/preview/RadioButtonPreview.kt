package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.RadioButton
import com.flamingo.playground.utils.Boast

@Preview
@Composable
@Suppress("FunctionNaming")
fun RadioButtonPreview() {
    val context = LocalContext.current
    var selected by remember { mutableStateOf(true) }
    RadioButton(selected = selected, onClick = {
        selected = !selected
        Boast.showText(context, "Click: selected = $selected")
    })
}
