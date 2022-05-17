@file:Suppress("MagicNumber", "MatchingDeclarationName")

package com.flamingo.playground.components.topappbar

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.flamingo.Flamingo
import com.flamingo.components.AvatarBackground
import com.flamingo.components.AvatarContent
import com.flamingo.components.AvatarIndicator
import com.flamingo.components.AvatarShape
import com.flamingo.components.IconButtonIndicator
import com.flamingo.components.IndicatorColor
import com.flamingo.components.IndicatorPlacement
import com.flamingo.components.topappbar.ActionItem
import com.flamingo.components.topappbar.CenterItem
import com.flamingo.components.topappbar.EdgeItem
import com.flamingo.components.topappbar.TopAppBar
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
import com.flamingo.demoapi.R as DemoR

private class TopAppBarActor : Actor {
    var start: EdgeItem? by mutableStateOf(null)
    var center: CenterItem? by mutableStateOf(null)
    var action1: ActionItem? by mutableStateOf(null)
    var action2: ActionItem? by mutableStateOf(null)
    var end: EdgeItem? by mutableStateOf(null)
    var showBackground: Boolean by mutableStateOf(true)

    lateinit var image: Painter

    @Composable
    override fun ActorScope.Actor() {
        Box(
            modifier = Modifier
                .requiredWidth(420.dp)
                .shadow(6.dp) // TopAppBar's shadow is located only at the bottom
        ) {
            image = painterResource(id = DemoR.drawable.example_dog)
            TopAppBar(start, center, action1, action2, end, showShadow = false, showBackground)
        }
    }
}

class TheaterPkg : TheaterPackage {
    override val play: TheaterPlay<*, *> = TheaterPlay(
        stage = FlamingoStage(),
        cast = listOf(EndScreenActor(Director.AntonPopov)),
        leadActor = TopAppBarActor(),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = topAppBarPlot,
        backstages = listOf(
            Backstage {
                val image = painterResource(id = DemoR.drawable.example_dog)
                remember {
                    TopAppBarActor().apply {
                        start = EdgeItem.IconButton(Flamingo.icons.ArrowLeft) {}
                        center = CenterItem.Center(
                            title = "Title",
                            subtitle = "Subtitle",
                            avatar = CenterItem.Avatar(
                                content = AvatarContent.Image(image),
                                indicator = AvatarIndicator(IndicatorColor.WARNING),
                            )
                        )
                        action1 = ActionItem(
                            Flamingo.icons.Bell,
                            indicator = IconButtonIndicator(color = IndicatorColor.PRIMARY)
                        ) {}
                        action2 = ActionItem(Flamingo.icons.Calendar) {}
                        end = EdgeItem.IconButton(Flamingo.icons.Coffee) {}
                    }
                }
            }
        )
    )
}

private val topAppBarPlot = Plot<TopAppBarActor, FlamingoStage> {
    hideEndScreenActor()
    layer0.cameraDistance.snapTo(6.07754f)
    // This music cannot be included in the apk because of the licensing issues
    // orchestra.playMusic(R.raw.danger_twins_make_it_look_easy)

    with(leadActor) {
        start = EdgeItem.IconButton(Flamingo.icons.ArrowLeft) {}
        center = CenterItem.Center(
            title = "Title",
            subtitle = "Subtitle",
            avatar = CenterItem.Avatar(
                content = AvatarContent.Image(image),
                indicator = AvatarIndicator(IndicatorColor.WARNING),
            )
        )
        action1 = ActionItem(
            Flamingo.icons.Bell,
            indicator = IconButtonIndicator(color = IndicatorColor.PRIMARY)
        ) {}
        action2 = ActionItem(Flamingo.icons.Calendar) {}
        end = EdgeItem.IconButton(Flamingo.icons.Coffee) {}
    }

    delay(2300)

    // slight tilt, zoom on the start item
    parallel({
        layer0.animateTo(
            rotationY = 17f,
            scaleX = 2.0841475f,
            scaleY = 2.0841475f,
            spec = tween(2000)
        )
    }, {
        layer1.animateTo(
            translationX = 168.29077f,
            spec = tween(1500)
        )
    })

    parallel({
        // length = 7400ms
        demonstrateIndicatorOfStartIconButton()

        leadActor.start = EdgeItem.Avatar(AvatarContent.Letters('A', 'B', AvatarBackground.PINK))
        delay(2000)
        leadActor.start = null
        delay(1000)
        leadActor.start = EdgeItem.IconButton(Flamingo.icons.ArrowLeft) {}
        delay(2000)
    }, {
        layer1.translationX.animateTo(138.62769f, animationSpec = tween(7400))
    })

    // zoom to the center item's avatar
    parallel({
        layer0.animateTo(
            scaleX = 2.7410662f,
            scaleY = 2.7410662f,
            spec = tween(1200)
        )
    }, {
        layer1.animateTo(
            scaleX = 1.3095018f,
            scaleY = 1.3095018f,
            translationX = 172.28525f,
            spec = tween(1200)
        )
    })

    demonstrateCenterAvatarShapes()

    toStartPosition()

    demonstrateLargeCenterText()

    delay(2000)

    demonstrateFontScale()

    // tilt and zoom to the end items
    parallel({
        layer0.animateTo(
            rotationY = 17f,
            scaleX = 2.0841475f,
            scaleY = 2.0841475f,
            spec = tween(2000)
        )
    }, {
        layer1.animateTo(
            translationX = -130.67337f,
            spec = tween(1800)
        )
    })

    demonstrateEndItems()

    toStartPosition()

    stage.darkTheme = true

    delay(3000)

    parallel(
        { goToEndScreen() },
        { orchestra.decreaseVolume() },
    )
}

