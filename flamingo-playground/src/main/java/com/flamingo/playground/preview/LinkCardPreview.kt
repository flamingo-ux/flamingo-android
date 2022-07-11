package com.flamingo.playground.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.components.LinkCard
import com.flamingo.components.widgetcard.WidgetCardSize
import com.flamingo.loremIpsum
import com.flamingo.playground.boast
import com.flamingo.playground.internalComponents

@Composable
@Preview(widthDp = 200, heightDp = 200, showBackground = true)
@Suppress("FunctionNaming", "MagicNumber")
fun LinkCardPreview() {
    Box(modifier = Modifier.requiredSize(168.dp)) {
        internalComponents.LinkCard(
            text = loremIpsum(3),
            size = WidgetCardSize.SQUARE,
            icon = Flamingo.icons.Plus,
            onClick = boast()
        )
    }
}
