package com.flamingo.playground.components.iconbutton

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.components.IconButton
import com.flamingo.components.IconButtonColor
import com.flamingo.components.IconButtonIndicator
import com.flamingo.components.IconButtonShape
import com.flamingo.components.IconButtonSize
import com.flamingo.components.IconButtonVariant
import com.flamingo.components.IndicatorColor
import com.flamingo.components.IndicatorPlacement
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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay

private class IconButtonActor : Actor {
    var variant by mutableStateOf(startVariant)
    var loading by mutableStateOf(false)
    var size by mutableStateOf(startSize)
    var shape by mutableStateOf(startShape)
    var color: IconButtonColor by mutableStateOf(startColor)
    var disabled by mutableStateOf(false)
    var indicator: IconButtonIndicator? by mutableStateOf(null)
    var icon by mutableStateOf(Flamingo.icons.User)

    @Composable
    override fun ActorScope.Actor() {
        Box(modifier = Modifier) {
            IconButton(
                onClick = {},
                icon = icon,
                contentDescription = null,
                size = size,
                variant = variant,
                color = color,
                shape = shape,
                indicator = indicator,
                loading = loading,
                disabled = disabled
            )
        }
    }
}

private class IconButtonTableActor : Actor {
    val topStart = IconButtonActor()
    val topCenter = IconButtonActor()
    val topEnd = IconButtonActor()
    val bottomStart = IconButtonActor()
    val bottomCenter = IconButtonActor()
    val bottomEnd = IconButtonActor()
    val buttons = listOf(topStart, topCenter, topEnd, bottomStart, bottomCenter, bottomEnd)

    private val distanceBetweenButtons = 16.dp
    private val horizontalArrangement = Arrangement.spacedBy(distanceBetweenButtons)

    @Composable
    override fun ActorScope.Actor() {
        Column {
            Row(
                horizontalArrangement = horizontalArrangement,
                verticalAlignment = Alignment.CenterVertically
            ) {
                with(topStart) { Actor() }
                with(topCenter) { Actor() }
                with(topEnd) { Actor() }
            }
            Spacer(modifier = Modifier.requiredHeight(distanceBetweenButtons))
            Row(
                horizontalArrangement = horizontalArrangement,
                verticalAlignment = Alignment.CenterVertically
            ) {
                with(bottomStart) { Actor() }
                with(bottomCenter) { Actor() }
                with(bottomEnd) { Actor() }
            }
        }
    }
}

/**
 * Read Theater's README.md, located in the `theater` module.
 * [(alternate link)](https://confluence.companyname.ru/x/lQkbZwE).
 */
class TheaterPkg : TheaterPackage {
    override val play: TheaterPlay<*, *> = TheaterPlay(
        stage = FlamingoStage(),
        leadActor = IconButtonActor(),
        cast = listOf(
            EndScreenActor(Director.AlekseyBublyaev),
            IconButtonTableActor()
        ),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = iconButtonPlot,
        backstages = listOf(
            Backstage(name = "Icon Button", actor = IconButtonActor()),
            Backstage(name = "Icon Button Table", actor = IconButtonTableActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AlekseyBublyaev)),
        )
    )
}

