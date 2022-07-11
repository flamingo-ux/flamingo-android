package com.flamingo.playground.components.widgetcardgroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.components.Text
import com.flamingo.components.widgetcard.WidgetCard
import com.flamingo.components.widgetcard.WidgetCardGroup
import com.flamingo.components.widgetcard.WidgetCardGroupChildrenRange

@Composable
@Preview(showBackground = true, heightDp = 1000)
fun Sample2() {
    // Label 1, Label 2, ... Label 8
    val labels = remember { (1..8).map { "Label " + it } }
    // this is the key to displaying unlimited number of WidgetCards
    val groups = remember { labels.chunked(WidgetCardGroupChildrenRange.last) }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        itemsIndexed(groups) { index, list ->
            Text("Group " + index, style = Flamingo.typography.h3)
            Spacer(modifier = Modifier.height(16.dp))
            WidgetCardGroup {
                check(list.size in WidgetCardGroupChildrenRange)
                list.forEach { label -> WidgetCard(title = label) }
            }
        }
    }
}
