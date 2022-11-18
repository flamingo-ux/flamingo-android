@file:Suppress("MagicNumber", "MatchingDeclarationName")

package com.flamingo.playground.components.tabrow

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.flamingo.Flamingo
import com.flamingo.components.BadgeColor
import com.flamingo.components.tabrow.Tab
import com.flamingo.components.tabrow.TabRow
import com.flamingo.playground.theater.Director
import com.flamingo.playground.theater.EndScreenActor
import com.flamingo.playground.theater.FlamingoStage
import com.flamingo.playground.theater.goToEndScreen
import com.flamingo.playground.theater.hideEndScreenActor
import com.flamingo.theme.FlamingoTheme
import com.flamingo.theme.colors.FlamingoColors
import com.theater.Actor
import com.theater.ActorScope
import com.theater.Backstage
import com.theater.Plot
import com.theater.TheaterPackage
import com.theater.TheaterPlay

private val tabLabels = listOf(
    "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven",
    "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
    "twenty",
).map { it.replaceFirstChar { it.titlecaseChar() } }

private val allTabs = tabLabels.take(10).mapIndexed { index, label ->
    Tab(
        label,
        disabled = index == 5,
        badge = if (index == 8) {
            Tab.Badge(label = "9 is the best!", color = BadgeColor.Warning)
        } else null
    )
}

private val contentPaneColors = with(Flamingo.palette) {
    listOf(
        green200, arctic200, blue200, electricBlue200, orhid200, pink200, red200, orange200,
        yellow200, spring200, grey200, coolGrey200,
    )
}

private class TabRowActor : Actor {

    var tabs = mutableStateListOf<Tab>().apply { addAll(tabLabels.take(2).map(::Tab)) }
    var selectedTabIndex: Int by mutableStateOf(0)
    var isContentPaneVisible by mutableStateOf(false)

    /**
     * Used in combination with [FlamingoStage.darkTheme] because of a strange bug:
     * [FlamingoColors.backgroundTextField] is not rendered correctly in the dark theme.
     */
    var darkTheme by mutableStateOf(false)

    private val contentPaneHeight = 200.dp
    private val edgePadding = 16.dp

    @Composable
    override fun ActorScope.Actor() {
        Box(modifier = Modifier.requiredWidth(1060.dp)) {
            Column {
                /** mirror of the content pane to preserve centered [TransformOrigin] */
                if (isContentPaneVisible) Box(
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .requiredHeight(contentPaneHeight),
                )
                if (darkTheme) FlamingoTheme(true) { TabRow() } else TabRow()
                ContentPane()
            }
        }
    }

    @Composable
    private fun TabRow() {
        TabRow(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelect = {},
        )
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun ContentPane() {
        AnimatedVisibility(
            visible = isContentPaneVisible,
            enter = fadeIn(animationSpec = tween(800))
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 24.dp, start = edgePadding, end = edgePadding)
                    .requiredSize(1020.dp, contentPaneHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .background(animateColorAsState(contentPaneColors[selectedTabIndex]).value),
            )
        }
    }
}

class TheaterPkg : TheaterPackage {
    override val play: TheaterPlay<*, *> = TheaterPlay(
        stage = FlamingoStage(),
        leadActor = TabRowActor(),
        cast = listOf(EndScreenActor(Director.AntonPopov)),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = tabRowPlot,
        backstages = listOf(
            Backstage(name = "Main", actor = TabRowActor().apply {
                tabs.apply {
                    clear()
                    addAll(allTabs)
                }
                isContentPaneVisible = true
            }),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AntonPopov)),
        )
    )
}

