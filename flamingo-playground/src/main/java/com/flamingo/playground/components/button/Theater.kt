package com.flamingo.playground.components.button

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.button.ButtonEndItem
import com.flamingo.components.button.ButtonSize
import com.flamingo.components.button.ButtonVariant
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.components.listitem.ListItem
import com.flamingo.loremIpsum
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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay

private class ButtonActor : Actor {
    var variant by mutableStateOf(startVariant)
    var loading by mutableStateOf(false)
    var size by mutableStateOf(startSize)
    var color: ButtonColor by mutableStateOf(startColor)
    var startIcon: FlamingoIcon? by mutableStateOf(null)
    var endItem: ButtonEndItem? by mutableStateOf(null)
    var disabled by mutableStateOf(false)
    var fillMaxWidth by mutableStateOf(false)
    var widthPolicy by mutableStateOf(ButtonWidthPolicy.MULTILINE)
    var modifier: Modifier by mutableStateOf(Modifier)
    var words by mutableStateOf(2)

    @Composable
    override fun ActorScope.Actor() {
        val labelCount by animateIntAsState(targetValue = words, tween(2000))
        Box(modifier = modifier) {
            Button(
                onClick = {},
                label = loremIpsum(labelCount),
                loading = loading,
                size = size,
                color = color,
                variant = variant,
                startIcon = startIcon,
                endItem = endItem,
                disabled = disabled,
                fillMaxWidth = fillMaxWidth,
                widthPolicy = widthPolicy
            )
        }
    }
}

private class ButtonTableActor : Actor {
    val topStart = ButtonActor()
    val topCenter = ButtonActor()
    val topEnd = ButtonActor()
    val bottomStart = ButtonActor()
    val bottomCenter = ButtonActor()
    val bottomEnd = ButtonActor()
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

private class ButtonFullWidthActor : Actor {
    val button = ButtonActor()

    private val exampleListItemText = loremIpsum(9)
    private val listItemsNumber = 4

