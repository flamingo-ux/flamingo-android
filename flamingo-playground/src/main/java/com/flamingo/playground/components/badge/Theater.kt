@file:Suppress("MagicNumber", "MatchingDeclarationName")

package com.flamingo.playground.components.badge

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.flamingo.components.Badge
import com.flamingo.components.BadgeColor
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
import kotlin.math.abs

private class BadgeActor : Actor {
    @Composable
    override fun ActorScope.Actor() = Unit
}

private class BadgeShowerActor : Actor {
    private val labels = loremIpsum(500).split(' ').filter { it.length in 5..7 }
    private val colors: List<BadgeColor> = BadgeColor::class.sealedSubclasses
        .filterNot {
            it == BadgeColor.Gradient::class ||
                    it == BadgeColor.White::class ||
                    it == BadgeColor.PickleRick::class ||
                    it == BadgeColor.Default::class
        }
        .mapNotNull { it.objectInstance } +
            BadgeColor.Gradient.values().toList()

    private val animSpec = tween<Float>(10_000, easing = LinearEasing)

    @Composable
    override fun ActorScope.Actor() {
        Column {
            repeat(7) { n1 ->
                Row {
                    repeat(8) { n2 ->
                        val targetX = remember { Animatable(0f) }
                        val targetY = remember { Animatable(0f) }
                        val targetZ = remember { Animatable(0f) }
                        LaunchedEffect(Unit) {
                            launch { targetX.animateTo(uniqueAngle(n1, n2).toFloat(), animSpec) }
                            launch { targetY.animateTo(uniqueAngle(n1, n2).toFloat(), animSpec) }
                            launch { targetZ.animateTo(uniqueAngle(n1, n2).toFloat(), animSpec) }
                        }
                        Box(modifier = Modifier.graphicsLayer {
                            rotationX = targetX.value
                            rotationY = targetY.value
                            rotationZ = targetZ.value
                        }) {
                            Badge(
                                label = labels[labels.uniqueIndex(n1, n2)],
                                color = colors[colors.uniqueIndex(n1, n2)]
                            )
                        }
                        Spacer(modifier = Modifier.requiredWidth(40.dp))
                    }
                }
                Spacer(modifier = Modifier.requiredHeight(40.dp))
            }
        }
    }

    /**
     * @return valid index of an item in a [List], which is _kind of_ unique for each pair of
     * [n1] and [n2] (order matters)
     */
    private fun <T> List<T>.uniqueIndex(n1: Int, n2: Int): Int {
        val uniqueCombination = abs((n1 + 1).toLong() shl 32 + n2)
        return (uniqueCombination % size).toInt().coerceIn(indices)
    }

    /**
     * @return _kind of_ unique angle for each pair of [n1] and [n2] (order matters)
     */
    private fun uniqueAngle(n1: Int, n2: Int): Int {
        val uniqueCombination = abs((n1 + 1).toLong() shl 32 + n2)
        return (uniqueCombination % 360).toInt().coerceIn(0, 360)
    }
}

class TheaterPkg : TheaterPackage {
    override val play: TheaterPlay<*, *> = TheaterPlay(
        stage = FlamingoStage(),
        leadActor = BadgeActor(),
        cast = listOf(BadgeShowerActor(), EndScreenActor(Director.AntonPopov)),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = badgePlot,
        backstages = listOf(
            Backstage(name = "Main", actor = BadgeActor()),
            Backstage(name = "BadgeShower", actor = BadgeShowerActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AntonPopov)),
        )
    )
}

private fun PlotScope<*, *>.badgeShowerActor(): BadgeShowerActor {
    return cast.first { it is BadgeShowerActor } as BadgeShowerActor
}

private val badgePlot = Plot<BadgeActor, FlamingoStage> {
    val badgeShowerActor = badgeShowerActor()
    hideEndScreenActor()

    badgeShowerActor.layer().animateTo(
        scaleX = 0.73f,
        scaleY = 0.73f,
        spec = snap(),
    )

    delay(12_000)

    parallel(
        { goToEndScreen() },
        { orchestra.decreaseVolume() },
    )
}
