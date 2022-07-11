package com.flamingo.playground.components.widgetcardgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.flamingo.Flamingo
import com.flamingo.components.Text
import com.flamingo.components.widgetcard.WidgetCardGroupChildrenRange
import com.flamingo.demoapi.FlamingoComponentDemoName
import com.flamingo.demoapi.TypicalUsageDemo
import com.flamingo.playground.Compose
import com.flamingo.playground.preview.WidgetCardGroupPreview

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
            item { Text(text = "Phone Layout", style = Flamingo.typography.h1) }
            items(WidgetCardGroupChildrenRange.toList()) { n ->
                ForceScreenWidth(width = phoneDp) {
                    WidgetCardGroupDemo(n)
                }
            }

            item { Text(text = "Tablet Layout", style = Flamingo.typography.h1) }
            items(WidgetCardGroupChildrenRange.toList()) { n ->
                ForceScreenWidth(width = tabletDp) {
                    WidgetCardGroupDemo(n)
                }
            }
        }
    }
}

private val tabletDp = 840.dp
private val phoneDp = 400.dp

@Composable
private fun WidgetCardGroupDemo(it: Int) =
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Number of WidgetCards: $it")
        WidgetCardGroupPreview(it)
    }

@Composable
private fun ForceScreenWidth(width: Dp, content: @Composable () -> Unit) {
    BoxWithConstraints {
        val currentDensity = LocalDensity.current
        val tabletDensity = Density(
            density = maxWidth * currentDensity.density / width,
            fontScale = currentDensity.fontScale
        )
        CompositionLocalProvider(LocalDensity provides tabletDensity) {
            content()
        }
    }
}
