package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.Flamingo
import com.flamingo.playground.boast
import com.flamingo.playground.utils.Boast

@Composable
@Preview
fun LinkPreview() {
    com.flamingo.components.Link(
        label = "LongLongLongLink.com",
        onClick = boast("click"),
        startIcon = Flamingo.icons.Link,
        endIcon = Flamingo.icons.Command
    )
}