@file:Suppress(
    "ModifierOrdering",
    "CommentSpacing",
    "NoBlankLineBeforeRbrace",
    "ModifierOrdering",
    "NoConsecutiveBlankLines",
    "MagicNumber",
    "FunctionName",
    "MatchingDeclarationName",
    "LongParameterList",
    "SpacingAroundParens"
)

package com.theater

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.CancellationException
import timber.log.Timber

@Composable
public fun Theater(
    theaterPackage: TheaterPackage,
    onPlayEnd: () -> Unit,
    enforceSize: Boolean = true,
    showStageBorder: Boolean = true,
): Unit = Theater(theaterPackage, theaterPackage.play, onPlayEnd, enforceSize, showStageBorder)

@Composable
private fun <LA : Actor, S : Stage> Theater(
    theaterPackage: TheaterPackage,
    play: TheaterPlay<LA, S>,
    onPlayEnd: () -> Unit,
    enforceSize: Boolean = true,
    showStageBorder: Boolean = true,
): Unit = ProvideDensity(theaterPackage.play, enforceSize) { density, width, height ->
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val actorScope = remember { ActorScopeImpl(theaterPackage) }
    val knobs = remember { List(play.cast.size + 1) { List(CONTROL_LAYERS_FOR_ACTOR) { Knobs() } } }
    val plotScope = remember {
        PlotScope(play.stage, play.cast, knobs, density, coroutineScope, play.leadActor, context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .run { if (showStageBorder) border(Dp.Hairline, Color.Red) else this }
            .requiredSize(width = width, height = height)
            .clip(RectangleShape),
        contentAlignment = Alignment.Center
    ) {
        with(play.stage) {
            Stage {
                Box(
                    modifier = Modifier.requiredSize(
                        width = width * play.sizeConfig.stageSizeMultiplier,
                        height = height * play.sizeConfig.stageSizeMultiplier,
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    val reversedKnobs = knobs.reversed()
                    val cast = play.cast.toMutableList()
                    cast.add(0, play.leadActor)
                    cast.reversed().forEachIndexed { index, actor ->
                        ControlLayer(knobsMirror = reversedKnobs[index][0].mirror) {
                            ControlLayer(knobsMirror = reversedKnobs[index][1].mirror) {
                                with(actor) { actorScope.Actor() }
                            }
                        }
                    }
                    plotScope.DebugMarkers()
                }
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    LaunchedEffect(true) {
        try {
            with(play.plot) { plotScope.plot() }
        } catch (t: Throwable) {
            if (t !is CancellationException) {
                Timber.e(t)
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        } finally {
            plotScope.onPlotEnd()
            onPlayEnd()
        }
    }
}

@Composable
private fun ControlLayer(knobsMirror: KnobsMirror, content: @Composable BoxScope.() -> Unit) = Box(
    modifier = Modifier.graphicsLayer(knobsMirror),
    contentAlignment = Alignment.Center,
    content = content
)

private const val CONTROL_LAYERS_FOR_ACTOR: Int = 2