private suspend fun PlotScope<TopAppBarActor, FlamingoStage>.toStartPosition() {
    parallel({
        layer0.animateTo(
            rotationX = 0f,
            rotationY = 0f,
            rotationZ = 0f,
            scaleX = 1f,
            scaleY = 1f,
            translationX = 0f,
            translationY = 0f,
            shadowElevation = 0f,
            cameraDistance = 6.07754f,
            spec = tween(2500)
        )
    }, {
        layer1.animateTo(
            rotationX = 0f,
            rotationY = 0f,
            rotationZ = 0f,
            scaleX = 1f,
            scaleY = 1f,
            translationX = 0f,
            translationY = 0f,
            shadowElevation = 0f,
            cameraDistance = 4f,
            spec = tween(2500)
        )
    })
}

private suspend fun
        PlotScope<TopAppBarActor, FlamingoStage>.demonstrateEndItems() = with(leadActor) {
    action1 = null
    delay(600)
    action2 = null
    delay(600)
    end = null
    delay(800)
    action1 = ActionItem(Flamingo.icons.Lock) {}
    delay(800)
    action2 = ActionItem(Flamingo.icons.Camera) {}
    delay(800)
    end = EdgeItem.IconButton(Flamingo.icons.Coffee) {}
    delay(1000)
    end = EdgeItem.Avatar(AvatarContent.Letters('A', 'B', AvatarBackground.PRIMARY))
    delay(2000)
}

private suspend fun PlotScope<TopAppBarActor, FlamingoStage>.demonstrateFontScale() {
    stage.fontScale.animateTo(1.6f, animationSpec = tween(1200))
    stage.fontScale.animateTo(1f, animationSpec = tween(1200))
}

private suspend fun PlotScope<TopAppBarActor, FlamingoStage>.demonstrateLargeCenterText() {
    leadActor.center = CenterItem.Center(
        title = loremIpsum(2),
        subtitle = loremIpsum(3),
        avatar = CenterItem.Avatar(
            content = AvatarContent.Letters('A', 'B', AvatarBackground.RED),
            shape = AvatarShape.CIRCLE,
            indicator = AvatarIndicator(IndicatorColor.WARNING),
        )
    )

    delay(1300)

    leadActor.center = CenterItem.Center(
        title = loremIpsum(15),
        subtitle = loremIpsum(15),
        avatar = CenterItem.Avatar(
            content = AvatarContent.Letters('A', 'B', AvatarBackground.RED),
            shape = AvatarShape.CIRCLE,
            indicator = AvatarIndicator(IndicatorColor.WARNING),
        )
    )
}

private suspend fun PlotScope<TopAppBarActor, FlamingoStage>.demonstrateCenterAvatarShapes() {
    val shapes = AvatarShape.values().filter { it != AvatarShape.CIRCLE } + AvatarShape.CIRCLE
    shapes.forEach { shape ->
        leadActor.center = CenterItem.Center(
            title = "Title",
            subtitle = "Subtitle",
            avatar = CenterItem.Avatar(
                content = AvatarContent.Image(leadActor.image),
                shape = shape,
                indicator = AvatarIndicator(IndicatorColor.WARNING),
            )
        )
        delay(700)
    }
}

private suspend fun
        PlotScope<TopAppBarActor, FlamingoStage>.demonstrateIndicatorOfStartIconButton() {
    IndicatorColor.values()
        .zip(IndicatorPlacement.values())
        .forEach { (color, placement) ->
            leadActor.start =
                EdgeItem.IconButton(Flamingo.icons.Bell, IconButtonIndicator(color, placement)) {}
            delay(600)
        }
}
