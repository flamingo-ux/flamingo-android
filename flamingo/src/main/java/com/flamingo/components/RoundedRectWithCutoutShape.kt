package com.flamingo.components

import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.flamingo.InternalComponents
import com.flamingo.annotations.FlamingoComponent
import kotlin.math.sqrt

/**
 * Used to add [Indicator] to [Avatar] and [IconButton]. Can be used ONLY inside of the design
 * system.
 *
 * Consists of [RoundedRectWithCutoutShape] and [circleOffset], which SHOULD be used together to
 * create a circle-shaped cutout in a rounded rectangle.
 */
@FlamingoComponent(
    displayName = "Rounded Rectangle with Cutout",
    preview = "com.flamingo.playground.preview.RoundedRectWithCutoutPreview",
    figma = "", specification = "",
    theaterPackage = "com.flamingo.playground.components.roundedrectwithcutout.TheaterPkg",
    demo = ["com.flamingo.playground.components.roundedrectwithcutout.StatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun InternalComponents.RoundedRectWithCutoutShape(nothing: Nothing) {
    error("This function is here just to create a component page in the playground app. Usable " +
            "declarations are right below.")
}

internal enum class CutoutPlacement {
    TopStart, TopEnd, BottomStart, BottomEnd,
    ;

    public val isEnd: Boolean get() = this == TopEnd || this == BottomEnd
    public val isStart: Boolean get() = this == TopStart || this == BottomStart

    public val isTop: Boolean get() = this == TopStart || this == TopEnd
    public val isBottom: Boolean get() = this == BottomStart || this == BottomEnd
}

/**
 * Used to add [Indicator] to [Avatar] and [IconButton]. Can be used ONLY inside of the design
 * system.
 *
 * Consists of [RoundedRectWithCutoutShape] and [circleOffset], which SHOULD be used together to
 * create a circle-shaped cutout in a rounded rectangle.
 */
@Immutable
internal class RoundedRectWithCutoutShape(
    private val cornerRadius: Dp,
    private val cutoutRadius: Dp,
    private val cutoutPlacement: CutoutPlacement,
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val cornerRadius = with(density) { cornerRadius.coerceAtLeast(0.dp).toPx() }
        val cutoutRadius = with(density) { cutoutRadius.coerceAtLeast(0.dp).toPx() }

        val path = Path().apply {
            // diameter of an inscribed circle can't be larger than the rect's minDimension
            val cornerRadius =
                if (cornerRadius * 2 > size.minDimension) size.minDimension else cornerRadius
            addRoundRect(RoundRect(rect = size.toRect(), cornerRadius = CornerRadius(cornerRadius)))
            if (cutoutRadius == 0f) {
                close()
                return@apply
            }
            val cutoutPath = Path().apply {
                addOval(Rect(
                    center = circleCenterOffset(size, cornerRadius, cutoutPlacement),
                    radius = cutoutRadius
                ))
            }
            op(this, cutoutPath, PathOperation.Difference)
            close()
        }
        return Outline.Generic(path)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoundedRectWithCutoutShape

        if (cornerRadius != other.cornerRadius) return false
        if (cutoutRadius != other.cutoutRadius) return false
        if (cutoutPlacement != other.cutoutPlacement) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cornerRadius.hashCode()
        result = 31 * result + cutoutRadius.hashCode()
        result = 31 * result + cutoutPlacement.hashCode()
        return result
    }
}

/**
 * Apply this modifier to the circle that needs to be placed in the center of the cutout. Used in
 * conjunction with [RoundedRectWithCutoutShape]. See samples for more info.
 *
 * @param rectSize size of the rectangle with a cutout
 */
@Stable
internal fun Modifier.circleOffset(
    rectSize: DpSize,
    cornerRadius: Dp,
    circleRadius: Dp,
    cutoutPlacement: CutoutPlacement,
): Modifier = offset {
    val offset = circleCenterOffset(
        rectSize = rectSize.toSize(),
        cornerRadius = cornerRadius.toPx(),
        cutoutPlacement = cutoutPlacement
    ) - Offset(circleRadius.toPx(), circleRadius.toPx())

    IntOffset(offset.x.toInt(), offset.y.toInt())
}

/**
 * 🤓 https://todo.ru/x/Kg81QQE
 */
@Stable
private fun circleCenterOffset(
    rectSize: Size,
    cornerRadius: Float,
    cutoutPlacement: CutoutPlacement,
): Offset {
    val halfSize = rectSize / 2f
    // radius of an inscribed circle can't be larger than the half of the rect's minDimension
    val cornerRadius =
        if (cornerRadius > halfSize.minDimension) halfSize.minDimension
        else cornerRadius.coerceAtLeast(0f)
    val cutoutOffsetMultiplier = 1 - (sqrt(2f) / 2)
    val offsetXFromZero = cornerRadius * cutoutOffsetMultiplier
    return Offset(
        x = if (cutoutPlacement.isStart) offsetXFromZero else rectSize.width - offsetXFromZero,
        y = if (cutoutPlacement.isTop) offsetXFromZero else rectSize.height - offsetXFromZero,
    )
}
