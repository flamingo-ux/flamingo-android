@file:Suppress("MagicNumber", "MatchingDeclarationName")

package com.flamingo.playground.components.alertmessage

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.flamingo.components.Action
import com.flamingo.components.ActionGroup
import com.flamingo.components.AlertMessage
import com.flamingo.components.AlertMessageVariant
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

private class AlertMessageActor : Actor {
    var variant by mutableStateOf(startVariant)
    var onClose by mutableStateOf<(() -> Unit)?>(null)
    var buttonsNumber by mutableStateOf(0)

    @Composable
    override fun ActorScope.Actor() {
        Box(modifier = Modifier.requiredWidth(350.dp)) {
            AlertMessage(
                text = loremIpsum(15),
                variant = variant,
                onClose = onClose,
                actions = when (buttonsNumber) {
                    0 -> null
                    1 -> ActionGroup(Action("Confirm", {}))
                    2 -> ActionGroup(Action("Confirm", {}), Action("Deny", {}))
                    else -> error("")
                }
            )
        }
    }
}

@Suppress("MaxLineLength")
/**
 * Creation of this file was part of the tutorial on how to create videos with __Theater__
 * framework. Tutorial is located [here](https://sbervideo.companyname.ru/watch/p3rphgxOQlGtJu2NDA).
 * Before watching, it is recommended to read Theater's README.md, located in the `theater` module.
 * [(alternate link)](https://confluence.companyname.ru/x/lQkbZwE).
 */
class TheaterPkg : TheaterPackage {
    override val play: TheaterPlay<*, *> = TheaterPlay(
        stage = FlamingoStage(),
        leadActor = AlertMessageActor(),
        cast = listOf(EndScreenActor(Director.AntonPopov)),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = alertMessagePlot,
        backstages = listOf(
            Backstage(name = "Main", actor = AlertMessageActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AntonPopov)),
        )
    )
}

private val alertMessagePlot = Plot<AlertMessageActor, FlamingoStage> {
    hideEndScreenActor()
    // This music cannot be included in the apk because of the licensing issues
    // orchestra.playMusic(R.raw.danger_twins_make_it_look_easy)
    delay(2000)

    // zoom on the left icon
    layer0.animateTo(
        rotationX = -22.516165f,
        rotationY = 31.168234f,
        rotationZ = 0f,
        scaleX = 2.1092918f,
        scaleY = 2.1092918f,
        translationX = 354.6582f,
        translationY = 0f,
        shadowElevation = 0f,
        cameraDistance = 9.507492f,
        spec = tween(2000),
    )

    delay(700)

    goThroughVariants()

    delay(700)

    // zoom on the close button
    layer0.animateTo(
        rotationX = -15.716208f,
        rotationY = -33.83238f,
        rotationZ = 0f,
        scaleX = 2.1092918f,
        scaleY = 2.1092918f,
        translationX = -296.00995f,
        translationY = 0f,
        shadowElevation = 0f,
        cameraDistance = 9.507492f,
        spec = tween(2000),
    )

    showOffCloseButton()

    // back to start
    layer0.animateTo(
        rotationX = 0f,
        rotationY = 0f,
        rotationZ = 0f,
        scaleX = 1f,
        scaleY = 1f,
        translationX = 0f,
        translationY = 0f,
        shadowElevation = 0f,
        cameraDistance = 9.507492f,
        spec = tween(2000),
    )

    delay(500)

    showOffActions()

    delay(1000)

    parallel(
        { goToEndScreen() },
        { orchestra.decreaseVolume() },
    )
}

private suspend fun PlotScope<AlertMessageActor, FlamingoStage>.showOffActions() {
    leadActor.buttonsNumber = 2
    goThroughVariants()

    delay(500)

    leadActor.buttonsNumber = 1
    goThroughVariants(400)

    leadActor.buttonsNumber = 0
}

private suspend fun PlotScope<AlertMessageActor, FlamingoStage>.showOffCloseButton() {
    repeat(2) {
        leadActor.onClose = {}
        delay(1000)
        leadActor.onClose = null
        delay(1000)
    }
}

private suspend fun PlotScope<AlertMessageActor, FlamingoStage>.goThroughVariants(
    delay: Long = 600,
) {
    variants.forEach {
        leadActor.variant = it
        delay(delay)
    }
}

private val startVariant = AlertMessageVariant.INFO
private val variants = AlertMessageVariant.values().filterNot { it == startVariant } + startVariant