    @Composable
    override fun ActorScope.Actor() {
        CompositionLocalProvider(LocalDensity provides Density(densityMultiplier * 2)) {
            Column(
                modifier = Modifier.requiredWidth(640.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                repeat(listItemsNumber) {
                    ListItem(
                        title = "Item $it, $exampleListItemText",
                        subtitle = "Subtitle $it, $exampleListItemText",
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    with(button) { Actor() }
                }
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
        leadActor = ButtonActor(),
        cast = listOf(
            EndScreenActor(Director.AlekseyBublyaev),
            ButtonTableActor(),
            ButtonFullWidthActor()
        ),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = buttonPlot,
        backstages = listOf(
            Backstage(name = "Button", actor = ButtonActor()),
            Backstage(name = "Button table", actor = ButtonTableActor()),
            Backstage(name = "Button full width", actor = ButtonFullWidthActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AlekseyBublyaev)),
        )
    )
}

private val buttonPlot = Plot<ButtonActor, FlamingoStage> {
    hideEndScreenActor()

    // This music cannot be included in the apk because of the licensing issues
    // orchestra.playMusic(R.raw.magnusthemagnus_area)

    stage.darkTheme = true

    buttonTableActor.layer().animateTo(
        alpha = 0f,
        rotationZ = -180f,
        spec = snap()
    )
    buttonFullWidthActor.layer().alpha.snapTo(0f)

    buttonTableActor.apply {
        topStart.variant = ButtonVariant.CONTAINED
        bottomStart.variant = ButtonVariant.TEXT
        topCenter.variant = ButtonVariant.TEXT
        bottomCenter.variant = ButtonVariant.CONTAINED
        topEnd.variant = ButtonVariant.CONTAINED
        bottomEnd.variant = ButtonVariant.TEXT

        topStart.color = ButtonColor.Primary
        bottomStart.color = ButtonColor.Default
        topCenter.color = ButtonColor.Warning
        bottomCenter.color = ButtonColor.Error
        topEnd.color =
            if (internalColors.isNotEmpty()) internalColors.first() else ButtonColor.Primary
        bottomEnd.color =
            if (internalColors.isNotEmpty()) internalColors.last() else ButtonColor.Primary

        topStart.size = ButtonSize.SMALL
        bottomStart.size = ButtonSize.SMALL
        topCenter.size = ButtonSize.MEDIUM
        bottomCenter.size = ButtonSize.MEDIUM
        topEnd.size = ButtonSize.LARGE
        bottomEnd.size = ButtonSize.LARGE
    }

    delay(2000)


    parallel({
        delay(700)
        leadActor.startIcon = Flamingo.icons.Airplay
    }, {
        // zoom on the start icon
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
        leadActor.endItem = ButtonEndItem.Icon(Flamingo.icons.Android)
        delay(1000)
        leadActor.endItem = ButtonEndItem.Badge("badge")
        delay(1000)
    }, {
        // zoom on the end item
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
        leadActor.loading = true
        delay(2000)
        leadActor.loading = false
    }, {
        // zoom on the front of the button
        layer0.animateTo(
            rotationX = 0f,
            rotationY = 0f,
            scaleX = 1.60393f,
            scaleY = 1.60393f,
            translationX = 0f,
            translationY = 0f,
        )
    })

    flickThroughColorVariants()
    delay(1000)
    leadActor.variant = ButtonVariant.TEXT
    delay(1000)
    flickThroughColorVariants()
    delay(1000)

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
            buttonTableActor.layer().animateTo(
                rotationZ = 0f,
                scaleX = 0.7f,
                scaleY = 0.7f,
                spec = tween(2000)
            )
        },
        {
            delay(1000)
            stage.darkTheme = false
            parallel(
                { buttonTableActor.layer().alpha.animateTo(1f, animationSpec = tween(1000)) },
                { layer0.alpha.animateTo(0f, animationSpec = tween(400, easing = LinearEasing)) },
            )
        }
    )

    buttonTablePlot()
    delay(2000)

    parallel({
        // zoom out buttonTableActor
        buttonTableActor.layer().animateTo(
            rotationX = 0f,
            scaleX = 0f,
            scaleY = 0f,
            translationY = 0f,
        )
    }, {
        delay(1000)
        // hide buttonTableActor
        buttonTableActor.layer().animateTo(alpha = 0f, spec = tween(300, easing = LinearEasing))
    }, {
        delay(800)
        // show buttonFullWidthActor
        buttonFullWidthActor.layer().animateTo(alpha = 1f, spec = tween(600))
    })

    buttonFullWidthPlot()

    parallel({ goToEndScreen() }, { orchestra.decreaseVolume() })
}

private suspend fun PlotScope<ButtonActor, FlamingoStage>.buttonTablePlot() {

    buttonTableActor.buttons.map {
        coroutineScope.async {
            delay(1000)
            it.startIcon = Flamingo.icons.Airplay
            delay(1000)
            it.endItem = ButtonEndItem.Icon(Flamingo.icons.Android)
            delay(1000)
            it.endItem = ButtonEndItem.Badge("badge")
            delay(3000)
            it.startIcon = null
            delay(1000)
            it.endItem = null
        }
    }.awaitAll()
}

private suspend fun PlotScope<ButtonActor, FlamingoStage>.buttonFullWidthPlot() {
    // zoom on button
    buttonFullWidthActor.layer().animateTo(
        rotationX = 68f,
        rotationY = 0f,
        scaleX = 2.01516f,
        scaleY = 2.01516f,
        translationX = 0f,
        translationY = -106.05032f,
    )

    //buttonFullWidthActor.button.modifier = Modifier.animateContentSize()

    parallel({
        buttonFullWidthActor.button.words = 15
        delay(3000)
        buttonFullWidthActor.button.widthPolicy = ButtonWidthPolicy.TRUNCATING
        delay(1000)
        buttonFullWidthActor.button.widthPolicy = ButtonWidthPolicy.MULTILINE
        delay(2000)
    }, {
        // zoom out on fullwidth
        buttonFullWidthActor.layer().animateTo(
            rotationX = 36f,
            rotationY = 0f,
            scaleX = 1.2273389f,
            scaleY = 1.2273389f,
            translationX = 0f,
            translationY = -106.05032f,
        )
    })
}

private val PlotScope<ButtonActor, FlamingoStage>.buttonTableActor: ButtonTableActor
    get() = cast.find { it is ButtonTableActor } as ButtonTableActor

private val PlotScope<ButtonActor, FlamingoStage>.buttonFullWidthActor: ButtonFullWidthActor
    get() = cast.find { it is ButtonFullWidthActor } as ButtonFullWidthActor

private suspend fun PlotScope<ButtonActor, FlamingoStage>.flickThroughColorVariants() {
    with(leadActor) {
        colors.forEach { newColor ->
            color = newColor
            delay(1000)
        }
    }
}

private val startVariant = ButtonVariant.CONTAINED
private val variants = ButtonVariant.values().filterNot { it == startVariant } + startVariant

private val startSize = ButtonSize.MEDIUM
private val sizes = ButtonSize.values().filterNot { it == startSize } + startSize

private val startColor = ButtonColor.Primary
private val colors =
    ButtonColor::class.sealedSubclasses.filterNot {
        it == ButtonColor.Primary::class || it == ButtonColor.TopAppBar::class
    }.map { it.objectInstance!! } + startColor
private val availableColors = listOf(
    ButtonColor.Default,
    ButtonColor.Error,
    ButtonColor.Primary,
    ButtonColor.White,
    ButtonColor.Warning,
    ButtonColor.TopAppBar
)
private val internalColors = colors.filter { !availableColors.contains(it) }
