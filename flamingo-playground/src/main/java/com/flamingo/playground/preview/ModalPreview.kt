package com.flamingo.playground.preview

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flamingo.components.Text
import com.flamingo.components.button.Button
import com.flamingo.components.modal.ButtonParams
import com.flamingo.components.modal.Modal
import com.flamingo.loremIpsum

@Preview
@Composable
fun ModalPreview() {
    var isVisible by remember { mutableStateOf(false) }

    Button(onClick = { isVisible = true }, label = "Click to show Modal")

    Modal(
        isVisible = isVisible,
        title = "Modal screen",
        hasCloseButton = true,
        onDismissRequest = { isVisible = false },
        primaryButtonParams = ButtonParams("Primary") { },
        secondaryButtonParams = ButtonParams("Secondary") { }
    ) {
        Text(modifier = Modifier.padding(horizontal = 16.dp), text = loremIpsum(200))
    }
}