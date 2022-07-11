package com.flamingo.playground.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flamingo.components.Avatar
import com.flamingo.components.AvatarContent
import com.flamingo.components.AvatarIndicator
import com.flamingo.components.AvatarShape
import com.flamingo.components.AvatarSize
import com.flamingo.components.IndicatorColor
import com.flamingo.components.widgetcard.WidgetCard
import com.flamingo.components.widgetcard.WidgetCardSize
import com.flamingo.demoapi.R
import com.flamingo.loremIpsum
import com.flamingo.playground.boast
import com.flamingo.playground.internalComponents

@Composable
@Preview(showBackground = true)
fun WidgetCardPreview() = Box(modifier = Modifier.requiredSize(168.dp)) { WidgetCardPreviewImpl() }

@Composable
@Preview(widthDp = 200, showBackground = true)
private fun Preview1() = WidgetCardPreviewImpl(textAtTheTop = true, size = WidgetCardSize.MONOLITH)

@Composable
@Preview(widthDp = 200, showBackground = true)
private fun Preview2() = WidgetCardPreviewImpl(textAtTheTop = false, size = WidgetCardSize.MONOLITH)

@Composable
@Preview(widthDp = 200, heightDp = 200, showBackground = true)
private fun Preview3() = WidgetCardPreviewImpl(textAtTheTop = true, size = WidgetCardSize.SQUARE)

@Composable
@Preview(widthDp = 200, heightDp = 200, showBackground = true)
private fun Preview4() = WidgetCardPreviewImpl(textAtTheTop = false, size = WidgetCardSize.SQUARE)

@Composable
@Preview(heightDp = 200, showBackground = true)
private fun Preview5() = WidgetCardPreviewImpl(textAtTheTop = true, size = WidgetCardSize.BRICK)

@Composable
@Preview(heightDp = 200, showBackground = true)
private fun Preview6() = WidgetCardPreviewImpl(textAtTheTop = false, size = WidgetCardSize.BRICK)

@Composable
@Preview(widthDp = 500, showBackground = true)
private fun Preview7() =
    WidgetCardPreviewImpl(textAtTheTop = true, size = WidgetCardSize.FIXED_HEIGHT_BRICK)

@Composable
@Preview(widthDp = 500, showBackground = true)
private fun Preview8() =
    WidgetCardPreviewImpl(textAtTheTop = false, size = WidgetCardSize.FIXED_HEIGHT_BRICK)

@Composable
@Preview(heightDp = 200)
private fun Preview9() = WidgetCardPreviewImpl(loading = true)

@Composable
@Suppress("FunctionNaming", "MagicNumber")
internal fun WidgetCardPreviewImpl(
    textAtTheTop: Boolean = true,
    size: WidgetCardSize = WidgetCardSize.SQUARE,
    loading: Boolean = false,
) = internalComponents.WidgetCard(
    title = loremIpsum(3),
    subtitle = loremIpsum(6),
    textAtTheTop = textAtTheTop,
    size = size,
    backgroundImage = painterResource(R.drawable.example_doctor),
    loading = loading,
    avatar = {
        Avatar(
            content = AvatarContent.Image(painterResource(R.drawable.example_dog)),
            indicator = AvatarIndicator(IndicatorColor.PRIMARY),
            size = AvatarSize.SIZE_40,
            shape = AvatarShape.CIRCLE,
            onClick = boast("Avatar's onClick"),
            contentDescription = null
        )
    },
    indicator = { BadgePreview() },
    onClick = boast(),
)