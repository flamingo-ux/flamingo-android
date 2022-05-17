@file:Suppress("MagicNumber")

package com.flamingo.playground.theater

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import com.flamingo.playground.R
import com.theater.Stage

/**
 * Logo of the flamingo design system that will be show on top of the [Stage], when
 * [FlamingoStage.logo] is non-null.
 */
data class FlamingoLogo(
    val withCircle: Boolean = false,
    val alignment: Alignment = BiasAlignment(0.95f, 0.9f),
    val alpha: Float = 0.4f,
    val fractionOfHeight: Float = 0.1f,
) {
    @Suppress("FunctionNaming")
    @Composable
    fun BoxScope.Logo() {
        val id = if (withCircle) {
            R.drawable.flamingo_logo_gray
        } else {
            R.drawable.flamingo_logo_gray_no_circle
        }
        Image(
            modifier = Modifier
                .fillMaxHeight(fractionOfHeight)
                .align(alignment)
                .alpha(alpha),
            painter = painterResource(id = id),
            contentDescription = null
        )
    }
}
