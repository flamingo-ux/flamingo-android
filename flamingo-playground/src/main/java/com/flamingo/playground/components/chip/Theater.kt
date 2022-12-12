@file:Suppress("MagicNumber", "MatchingDeclarationName")

package com.flamingo.playground.components.chip

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import com.flamingo.Flamingo
import com.flamingo.components.Chip
import com.flamingo.components.ChipColor
import com.flamingo.components.ChipVariant
import com.flamingo.components.listitem.ListItem
import com.flamingo.loremIpsum
import com.flamingo.playground.R
import com.flamingo.playground.theater.Director
import com.flamingo.playground.theater.EndScreenActor
import com.flamingo.playground.theater.FlamingoStage
import com.flamingo.playground.theater.goToEndScreen
import com.flamingo.playground.theater.hideEndScreenActor
import com.flamingo.theme.FlamingoIcon
import com.theater.Actor
import com.theater.ActorScope
import com.theater.Backstage
import com.theater.Plot
import com.theater.PlotScope
import com.theater.TheaterPackage
import com.theater.TheaterPlay
import com.theater.densityMultiplier

private class ChipActor : Actor {
    private val startColor = ChipColor.DEFAULT
    val colors = ChipColor.values().filterNot { it == startColor } + startColor

    private val startVariant = ChipVariant.CONTAINED
    val variants = ChipVariant.values().filterNot { it == startVariant } + startVariant

    var label: String by mutableStateOf("")
    var selected: Boolean by mutableStateOf(false)
    var onClick: (() -> Unit)? by mutableStateOf(null)
    var onDelete: (() -> Unit)? by mutableStateOf(null)
    var variant: ChipVariant by mutableStateOf(startVariant)
    var color: ChipColor by mutableStateOf(startColor)
    var icon: FlamingoIcon? by mutableStateOf(null)
    var disabled: Boolean by mutableStateOf(false)

    @Composable
    override fun ActorScope.Actor() {
        label = stringArrayResource(R.array.theater_chip_labels)[0]
        Chip(label, selected, onClick, onDelete, variant, color, icon, disabled)
    }
}

private class ChipTableActor : Actor {
    val topStart = ChipActor()
    val topEnd = ChipActor()
    val bottomStart = ChipActor()
    val bottomEnd = ChipActor()
    val chips = listOf(topStart, topEnd, bottomStart, bottomEnd)

    private val distanceBetweenChips = 16.dp
    private val horizontalArrangement = Arrangement.spacedBy(distanceBetweenChips)

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    override fun ActorScope.Actor() {
        Column {
            Row(horizontalArrangement = horizontalArrangement) {
                with(topStart) { Actor() }
                with(topEnd) { Actor() }
            }
            Spacer(modifier = Modifier.requiredHeight(distanceBetweenChips))
            Row(horizontalArrangement = horizontalArrangement) {
                with(bottomStart) { Actor() }
                with(bottomEnd) { Actor() }
            }
        }
    }
}

private class ChipGroupActor : Actor {
    var selectedIndex: Int by mutableStateOf(-1)
    var disabledIndex: Int by mutableStateOf(-1)
    var chipsNumber by mutableStateOf(6)
    var variant by mutableStateOf(ChipVariant.CONTAINED)

    private val exampleListItemText = loremIpsum(9)
    private val listItemsNumber = 4

    @Composable
    override fun ActorScope.Actor() {
        val labels = stringArrayResource(R.array.theater_chip_labels).toList()
        CompositionLocalProvider(LocalDensity provides Density(densityMultiplier * 2)) {
            Column(modifier = Modifier.requiredWidth(640.dp)) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(chipsNumber) {
                        var labelIndex = it
                        while (labelIndex > labels.lastIndex) {
                            labelIndex -= labels.size
                        }
                        Chip(
                            color = ChipColor.PRIMARY,
                            label = labels[labelIndex],
                            variant = variant,
                            selected = selectedIndex == it,
                            disabled = disabledIndex == it,
                        )
                    }
                }
                repeat(listItemsNumber) {
                    ListItem(
                        title = "Item $it, $exampleListItemText",
                        subtitle = "Subtitle $it, $exampleListItemText",
                    )
                }
            }
        }
    }
}

