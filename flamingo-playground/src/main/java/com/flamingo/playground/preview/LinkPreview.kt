package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.Flamingo
import com.flamingo.playground.utils.Boast

@Composable
@Preview
fun LinkPreview() {
    val context = LocalContext.current
    com.flamingo.components.Link(
        label = "LongLongLongLink.com",
        onClick = { Boast.showText(context, "Clicked on $it") },
        startIcon = Flamingo.icons.Link,
        endIcon = Flamingo.icons.Command
    )
}