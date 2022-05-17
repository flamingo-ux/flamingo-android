package com.flamingo.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

public enum class CornerRadius(public val dp: Dp) {
    NO(0.dp), SMALL(8.dp), MEDIUM(12.dp), LARGE(20.dp);

    public val shape: RoundedCornerShape = RoundedCornerShape(dp)
}
