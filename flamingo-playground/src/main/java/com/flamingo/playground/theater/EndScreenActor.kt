@file:Suppress("MagicNumber", "FunctionNaming")

package com.flamingo.playground.theater

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import com.flamingo.Flamingo
import com.flamingo.components.Text
import com.flamingo.playground.R
import com.theater.Actor
import com.theater.ActorScope
import com.theater.PlotScope
import com.theater.densityMultiplier

/**
 * Shows flamingo logo, name of the DS, [componentName] and [directorName]. Intended to be shown at
 * the end of the video.
 */
class EndScreenActor(
    val director: Director,
    val componentName: String? = null,
) : Actor {

    @Composable
    override fun ActorScope.Actor() = Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        val componentName = remember {
            componentName
                ?: theaterPackage?.componentRecord()?.displayName
                ?: "no component name found"
        }

        CompositionLocalProvider(LocalDensity provides Density(densityMultiplier * 4f)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Logo(Modifier.requiredSize(100.dp))
                Text(text = "Flamingo", style = Flamingo.typography.h4, color = Color.White)
                Text(
                    text = componentName,
                    style = Flamingo.typography.body1,
                    color = Color.White,
                )
                Text(
                    text = stringResource(
                        R.string.theater_director_subtitle, stringResource(director.fullName)
                    ),
                    style = Flamingo.typography.subtitle2,
                    color = Color.White
                )
            }
        }
    }

    @Composable
    private fun Logo(modifier: Modifier) = Image(
        modifier = modifier,
        painter = painterResource(id = R.drawable.flamingo_logo),
        contentDescription = null,
    )
}

suspend fun PlotScope<*, *>.hideEndScreenActor() = findEndScreenActorLayer().alpha.snapTo(0f)

/**
 * @param endScreenDuration after appearing with [spec], an amount of additional time end screen
 * will be visible
 */
suspend fun PlotScope<*, FlamingoStage>.goToEndScreen(
    spec: AnimationSpec<Float> = tween(1000),
    endScreenDuration: Long = 1500,
) {
    parallel(
        { stage.logoVisibility.animateTo(0f, spec) },
        {
            (cast + leadActor).mapNotNull { actor ->
                if (actor is EndScreenActor) return@mapNotNull null
                coroutineScope.async { actor.layer(1).alpha.animateTo(0f, spec) }
            }.awaitAll()
        },
        {
            findEndScreenActorLayer().alpha.animateTo(1f, spec)
            delay(endScreenDuration)
        },
    )
}

private fun PlotScope<*, *>.findEndScreenActorLayer() = actorOfType<EndScreenActor>().layer()