class TheaterPkg : TheaterPackage {
    override val play: TheaterPlay<*, *> = TheaterPlay(
        stage = FlamingoStage(),
        leadActor = ChipActor(),
        cast = listOf(
            EndScreenActor(Director.AntonPopov),
            ChipTableActor(),
            ChipGroupActor()
        ),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = chipPlot,
        backstages = listOf(
            Backstage(name = "Main", actor = {
                val firstLabel = stringArrayResource(R.array.theater_chip_labels)
                remember {
                    ChipActor().apply {
                        label = firstLabel[0]
                        icon = Flamingo.icons.Moon
                    }
                }
            }),
            Backstage(name = "ChipTable", actor = ChipTableActor()),
            Backstage(name = "ChipGroup", actor = ChipGroupActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AntonPopov)),
        )
    )
}

private val chipPlot = Plot<ChipActor, FlamingoStage> {
    hideEndScreenActor()
    stage.darkTheme = true
    chipTableActor.layer().animateTo(
        alpha = 0f,
        rotationZ = -180f,
        spec = snap()
    )
    chipGroupActor.layer().alpha.snapTo(0f)
    // This music cannot be included in the apk because of the licensing issues
    // orchestra.playMusic(R.raw.magnusthemagnus_area)

    delay(2000)

    parallel({
        delay(700)
        leadActor.icon = Flamingo.icons.Moon
    }, {
        // zoom on the icon
        layer0.animateTo(
            rotationX = -12.483415f,
            rotationY = 23.070297f,
            scaleX = 2.00393f,
            scaleY = 2.00393f,
            translationX = 77.87079f,
            translationY = -10.58313f,
        )
    })

    parallel({
        delay(1000)
        leadActor.onDelete = {}
    }, {
        // zoom on the delete button
        layer0.animateTo(
            rotationX = -11.598729f,
            rotationY = -26.763685f,
            scaleX = 2.00393f,
            scaleY = 2.00393f,
            translationX = -101.87523f,
            translationY = -10.58313f,
        )
    })

    parallel({
        delay(1000)
        leadActor.selected = true
    }, {
        // zoom on the front of the chip
        layer0.animateTo(
            rotationX = 0f,
            rotationY = 0f,
            scaleX = 2.00393f,
            scaleY = 2.00393f,
            translationX = 0f,
            translationY = 0f,
        )
    })

    flickerAllConfigs()

    // rotation and transition to another actor
    parallel(
        {
            layer0.animateTo(
                rotationZ = 180f,
                scaleX = 8f,
                scaleY = 8f,
                spec = tween(2000)
            )
        },
        {
            chipTableActor.layer().animateTo(
                rotationZ = 0f,
                scaleX = 1.5f,
                scaleY = 1.5f,
                spec = tween(2000)
            )
        },
        {
            delay(1000)
            stage.darkTheme = false
            parallel(
                { chipTableActor.layer().alpha.animateTo(1f, animationSpec = tween(1000)) },
                { layer0.alpha.animateTo(0f, animationSpec = tween(400, easing = LinearEasing)) },
            )
        }
    )

    chipTablePlot()

    parallel(
        {
            // zoom into chipTableActor's hole and hide it, transitioning to chipGroupActor
            chipTableActor.layer().animateTo(
                rotationX = -55f with tween(2000),
                scaleX = 30f with tween(4000),
                scaleY = 30f with tween(4000),
                translationY = 40f with tween(4000),
            )
        },
        {
            delay(1700)
            // hide chipTableActor
            chipTableActor.layer().animateTo(alpha = 0f, spec = tween(300, easing = LinearEasing))
        },
        {
            delay(800)
            // show chipGroupActor
            chipGroupActor.layer().animateTo(alpha = 1f, spec = tween(600))
            // zoom onto first chip in the group
            parallel(
                {
                    chipGroupActor.layer().animateTo(
                        rotationX = -18.092756f,
                        rotationY = 24.831848f,
                        rotationZ = -5f,
                        scaleX = 3.1396444f,
                        scaleY = 3.1396444f,
                        cameraDistance = 8.457f,
                        spec = tween(2700),
                    )
                },
                {
                    chipGroupActor.layer(1).animateTo(
                        translationX = 127.00368f,
                        translationY = 63.234375f,
                        spec = tween(1300),
                    )
                },
            )
        },
    )

    chipGroupPlot()

    parallel({ goToEndScreen() }, { orchestra.decreaseVolume() })
}

