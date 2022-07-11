package com.flamingo.playground.components.widgetcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.flamingo.components.widgetcard.WidgetCardSize
import com.flamingo.demoapi.FlamingoComponentDemoName
import com.flamingo.demoapi.TypicalUsageDemo
import com.flamingo.playground.Compose
import com.flamingo.playground.preview.WidgetCardPreviewImpl

@TypicalUsageDemo
@FlamingoComponentDemoName("Examples")
class Demo : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ) = Compose {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Box(Modifier.requiredHeight(168.dp)) { WidgetCardPreviewImpl(loading = true) } }
            WidgetCardSize.values().forEach { size ->
                item {
                    Box(Modifier.requiredHeight(168.dp)) {
                        WidgetCardPreviewImpl(size = size, textAtTheTop = true)
                    }
                }
                item {
                    Box(Modifier.requiredHeight(168.dp)) {
                        WidgetCardPreviewImpl(size = size, textAtTheTop = false)
                    }
                }
            }
        }
    }
}
