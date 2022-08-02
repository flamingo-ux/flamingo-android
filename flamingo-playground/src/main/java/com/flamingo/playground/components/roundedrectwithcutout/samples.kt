package com.flamingo.playground.components.roundedrectwithcutout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.components.Text
import kotlin.reflect.full.declaredMemberProperties

@Composable
@Preview
fun RoundedRectWithCutoutShapeSample() = RoundedRectWithCutoutShapeSample(
    rectSize = DpSize(80.dp, 60.dp),
    cornerRadius = 20.dp,
    circleRadius = 6.dp,
    cutoutRadiusPadding = 2.dp,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RoundedRectWithCutoutShapeSample(
    rectSize: DpSize,
    cornerRadius: Dp,
    circleRadius: Dp,
    cutoutRadiusPadding: Dp,
) = Column {
    cutoutPlacements.chunked(2).forEach { chunk ->
        Row {
            chunk.forEach { cutoutPlacement ->
                Column(modifier = Modifier.padding(circleRadius.coerceAtLeast(4.dp))) {
                    Text(text = cutoutPlacement.name, style = Flamingo.typography.caption2)
                    Sample1(
                        rectSize = rectSize,
                        cornerRadius = cornerRadius,
                        circleRadius = circleRadius,
                        cutoutRadiusPadding = cutoutRadiusPadding,
                        cutoutPlacement = cutoutPlacement,
                    )
                }
            }
        }
    }
}

@Composable
fun Sample1(
    rectSize: DpSize,
    cornerRadius: Dp,
    circleRadius: Dp,
    cutoutRadiusPadding: Dp,
    cutoutPlacement: Enum<*>,
) {
    val roundedRectWithCutoutShape = roundedRectWithCutoutShapeClass.newInstance(
        cornerRadius.value,
        circleRadius.value + cutoutRadiusPadding.value,
        cutoutPlacement,
    ) as Shape

    val circleOffsetModifier = circleOffsetMethod.invoke(
        Unit,
        Modifier,
        dpOffsetPackedValueField.get(rectSize) as Long,
        cornerRadius.value,
        circleRadius.value,
        cutoutPlacement,
    ) as Modifier

    Box(modifier = Modifier
        .requiredSize(rectSize)
        .background(
            color = Flamingo.colors.inverse.backgroundPrimary,
            shape = roundedRectWithCutoutShape
        )
    ) {
        Box(modifier = Modifier
            .then(circleOffsetModifier)
            .requiredSize(circleRadius * 2)
            .background(Flamingo.colors.error, CircleShape)
        )
    }
}

private val cutoutPlacements = Class
    .forName("com.flamingo.components.CutoutPlacement")
    .enumConstants
    .toList() as List<Enum<*>>

private val circleOffsetMethod = Class
    .forName("com.flamingo.components.RoundedRectWithCutoutShapeKt")
    .declaredMethods
    .first { it.name.startsWith("circleOffset") }

private val roundedRectWithCutoutShapeClass = Class
    .forName("com.flamingo.components.RoundedRectWithCutoutShape")
    .declaredConstructors
    .first()

private val dpOffsetPackedValueField = DpSize::class.declaredMemberProperties
    .first { it.name == "packedValue" }
