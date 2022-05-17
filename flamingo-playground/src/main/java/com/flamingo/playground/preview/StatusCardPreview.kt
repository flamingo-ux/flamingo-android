package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.Action
import com.flamingo.components.ActionGroup
import com.flamingo.components.StatusCard
import com.flamingo.components.StatusCardImage
import com.flamingo.playground.boast
import com.flamingo.loremIpsum

@Composable
@Preview
@Suppress("FunctionNaming", "MagicNumber")
fun StatusCardPreview() {
    val context = LocalContext.current
    StatusCard(
        image = StatusCardImage.Success,
        title = loremIpsum(3),
        description = loremIpsum(8),
        actions = ActionGroup(Action("Button", onClick = boast("Action click"))),
        contentDescription = "demo"
    )
}