private val tabRowPlot = Plot<TabRowActor, FlamingoStage> {
    hideEndScreenActor()
    layer0.animateTo(
        scaleX = 1.2086357f,
        scaleY = 1.2086357f,
        spec = snap(),
    )
    layer1.animateTo(
        translationX = 425f,
        spec = snap(),
    )
    // This music cannot be included in the apk because of the licensing issues
    // orchestra.playMusic(R.raw.howling_at_the_moon_instrumental) {
    //     seekTo(27_000)
    // }

    delay(2000)

    parallel({
        // tilt to the opposite side
        layer0.animateTo(
            rotationX = -6f,
            rotationY = 42f,
            scaleX = 1.3222874f,
            scaleY = 1.3222874f,
            translationX = -11.214844f,
            translationY = -4.169525f,
            cameraDistance = 4.108558f,
        )
    }, {
        delay(300)
        // add tabs one by one
        allTabs.subList(2, 10).forEach {
            leadActor.tabs += it
            delay(220)
        }
    })

    // go to the opposite side
    parallel({
        layer0.animateTo(
            rotationX = -6f,
            rotationY = -29f,
            scaleX = 1.9146131f,
            scaleY = 1.9146131f,
            translationX = 173.32985f,
            cameraDistance = 3.917989f,
        )
    }, {
        layer1.animateTo(
            translationX = -465.5957f,
        )
    })

    delay(1000)

    // back to the beginning
    parallel({
        layer0.animateTo(
            rotationX = 0f,
            rotationY = 0f,
            scaleX = 1.2086357f,
            scaleY = 1.2086357f,
            translationX = 0f,
            translationY = 0f,
            cameraDistance = 4.108558f,
        )
    }, {
        layer1.animateTo(translationX = 425f)
    })

    // move tabs at the top
    parallel({
        layer0.animateTo(
            scaleX = 1.2062719f,
            scaleY = 1.2062719f,
            spec = tween(2000)
        )
    }, {
        layer0.animateTo(
            translationX = -100.713806f,
            spec = tween(2000, easing = FastOutLinearInEasing)
        )
    }, {
        layer0.animateTo(
            translationY = -79.99643f,
            spec = tween(2000, easing = FastOutSlowInEasing)
        )
    })
    // and slide the content pane into existence
    leadActor.isContentPaneVisible = true

    delay(2000)

    /** angle the camera and start sliding across the [TabRow] */
    parallel({
        layer0.animateTo(
            rotationX = 40f,
            rotationY = 0f,
            rotationZ = -28f,
            scaleX = 1.3142649f,
            scaleY = 1.3142649f,
            translationX = -102.08179f,
            translationY = -79.99643f,
            cameraDistance = 12.136086f,
        )
    }, {
        layer1.animateTo(
            translationX = 455.69946f,
            translationY = 77.82837f,
        )
    })

    // stop and zoom onto the disabled tab
    val slideToDisabledTabDuration = 6000
    parallel({
        layer0.animateTo(
            rotationX = 19f,
            rotationY = -38f,
            rotationZ = 15f,
            scaleX = 1.9713053f,
            scaleY = 1.9713053f,
            translationX = -102.08179f,
            translationY = -79.99643f,
            cameraDistance = 12.0405035f,
            spec = tween(slideToDisabledTabDuration, easing = LinearEasing)
        )
    }, {
        layer1.animateTo(
            translationX = 79.6521f,
            translationY = 32.75116f,
            spec = tween(slideToDisabledTabDuration, easing = LinearEasing)
        )
    }, {
        repeat(4) {
            val i = it + 1 // first tab is already selected
            leadActor.selectedTabIndex = i
            delay(slideToDisabledTabDuration / 4L)
        }
    })

    // stop and showcase the tab with a badge
    val slideToBadgedTabDuration = 4000
    parallel({
        layer0.animateTo(
            rotationX = -21f,
            rotationY = 45f,
            rotationZ = 0f,
            scaleX = 2.3202827f,
            scaleY = 2.3202827f,
            translationX = -102.08179f,
            translationY = -79.99643f,
            spec = tween(slideToBadgedTabDuration, easing = LinearEasing)
        )
    }, {
        layer1.animateTo(
            translationX = -220.91847f,
            translationY = 45.051147f,
            spec = tween(slideToBadgedTabDuration, easing = LinearEasing)
        )
    }, {
        repeat(3) {
            val i = it + 6 // fifth tab is already selected, sixth is disabled
            leadActor.selectedTabIndex = i
            delay(slideToBadgedTabDuration / 3L)
        }
    })

    // showcasing
    parallel({
        layer0.animateTo(
            rotationX = 25f,
            rotationY = -52f,
            rotationZ = 0f,
            scaleX = 2.3202827f,
            scaleY = 2.3202827f,
            translationX = -102.08179f,
            translationY = -79.99643f,
            spec = tween(3000)
        )
    }, {
        layer1.animateTo(
            translationY = 55.323547f,
            spec = tween(3000),
        )
    })

    // still on badged tab to show dark theme and font scaling
    parallel({
        layer0.animateTo(
            rotationX = -15f,
            rotationY = 37f,
            rotationZ = 0f,
            scaleX = 1.6386402f,
            scaleY = 1.6386402f,
            translationX = 124.39441f,
            translationY = -79.99643f,
            spec = tween(2000)
        )
    }, {
        layer1.animateTo(
            scaleX = 0.8680313f,
            scaleY = 0.8680313f,
            translationX = -342.56592f,
            translationY = 28.628662f,
            spec = tween(2000)
        )
    })

    stage.fontScale.animateTo(1.6f, animationSpec = tween(1800))
    delay(1000)
    stage.fontScale.animateTo(1f, animationSpec = tween(1800))
    delay(2000)

    leadActor.darkTheme = true
    stage.darkTheme = true

    delay(4500)

    parallel(
        { goToEndScreen() },
        { orchestra.decreaseVolume() },
    )
}