private val iconButtonPlot = Plot<IconButtonActor, FlamingoStage> {
    hideEndScreenActor()

    stage.darkTheme = true

    iconButtonTableActor.layer().animateTo(
        alpha = 0f,
        rotationZ = -980f,
        spec = snap()
    )

    iconButtonTableActor.apply {
        topStart.variant = IconButtonVariant.CONTAINED
        bottomStart.variant = IconButtonVariant.TEXT
        topCenter.variant = IconButtonVariant.TEXT
        bottomCenter.variant = IconButtonVariant.CONTAINED
        topEnd.variant = IconButtonVariant.CONTAINED
        bottomEnd.variant = IconButtonVariant.TEXT

        topStart.color = IconButtonColor.PRIMARY
        bottomStart.color = IconButtonColor.RATING
        topCenter.color = IconButtonColor.DEFAULT
        bottomCenter.color = IconButtonColor.PRIMARY
        topEnd.color = IconButtonColor.WARNING
        bottomEnd.color = IconButtonColor.ERROR

        topStart.size = IconButtonSize.SMALL
        bottomStart.size = IconButtonSize.SMALL
        topCenter.size = IconButtonSize.MEDIUM
        bottomCenter.size = IconButtonSize.MEDIUM
        topEnd.size = IconButtonSize.LARGE
        bottomEnd.size = IconButtonSize.LARGE
    }

    delay(1000)

    layer0.animateTo(
        rotationX = 0f,
        rotationY = 0f,
        scaleX = 2.30393f,
        scaleY = 2.30393f,
        translationX = 0f,
        translationY = 0f,
    )

    delay(1000)
    leadActor.shape = IconButtonShape.SQUARE
    delay(1000)
    leadActor.shape = IconButtonShape.CIRCLE
    delay(1000)
    leadActor.indicator = IconButtonIndicator(IndicatorColor.DEFAULT, IndicatorPlacement.TopEnd)
    delay(1000)
    leadActor.loading = true
    delay(1000)
    leadActor.loading = false
    delay(1000)
    leadActor.size = IconButtonSize.SMALL
    delay(1000)
    leadActor.size = IconButtonSize.LARGE
    delay(1000)
    leadActor.size = IconButtonSize.MEDIUM
    delay(1000)
    flickThroughColorVariants()

    parallel(
        {
            layer0.animateTo(
                rotationZ = 980f,
                scaleX = 16f,
                scaleY = 16f,
                spec = tween(2000)
            )
        },
        {
            iconButtonTableActor.layer().animateTo(
                rotationZ = 0f,
                scaleX = 1.2f,
                scaleY = 1.2f,
                spec = tween(2000)
            )
        },
        {
            delay(1000)
            stage.darkTheme = false
            parallel(
                { iconButtonTableActor.layer().alpha.animateTo(1f, animationSpec = tween(1000)) },
                { layer0.alpha.animateTo(0f, animationSpec = tween(400, easing = LinearEasing)) },
            )
        }
    )

    iconButtonTablePlot()
    delay(1000)

    parallel({ goToEndScreen() }, { orchestra.decreaseVolume() })
}

private suspend fun PlotScope<IconButtonActor, FlamingoStage>.iconButtonTablePlot() {
    iconButtonTableActor.layer().animateTo(
        rotationZ = 0f,
        rotationY = 35f,
        translationX = -43f,
        scaleX = 1.6f,
        scaleY = 1.6f,
        spec = tween(1000)
    )

    iconButtonTableActor.buttons.mapIndexed { index, actor ->
        coroutineScope.async {
            val newIndex =
                if (index >= IndicatorColor.values().size) index - IndicatorColor.values().size
                else index
            actor.shape = IconButtonShape.CIRCLE
            delay(1000)
            actor.indicator =
                IconButtonIndicator(IndicatorColor.values()[newIndex], IndicatorPlacement.TopEnd)
            delay(700)
            actor.icon = Flamingo.icons.Android
            delay(700)
            actor.icon = Flamingo.icons.Aperture
            delay(700)
            actor.icon = Flamingo.icons.Repost
            delay(1000)
        }
    }.awaitAll()

    iconButtonTableActor.layer().animateTo(
        rotationZ = 0f,
        rotationY = -35f,
        translationX = 43f,
        scaleX = 1.6f,
        scaleY = 1.6f,
        spec = tween(1000)
    )

    iconButtonTableActor.buttons.mapIndexed { index, actor ->
        coroutineScope.async {
            val newIndex =
                if (index >= IndicatorColor.values().size) index - IndicatorColor.values().size
                else index
            actor.shape = IconButtonShape.SQUARE
            delay(1000)
            actor.indicator =
                IconButtonIndicator(IndicatorColor.values()[newIndex], IndicatorPlacement.TopEnd)
            delay(700)
            actor.icon = Flamingo.icons.Bell
            delay(700)
            actor.icon = Flamingo.icons.Download
            delay(700)
            actor.icon = Flamingo.icons.Edit
            delay(1000)
        }
    }.awaitAll()
}

private suspend fun PlotScope<IconButtonActor, FlamingoStage>.flickThroughColorVariants() {
    with(leadActor) {
        colors.forEach { newColor ->
            color = newColor
            delay(1000)
        }
    }
}

private val PlotScope<IconButtonActor, FlamingoStage>.iconButtonTableActor: IconButtonTableActor
    get() = cast.find { it is IconButtonTableActor } as IconButtonTableActor

private val startVariant = IconButtonVariant.CONTAINED

private val startSize = IconButtonSize.MEDIUM

private val startColor = IconButtonColor.PRIMARY
private val colors = IconButtonColor.values().filterNot { it == startColor } + startColor

private val startShape = IconButtonShape.CIRCLE
