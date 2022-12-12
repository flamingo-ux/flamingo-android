package com.flamingo.playground.components.widgetcardgroup

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.components.Avatar
import com.flamingo.components.AvatarContent
import com.flamingo.components.AvatarIndicator
import com.flamingo.components.AvatarShape
import com.flamingo.components.AvatarSize
import com.flamingo.components.Badge
import com.flamingo.components.BadgeColor
import com.flamingo.components.IconButton
import com.flamingo.components.IndicatorColor
import com.flamingo.components.widgetcard.WidgetCard
import com.flamingo.components.widgetcard.WidgetCardGroup
import com.flamingo.demoapi.R
import com.flamingo.loremIpsum
import com.flamingo.playground.theater.Director
import com.flamingo.playground.theater.EndScreenActor
import com.flamingo.playground.theater.FlamingoStage
import com.flamingo.playground.theater.goToEndScreen
import com.flamingo.playground.theater.hideEndScreenActor
import com.theater.Actor
import com.theater.ActorScope
import com.theater.Backstage
import com.theater.Plot
import com.theater.PlotScope
import com.theater.TheaterPackage
import com.theater.TheaterPlay
import kotlinx.coroutines.delay

private class WidgetCardSingleActor : Actor {

    var title: String? by mutableStateOf(loremIpsum(3))
    var subtitle: String? by mutableStateOf(null)
    var textAtTop: Boolean by mutableStateOf(true)
    var loading: Boolean by mutableStateOf(false)
    var avatarPresent: Boolean by mutableStateOf(false)
    var indicatorPresent: Boolean by mutableStateOf(false)
    var iconButtonPresent: Boolean by mutableStateOf(false)
    var backgroundImageRes: Int? by mutableStateOf(null)
    var backgroundColor: Color? by mutableStateOf(null)

    @Composable
    override fun ActorScope.Actor() {
        Box(Modifier.width(320.dp)) {
            WidgetCardGroup {
                WidgetCard(
                    title = title,
                    subtitle = subtitle,
                    textAtTheTop = textAtTop,
                    backgroundImage = backgroundImageRes?.let { painterResource(it) },
                    backgroundColor = backgroundColor ?: Flamingo.colors.backgroundSecondary,
                    loading = loading,
                    avatar = if (avatarPresent) ({ AvatarDemo() }) else null,
                    indicator = if (indicatorPresent) ({ BadgeDemo() }) else null,
                    iconButton = if (iconButtonPresent) ({ IconButtonDemo() }) else null
                )
            }
        }
    }
}

private class WidgetCardGroupActor : Actor {

    var cardAmount by mutableStateOf(1)

    @Composable
    override fun ActorScope.Actor() {
        Box(Modifier.width(400.dp)) {
            WidgetCardGroup {
                if (cardAmount > 0) WidgetCard(title = loremIpsum(3), subtitle = loremIpsum(6))
                if (cardAmount > 1) WidgetCard(
                    title = loremIpsum(3),
                    avatar = { AvatarDemo() },
                    backgroundImage = painterResource(R.drawable.example_doctor)
                )
                if (cardAmount > 2) WidgetCard(
                    title = null,
                    subtitle = loremIpsum(5),
                    indicator = { BadgeDemo() },
                    backgroundColor = Flamingo.colors.backgroundTertiary
                )
                if (cardAmount > 3) WidgetCard(
                    title = loremIpsum(3),
                    subtitle = loremIpsum(4),
                    textAtTheTop = false,
                    iconButton = { IconButtonDemo() },
                    backgroundImage = painterResource(R.drawable.example_doctor),
                    backgroundColor = Flamingo.colors.primary
                )
                if (cardAmount > 4) WidgetCard(
                    title = loremIpsum(3),
                    textAtTheTop = false,
                    avatar = { AvatarDemo() },
                    indicator = { BadgeDemo() }
                )
                if (cardAmount > 5) WidgetCard(
                    title = loremIpsum(3),
                    subtitle = loremIpsum(6),
                    avatar = { AvatarDemo() },
                    iconButton = { IconButtonDemo() }
                )
            }
        }
    }
}

class TheaterPkg : TheaterPackage {
    override val play: TheaterPlay<*, *> = TheaterPlay(
        stage = FlamingoStage(),
        leadActor = WidgetCardSingleActor(),
        cast = listOf(
            EndScreenActor(Director.AlekseyBublyaev),
            WidgetCardGroupActor()
        ),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = widgetCardGroupPlot,
        backstages = listOf(
            Backstage(name = "Widget card single", actor = WidgetCardSingleActor()),
            Backstage(name = "Widget card group", actor = WidgetCardGroupActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AlekseyBublyaev)),
        )
    )
}

