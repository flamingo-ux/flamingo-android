package com.flamingo

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/**
 * Applies alpha to make content appear in disabled ui state.
 */
internal fun Modifier.alpha(disabled: Boolean, animate: Boolean = false): Modifier {
    return if (animate) composed {
        val alpha by animateFloatAsState(
            targetValue = if (disabled) ALPHA_DISABLED else 1f,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        )
        graphicsLayer { this.alpha = alpha }
    } else alpha(if (disabled) ALPHA_DISABLED else 1f)
}

internal fun Modifier.contentDescriptionSemantics(contentDescription: String?): Modifier {
    return if (contentDescription != null) this then semantics {
        this.contentDescription = contentDescription
    } else this
}
