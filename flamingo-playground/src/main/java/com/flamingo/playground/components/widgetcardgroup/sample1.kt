package com.flamingo.playground.components.widgetcardgroup

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.widgetcard.WidgetCard
import com.flamingo.components.widgetcard.WidgetCardGroup
import com.flamingo.loremIpsum

@Composable
@Preview(showBackground = true)
fun Sample1() {
    WidgetCardGroup {
        WidgetCard(title = loremIpsum(3))
        WidgetCard(title = loremIpsum(3))
        WidgetCard(title = loremIpsum(3))
    }
}
