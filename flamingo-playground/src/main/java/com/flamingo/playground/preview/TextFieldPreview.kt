package com.flamingo.playground.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.Flamingo
import com.flamingo.components.EdgeItem
import com.flamingo.components.TextField
import com.flamingo.playground.boast

@Preview
@Composable
@Suppress("FunctionNaming")
fun TextFieldPreview() {
    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            label = "Label",
            placeholder = "Placeholder",
            helperText = "Helper text",
            required = true,
            maxCharNumber = 12,
            value = value,
            onValueChange = { value = it },
            edgeItem = EdgeItem.TextFieldIcon(
                icon = Flamingo.icons.Watch,
                onClick = boast("Click"),
                contentDescription = "send"
            )
        )
    }
}
