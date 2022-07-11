package com.flamingo.components.widgetcard

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

public enum class WidgetCardSize(internal val aspectRatio: Float?) {
    SQUARE(aspectRatio = 1f / 1f),
    MONOLITH(aspectRatio = 3f / 4f),
    BRICK(aspectRatio = 2f / 1f),
    FIXED_HEIGHT_BRICK(aspectRatio = null),
    ;
}

internal fun Modifier.cardSize(size: WidgetCardSize): Modifier =
    if (size.aspectRatio != null) this.aspectRatio(size.aspectRatio)
    else this.fillMaxWidth().requiredHeight(168.dp)