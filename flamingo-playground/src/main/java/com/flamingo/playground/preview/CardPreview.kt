package com.flamingo.playground.preview

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flamingo.components.Card
import com.flamingo.components.Elevation

@Preview
@Composable
@Suppress("FunctionNaming")
fun CardPreview() = Card(elevation = Elevation.Solid.Medium) {
    Spacer(modifier = Modifier.requiredSize(100.dp))
}