private suspend fun PlotScope<ChipActor, FlamingoStage>.chipTablePlot() {
    chipTableActor.apply {
        topStart.variant = ChipVariant.CONTAINED
        bottomStart.variant = ChipVariant.CONTAINED
        topEnd.variant = ChipVariant.OUTLINED
        bottomEnd.variant = ChipVariant.OUTLINED

        topStart.color = ChipColor.PRIMARY
        bottomStart.color = ChipColor.DEFAULT
        topEnd.color = ChipColor.PRIMARY
        bottomEnd.color = ChipColor.DEFAULT
    }

    chipTableActor.chips.map {
        coroutineScope.async {
            it.icon = Flamingo.icons.Moon
            delay(1000)
            it.onDelete = {}
            delay(1000)
            it.selected = true
            delay(1500)
            it.disabled = true

            delay(1000)

            it.disabled = false
            delay(1000)
            it.selected = false
            delay(1000)
            it.onDelete = null
            delay(1000)
            it.icon = null
            delay(1000)
        }
    }.awaitAll()
}

@Suppress("LongMethod")
private suspend fun PlotScope<ChipActor, FlamingoStage>.chipGroupPlot() {
    val slideDurationMs = 6_500
    parallel(
        {
            // changing selected chip
            val chipsNumber = chipGroupActor.chipsNumber
            repeat(chipsNumber) {
                chipGroupActor.selectedIndex = it
                delay((slideDurationMs.toLong() - 1000) / chipsNumber)
            }
        },
        {
            // pan the camera while sliding
            chipGroupActor.layer().animateTo(
                rotationX = 9.748912f,
                rotationY = -34.170223f,
                rotationZ = 0f,
                spec = tween(slideDurationMs)
            )
        },
        {
            // camera slide
            chipGroupActor.layer(1).animateTo(
                translationX = -123f,
                spec = tween(slideDurationMs, easing = LinearEasing)
            )
        },
    )

    // switching camera position to the view from the top
    parallel(
        {
            chipGroupActor.layer().animateTo(
                rotationX = 48f,
                rotationY = 0f,
                rotationZ = 66f,
                scaleX = 4f,
                scaleY = 4f,
                cameraDistance = 8.409751f,
                spec = tween(1300)
            )
        },
        {
            chipGroupActor.layer(1).animateTo(
                translationX = -116.251755f,
                translationY = 64.95416f,
                spec = tween(1300)
            )
        }
    )

    parallel(
        {
            // changing selected chip
            chipGroupActor.variant = ChipVariant.OUTLINED
            val chipsNumber = chipGroupActor.chipsNumber
            for (i in chipsNumber - 1 downTo 0) {
                chipGroupActor.selectedIndex = i
                delay((slideDurationMs.toLong() - 1000) / chipsNumber)
            }
        },
        {
            // camera slide
            chipGroupActor.layer(1).animateTo(
                translationX = 202f,
                spec = tween(slideDurationMs, easing = LinearEasing)
            )
        }
    )
}

private val PlotScope<ChipActor, FlamingoStage>.chipTableActor: ChipTableActor
    get() = cast.find { it is ChipTableActor } as ChipTableActor

private val PlotScope<ChipActor, FlamingoStage>.chipGroupActor: ChipGroupActor
    get() = cast.find { it is ChipGroupActor } as ChipGroupActor

private suspend fun PlotScope<ChipActor, FlamingoStage>.flickerAllConfigs() = with(leadActor) {
    colors.forEach { currentColor ->
        color = currentColor

        icon = Flamingo.icons.Moon
        delay(100)
        onDelete = {}

        selected = true
        delay(200)
        selected = false

        onDelete = null
        delay(100)
        icon = null

        delay(200)
    }
}
