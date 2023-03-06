package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.Flamingo
import com.flamingo.components.IconButtonColor
import com.flamingo.components.IconButtonIndicator
import com.flamingo.components.IndicatorColor
import com.flamingo.components.fab.FloatingActionButton
import com.flamingo.playground.boast

@Composable
@Preview
fun FloatingActionButtonPreview() {
    FloatingActionButton(
        onClick = boast("Click"),
        icon = Flamingo.icons.Info,
        color = IconButtonColor.PRIMARY,
        indicator = IconButtonIndicator(color = IndicatorColor.WARNING),
        contentDescription = "preview info",
    )
}