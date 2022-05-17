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
    "LongMethod",
    "ComplexMethod",
    "SpacingAroundParens",
    "IllegalIdentifier",
    "EnumEntryName",
    "NonAsciiCharacters",
    "EnumNaming",
)

package com.flamingo.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flamingo.annotations.DelicateFlamingoApi
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.CircularLoaderColor.DEFAULT
import com.flamingo.components.CircularLoaderColor.ERROR
import com.flamingo.components.CircularLoaderColor.PRIMARY
import com.flamingo.components.CircularLoaderColor.RATING
import com.flamingo.components.CircularLoaderColor.WARNING
import com.flamingo.components.CircularLoaderColor.WHITE
import com.flamingo.components.CircularLoaderColor.`🌈`
import com.flamingo.rainbowColorAnim
import com.flamingo.Flamingo
import com.flamingo.annotations.UsedInsteadOf

/**
 * Indeterminate circular progress indicator. Progress indicators express an unspecified wait time
 * or display the length of a process.
 */
@FlamingoComponent(
    displayName = "Circular Loader",
    preview = "com.flamingo.playground.preview.CircularLoaderPreview",
    figma = "https://todo.com/file/qVO8jDuABDK9vsuLqRXeMx?node-id=6485%3A1143",
    specification = "https://todo.com/x/qALDOAE",
    demo = ["com.flamingo.playground.components.CircularLoaderStatesPlayroom"],
    supportsWhiteMode = true,
)
@UsedInsteadOf("androidx.compose.material.CircularProgressIndicator")
@Preview
@Composable
public fun CircularLoader(
    size: CircularLoaderSize = CircularLoaderSize.MEDIUM,
    color: CircularLoaderColor = if (Flamingo.isWhiteMode) WHITE else DEFAULT
) {
    val color = with(Flamingo.colors) {
        when (color) {
            WARNING -> warning
            ERROR -> error
            PRIMARY -> primary
            WHITE -> Flamingo.palette.white
            DEFAULT -> textSecondary
            RATING -> rating
            `🌈` -> rainbowColorAnim().value
        }
    }
    CircularLoader(size, color)
}

@Composable
internal fun CircularLoader(
    size: CircularLoaderSize = CircularLoaderSize.MEDIUM,
    color: Color
): Unit = FlamingoComponentBase {
    val rotation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing))
    )
    val brush = Brush.sweepGradient(
        0f to Color.Transparent,
        1f to color
    )
    with(LocalDensity.current) {
        val lineThicknessPx = size.lineThickness.toPx()
        val topLeft = lineThicknessPx / 2
        val topLeftOffset = Offset(topLeft, topLeft)

        val arcWidth = size.size.toPx() - topLeft * 2
        val arcSize = Size(arcWidth, arcWidth)

        val arcStyle = Stroke(width = lineThicknessPx, cap = StrokeCap.Round)

        Canvas(modifier = Modifier
            .progressSemantics()
            .requiredSize(size.size)
            .graphicsLayer { rotationZ = rotation }
        ) {
            drawArc(
                brush = brush, topLeft = topLeftOffset, size = arcSize,
                startAngle = 5f, sweepAngle = 345f, useCenter = false, style = arcStyle,
            )
        }
    }
}

public enum class CircularLoaderSize(internal val size: Dp, internal val lineThickness: Dp) {
    SMALL(size = 16.dp, lineThickness = 2.dp),
    MEDIUM(size = 24.dp, lineThickness = 2.dp),
    LARGE(size = 40.dp, lineThickness = 3.dp),
}

public enum class CircularLoaderColor {
    WARNING, ERROR, PRIMARY, WHITE, DEFAULT, RATING,

    @DelicateFlamingoApi
    `🌈`
}
