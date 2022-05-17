package com.flamingo.playground.overlay

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Color
import com.flamingo.overlay.DebugOverlay

class DebugOverlayConfigImpl(
    override val borderStroke: BorderStroke,
    override val backgroundColor: Color,
    override val text: String,
) : DebugOverlay.Config