private val widgetCardGroupPlot = Plot<WidgetCardSingleActor, FlamingoStage> {
    hideEndScreenActor()

    widgetCardGroupActor.layer().animateTo(
        alpha = 0f,
        translationX = 500f,
        spec = snap()
    )

    delay(1000)

    // zoom on text
    layer0.animateTo(
        rotationX = 0f,
        rotationY = 0f,
        scaleX = 1.812f,
        scaleY = 1.812f,
        translationX = 61f,
        translationY = 88f,
    )

    leadActor.subtitle = loremIpsum(6)
    delay(1000)
    leadActor.title = null
    delay(1000)
    leadActor.title = loremIpsum(3)
    delay(500)

    // zoom on avatar
    layer0.animateTo(
        rotationX = 0f,
        rotationY = 0f,
        scaleX = 2.33f,
        scaleY = 2.33f,
        translationX = 258f,
        translationY = -90f,
    )

    leadActor.avatarPresent = true
    delay(2000)

    // zoom on badge
    layer0.animateTo(
        rotationX = 0f,
        rotationY = 0f,
        scaleX = 2.33f,
        scaleY = 2.33f,
        translationX = -180f,
        translationY = -90f,
    )

    leadActor.indicatorPresent = true
    delay(2000)
    leadActor.indicatorPresent = false
    leadActor.iconButtonPresent = true
    delay(2000)

    // zoom out
    layer0.animateTo(
        rotationX = 0f,
        rotationY = 0f,
        scaleX = 1.36f,
        scaleY = 1.36f,
        translationX = 0f,
        translationY = 0f,
    )

    leadActor.textAtTop = false
    delay(1000)
    leadActor.textAtTop = true
    delay(1000)
    leadActor.loading = true
    delay(2000)
    leadActor.backgroundImageRes = R.drawable.example_doctor
    leadActor.loading = false
    delay(2000)

    parallel(
        {
            layer0.animateTo(
                translationX = -500f,
                spec = tween(2000)
            )
        },
        {
            widgetCardGroupActor.layer().animateTo(
                translationX = 0f,
                scaleX = 1f,
                scaleY = 1f,
                spec = tween(2000)
            )
        },
        {
            parallel(
                { widgetCardGroupActor.layer().alpha.animateTo(1f, animationSpec = tween(1000)) },
                { layer0.alpha.animateTo(0f, animationSpec = tween(1000, easing = LinearEasing)) },
            )
        }
    )

    widgetCardGroupPlot()

    parallel({ goToEndScreen() }, { orchestra.decreaseVolume() })
}

private suspend fun PlotScope<WidgetCardSingleActor, FlamingoStage>.widgetCardGroupPlot() {
    widgetCardGroupActor.layer().animateTo(
        rotationX = 35f,
        rotationZ = -22f,
        scaleX = .9f,
        scaleY = .9f,
    )


    val slideDurationMs = 10_500
    parallel(
        {
            delay(2000)
            widgetCardGroupActor.cardAmount = 2
            delay(2000)
            widgetCardGroupActor.cardAmount = 3
            delay(2000)
            widgetCardGroupActor.cardAmount = 4
            delay(2000)
            widgetCardGroupActor.cardAmount = 5
            delay(2000)
            widgetCardGroupActor.cardAmount = 6
            delay(2000)
        },
        {
            // camera slide
            widgetCardGroupActor.layer(1).animateTo(
                translationX = -7f,
                translationY = -288.6f,
                spec = tween(slideDurationMs, easing = LinearEasing)
            )
        },
    )

    widgetCardGroupActor.layer().animateTo(
        rotationX = 0f,
        rotationZ = 0f,
        scaleX = 1f,
        scaleY = 1f,
        translationX = 0f,
        translationY = 9f,
    )

    widgetCardGroupActor.layer().animateTo(
        rotationX = 0f,
        rotationZ = 0f,
        scaleX = 1f,
        scaleY = 1f,
        translationX = 0f,
        translationY = 780f,
        spec = tween(9000, easing = LinearEasing)
    )

}

private val PlotScope<WidgetCardSingleActor, FlamingoStage>.widgetCardGroupActor: WidgetCardGroupActor
    get() = cast.find { it is WidgetCardGroupActor } as WidgetCardGroupActor

@Composable
private fun AvatarDemo() {
    Avatar(
        content = AvatarContent.Image(painterResource(R.drawable.example_dog)),
        indicator = AvatarIndicator(IndicatorColor.PRIMARY),
        size = AvatarSize.SIZE_40,
        shape = AvatarShape.CIRCLE,
        onClick = null,
        contentDescription = null
    )
}

@Composable
private fun BadgeDemo() {
    Badge(label = "Badge", color = BadgeColor.Gradient.BLUE)
}

@Composable
private fun IconButtonDemo() {
    IconButton(onClick = {}, icon = Flamingo.icons.Bell, contentDescription = null)
}