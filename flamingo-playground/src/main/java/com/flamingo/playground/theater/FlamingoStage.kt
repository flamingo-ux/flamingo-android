@file:Suppress("SpacingAroundParens")

package com.flamingo.playground.theater

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.flamingo.theme.FlamingoTheme
import com.flamingo.Flamingo
import com.theater.Stage

open class FlamingoStage(
    val logo: FlamingoLogo? = FlamingoLogo(),
) : Stage {
    val fontScale = Animatable(1f)
    var darkTheme: Boolean by mutableStateOf(false)
    val logoVisibility = Animatable(1f)
    private val logoVisibilityValue by logoVisibility.asState()

    @Composable
    override fun BoxScope.Stage(content: @Composable () -> Unit) = FlamingoTheme(darkTheme) {
        val density = LocalDensity.current
        val newDensity = Density(density.density, fontScale.value)
        CompositionLocalProvider(LocalDensity provides newDensity) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Flamingo.colors.background)
            ) {
                content()
            }
        }
        if (logo != null) Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = logoVisibilityValue }
        ) {
            with(logo) { Logo() }
        }
    }
}
