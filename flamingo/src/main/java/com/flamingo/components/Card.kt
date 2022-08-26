@file:Suppress(
    "ModifierOrdering",
    "CommentSpacing",
    "NoBlankLineBeforeRbrace",
    "ModifierOrdering",
    "NoConsecutiveBlankLines",
    "MagicNumber",
    "FunctionName",
    "MatchingDeclarationName",
    "LongParameterList",
    "SpacingAroundParens"
)

package com.flamingo.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.Shadow
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.internalComponents

/**
 * Changes of the [elevation] are animated (including a background color in dark theme).
 *
 * To make [Card] clickable, see this sample to avoid a __common__ mistake:
 * @sample com.flamingo.playground.components.card.ClickableCard
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.CardPreview",
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx?node-id=1026%3A1",
    specification = "https://confluence.companyname.ru/x/WiTQgAE",
    demo = [
        "com.flamingo.playground.components.card.CardStatesPlayroom",
        "com.flamingo.playground.components.card.ClickableCardTypicalUsage",
    ],
    supportsWhiteMode = false,
)
@UsedInsteadOf("androidx.compose.material.Card", "androidx.compose.material.Surface")
@Composable
public fun Card(
    elevation: Elevation = Elevation.No,
    cornerRadius: CornerRadius = CornerRadius.MEDIUM,
    content: @Composable () -> Unit,
): Unit = FlamingoComponentBase {
    val backgroundColor = if (Flamingo.colors.isLight) {
        Flamingo.colors.background
    } else when (elevation) {
        Elevation.No -> Flamingo.colors.background
        Elevation.Solid.Small -> Flamingo.palette.grey950
        Elevation.Solid.Medium -> Flamingo.palette.grey900
        Elevation.Solid.Large -> Flamingo.palette.grey850
        Elevation.Transparent.Small -> Flamingo.palette.white.copy(alpha = 0.08f)
        Elevation.Transparent.Medium -> Flamingo.palette.white.copy(alpha = 0.12f)
        Elevation.Transparent.Large -> Flamingo.palette.white.copy(alpha = 0.20f)
    }.let { animateColorAsState(it, animationSpec = spring(stiffness = 600f)).value }

    internalComponents.Shadow(
        elevation = animateDpAsState(elevation.dp).value,
        opacity = 0.85f,
        shape = cornerRadius.shape
    ) {
        Box(
            modifier = Modifier
                .background(color = backgroundColor, shape = cornerRadius.shape)
                .clip(cornerRadius.shape)
        ) {
            content()
        }
    }
}

public sealed class Elevation {
    internal abstract val dp: Dp

    public object No : Elevation() {
        override val dp: Dp = 0.dp
    }

    /**
     * Should be used by default
     */
    public sealed class Solid(override val dp: Dp) : Elevation() {
        public object Small : Solid(3.dp)
        public object Medium : Solid(6.dp)
        public object Large : Solid(12.dp)
    }

    public sealed class Transparent(override val dp: Dp) : Elevation() {
        public object Small : Solid(3.dp)
        public object Medium : Solid(6.dp)
        public object Large : Solid(12.dp)
    }
}
