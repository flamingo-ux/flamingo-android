@file:Suppress("SpacingAroundParens", "FunctionNaming")

package com.theater

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.theater.Stage.Companion.DefaultStage

/**
 * Wraps the content of the play. Can be used to apply an overlay or call theme function
 * (like [MaterialTheme]).
 *
 * If nothing like that is needed, use [DefaultStage].
 */
public interface Stage {
    @Composable
    public fun BoxScope.Stage(content: @Composable () -> Unit) {
        content()
    }

    public companion object {
        public val DefaultStage: Stage = object : Stage {}
    }
}
