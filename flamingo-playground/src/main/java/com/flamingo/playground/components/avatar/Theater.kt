package com.flamingo.playground.components.avatar

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.ModalDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.components.Avatar
import com.flamingo.components.AvatarBackground
import com.flamingo.components.AvatarContent
import com.flamingo.components.AvatarIndicator
import com.flamingo.components.AvatarShape
import com.flamingo.components.AvatarSize
import com.flamingo.components.IndicatorColor
import com.flamingo.demoapi.R
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

private class AvatarActor : Actor {

    var content: AvatarContent by mutableStateOf(
        AvatarContent.Letters(
            'A',
            'A',
            startBackground
        )
    )
    var disabled: Boolean by mutableStateOf(false)
    var size: AvatarSize by mutableStateOf(startSize)
    var shape: AvatarShape by mutableStateOf(AvatarShape.CIRCLE)
    var indicator: AvatarIndicator? by mutableStateOf(null)

    var painterRes: Int? by mutableStateOf(null)

    @Composable
    override fun ActorScope.Actor() {
        Box(modifier = Modifier) {
            Avatar(
                content = if (painterRes != null)
                    AvatarContent.Image(painterResource(painterRes!!)) else content,
                onClick = null,
                contentDescription = null,
                size = size,
                shape = shape,
                indicator = indicator,
                disabled = disabled
            )
        }
    }
}

private class AvatarRowActor : Actor {
    val start = AvatarActor().apply {
        size = AvatarSize.SIZE_112
        content = AvatarContent.Letters('A', 'A', AvatarBackground.BLUE)
        shape = AvatarShape.ROUNDED_CORNERS_SMALL
    }
    val center = AvatarActor().apply {
        size = AvatarSize.SIZE_112
        shape = AvatarShape.ROUNDED_CORNERS_BIG
    }
    val end = AvatarActor().apply {
        size = AvatarSize.SIZE_112
        content = AvatarContent.Icon(Flamingo.icons.Aperture, AvatarBackground.GREEN)
        shape = AvatarShape.ROUNDED_CORNERS_MEDIUM
    }
    val avatars = listOf(start, center, end)

    private val distanceBetween = 16.dp
    private val horizontalArrangement = Arrangement.spacedBy(distanceBetween)

    @Composable
    override fun ActorScope.Actor() {
        center.content = AvatarContent.Image(painterResource(R.drawable.example_human))
        Row(horizontalArrangement = horizontalArrangement) {
            with(start) { Actor() }
            with(center) { Actor() }
            with(end) { Actor() }
        }
    }
}

class TheaterPkg : TheaterPackage {
    override val play: TheaterPlay<*, *> = TheaterPlay(
        stage = FlamingoStage(),
        leadActor = AvatarActor(),
        cast = listOf(
            EndScreenActor(Director.AlekseyBublyaev),
            AvatarRowActor()
        ),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = avatarPlot,
        backstages = listOf(
            Backstage(name = "Avatar", actor = AvatarActor()),
            Backstage(name = "Avatar row", actor = AvatarRowActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AlekseyBublyaev)),
        )
    )
}

private val avatarPlot = Plot<AvatarActor, FlamingoStage> {
    hideEndScreenActor()

    avatarRowActor.layer().animateTo(
        alpha = 0f,
        rotationZ = -180f,
        spec = snap()
    )

    delay(1000)

    layer0.animateTo(
        rotationX = 0f,
        rotationY = 0f,
        scaleX = 2.87f,
        scaleY = 2.87f,
        translationX = 0f,
        translationY = 0f,
    )

    flickThroughBackgrounds()

    delay(1000)

    leadActor.content = AvatarContent.Icon(
        Flamingo.icons.Bell,
        startBackground
    )

    delay(2000)

    leadActor.painterRes = R.drawable.example_human

    delay(2000)

    layer0.animateTo(
        rotationX = 0f,
        rotationY = 0f,
        scaleX = 1f,
        scaleY = 1f,
        translationX = 0f,
        translationY = 0f,
    )

    leadActor.indicator = AvatarIndicator(IndicatorColor.PRIMARY)

    flickThroughSizes()

    delay(1000)

    leadActor.disabled = true
    delay(1000)
    leadActor.disabled = false
    delay(1000)

    // rotation and transition to another actor
    parallel(
        {
            layer0.animateTo(
                rotationZ = 180f,
                scaleX = 1f,
                scaleY = 1f,
                spec = tween(2000)
            )
        },
        {
            avatarRowActor.layer().animateTo(
                rotationZ = 0f,
                scaleX = 1f,
                scaleY = 1f,
                spec = tween(2000)
            )
        },
        {
            delay(1000)
            parallel(
                { avatarRowActor.layer().alpha.animateTo(1f, animationSpec = tween(1000)) },
                { layer0.alpha.animateTo(0f, animationSpec = tween(400, easing = LinearEasing)) },
            )
        }
    )

    avatarRowPlot()
    delay(1000)

    parallel({ goToEndScreen() }, { orchestra.decreaseVolume() })
}

private suspend fun PlotScope<AvatarActor, FlamingoStage>.avatarRowPlot() {
    avatarRowActor.layer().animateTo(
            rotationX = 0f,
            rotationY = 26f,
            scaleX = 1f,
            scaleY = 1f,
            translationX = 18f,
            translationY = 0f
        )

    AvatarSize.values().reversed().forEach { newSize ->
        avatarRowActor.avatars.forEach {
            it.size = newSize
        }
        delay(700)
    }
}

private suspend fun PlotScope<AvatarActor, FlamingoStage>.flickThroughBackgrounds() {
    with(leadActor) {
        backgrounds.forEach { newBackground ->
            content = AvatarContent.Letters(
                'A',
                'A',
                newBackground
            )
            delay(300)
        }
    }
}

private suspend fun PlotScope<AvatarActor, FlamingoStage>.flickThroughSizes() {
    with(leadActor) {
        AvatarSize.values().forEach { newSize ->
            parallel({
                layer0.animateTo(
                    rotationX = 0f,
                    rotationY = 0f,
                    rotationZ = 360f,
                    scaleX = 1f,
                    scaleY = 1f,
                    translationX = 0f,
                    translationY = 0f,
                )
            }, {
                delay(400)
                size = newSize
            })
            layer0.animateTo(
                rotationZ = 0f,
                spec = snap()
            )
            delay(200)
        }
    }
}

private val PlotScope<AvatarActor, FlamingoStage>.avatarRowActor: AvatarRowActor
    get() = cast.find { it is AvatarRowActor } as AvatarRowActor

private val startBackground = AvatarBackground.GREEN
private val backgrounds =
    AvatarBackground.values().filterNot { it == startBackground } + startBackground

private val startSize = AvatarSize.SIZE_56
private val sizes = AvatarSize.values().filterNot { it == startSize } + startSize
