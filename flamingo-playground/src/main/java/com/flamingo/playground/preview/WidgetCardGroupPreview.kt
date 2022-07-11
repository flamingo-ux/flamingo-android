package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.flamingo.Flamingo
import com.flamingo.components.Avatar
import com.flamingo.components.AvatarContent
import com.flamingo.components.AvatarIndicator
import com.flamingo.components.AvatarShape
import com.flamingo.components.AvatarSize
import com.flamingo.components.IndicatorColor
import com.flamingo.components.LinkCard
import com.flamingo.components.widgetcard.WidgetCard
import com.flamingo.components.widgetcard.WidgetCardGroup
import com.flamingo.components.widgetcard.WidgetCardGroupChildrenRange
import com.flamingo.components.widgetcard.WidgetCardGroupScope
import com.flamingo.demoapi.R
import com.flamingo.loremIpsum
import com.flamingo.playground.boast

@Composable
@Preview(showBackground = true)
private fun Loading() = WidgetCardGroup { repeat(6) { WidgetCard(title = "", loading = true) } }

@Composable
@Preview(showBackground = true)
private fun Link() =
    WidgetCardGroup { repeat(2) { LinkCard(text = loremIpsum(2), icon = Flamingo.icons.Plus) } }

class CardsNumber : CollectionPreviewParameterProvider<Int>(WidgetCardGroupChildrenRange.toList())

@Composable
@Preview(showBackground = true)
@Preview(widthDp = 840, showBackground = true)
private fun Card(@PreviewParameter(CardsNumber::class) n: Int) {
    WidgetCardGroupPreview(n)
}

@Composable
fun WidgetCardGroupPreview(n: Int = 4) =
    WidgetCardGroup { repeat(n) { WidgetCardInGroupPreview() } }

@Composable
private fun WidgetCardGroupScope.WidgetCardInGroupPreview() = WidgetCard(
    title = loremIpsum(3),
    subtitle = loremIpsum(6),
    textAtTheTop = true,
    backgroundImage = painterResource(R.drawable.example_doctor),
    loading = false,
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