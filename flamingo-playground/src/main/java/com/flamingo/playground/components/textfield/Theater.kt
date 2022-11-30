package com.flamingo.playground.components.textfield

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.components.AvatarBackground
import com.flamingo.components.AvatarContent
import com.flamingo.components.EdgeItem
import com.flamingo.components.IconPlacement
import com.flamingo.components.TextField
import com.flamingo.components.TextFieldIconAlignment
import com.flamingo.components.TextFieldSize
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

private class TextFieldActor : Actor {

    val state = TextFieldState()

    val focus by mutableStateOf(FocusRequester())

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun ActorScope.Actor() {
        val focusRequester = remember { focus }
        val kc = LocalSoftwareKeyboardController.current
        kc?.hide()
        Box(modifier = Modifier.requiredWidth(320.dp).animateContentSize().focusRequester(focusRequester)) {
            with(state) {
                TextField(
                    value = value,
                    onValueChange = { value = it },
                    label = label,
                    placeholder = placeholder,
                    required = required,
                    size = size,
                    error = error,
                    errorText = errorText,
                    helperText = helperText,
                    maxCharNumber = maxCharNumber,
                    loading = loading,
                    disabled = disabled,
                    multiline = multiline,
                    maxVisibleLines = maxVisibleLines,
                    edgeItemAreaAlignment = iconAreaAlignment,
                    edgeItem = edgeItem,
                    onClick = null,
                    bottomPadding = bottomPadding,
                )
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
        leadActor = TextFieldActor(),
        cast = listOf(
            EndScreenActor(Director.AlekseyBublyaev),
        ),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = textFieldPlot,
        backstages = listOf(
            Backstage(name = "text field", actor = TextFieldActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AlekseyBublyaev)),
        )
    )
}

private val textFieldPlot = Plot<TextFieldActor, FlamingoStage> {

    hideEndScreenActor()

    delay(1000)

    leadActor.state.placeholder = "placeholder"

    //todo think about this
    delay(1000)
    leadActor.focus.requestFocus()
    delay(1000)
    leadActor.focus.freeFocus()
    delay(1000)
    typeText()
    delay(1000)
    deleteText()
    delay(1000)

    showcaseStartEdge()

    showcaseEndEdge()

    showcaseLongText()

    showcaseSizes()

    parallel({ goToEndScreen() }, { orchestra.decreaseVolume() })
}


private suspend fun PlotScope<TextFieldActor, FlamingoStage>.typeText(
    text: String = "textField",
    delay: Long = 50
) = with(leadActor) {
    text.forEach {
        state.value = state.value + it
        delay(delay)
    }
}

private suspend fun PlotScope<TextFieldActor, FlamingoStage>.deleteText(delay: Long = 50) = with(leadActor) {
    while (state.value.isNotEmpty()) {
        state.value = state.value.dropLast(1)
        delay(delay)
    }
}

private suspend fun PlotScope<TextFieldActor, FlamingoStage>.showcaseStartEdge() {
    // zoom to start of textField
    layer0.animateTo(
        rotationX = -11f,
        rotationY = 53f,
        scaleX = 1.3456f,
        scaleY = 1.3456f,
        translationX = 119.98f,
        translationY = -32.039f,
    )

    leadActor.state.edgeItem =
        EdgeItem.TextFieldIcon(Flamingo.icons.Bell, null, IconPlacement.START, null)
    delay(1000)
    leadActor.state.edgeItem =
        EdgeItem.TextFieldAvatar(
            content = AvatarContent.Letters('A', 'A', AvatarBackground.GREEN),
            placement = IconPlacement.START
        )
    delay(1000)

    // zoom to label
    layer0.animateTo(
        rotationX = 0f,
        rotationY = 0f,
        scaleX = 3.0156f,
        scaleY = 3.0156f,
        translationX = 300f,
        translationY = 95f,
    )

    leadActor.state.label = "label"
    delay(1000)
    leadActor.state.required = true
    delay(1000)
    leadActor.state.required = false

    // zoom to helperText
    layer0.animateTo(
        rotationX = 0f,
        rotationY = 0f,
        scaleX = 3.0156f,
        scaleY = 3.0156f,
        translationX = 300f,
        translationY = -53.1f,
    )

    leadActor.state.helperText = "helperText"
    delay(1000)
}

private suspend fun PlotScope<TextFieldActor, FlamingoStage>.showcaseEndEdge() {
    // zoom to end of textField
    layer0.animateTo(
        rotationX = -11f,
        rotationY = -53f,
        scaleX = 1.3456f,
        scaleY = 1.3456f,
        translationX = -119.98f,
        translationY = -32.039f,
    )

    leadActor.state.edgeItem =
        EdgeItem.TextFieldIcon(Flamingo.icons.Bell, null, IconPlacement.END, null)
    delay(1000)
    leadActor.state.edgeItem =
        EdgeItem.TextFieldAvatar(
            content = AvatarContent.Letters('A', 'A', AvatarBackground.GREEN),
            placement = IconPlacement.END
        )
    delay(1000)
    leadActor.state.edgeItem = EdgeItem.TextFieldButton("button", {})
    delay(1000)
    leadActor.state.edgeItem = EdgeItem.TextFieldButton("button", {}, true)
    delay(1000)
    leadActor.state.edgeItem =
        EdgeItem.TextFieldIcon(Flamingo.icons.Bell, null, IconPlacement.END, null)
    delay(1000)
    leadActor.state.loading = true
    delay(1000)
    leadActor.state.loading = false

    delay(1000)
    leadActor.state.maxCharNumber = 20
    delay(1000)


    typeText()
    delay(1000)
    deleteText()
    delay(1000)
}

private suspend fun PlotScope<TextFieldActor, FlamingoStage>.showcaseLongText() {
    parallel({
        leadActor.state.maxCharNumber = null
    }, {
        //zoom out
        layer0.animateTo(
            rotationX = 0f,
            rotationY = 0f,
            scaleX = 1.374f,
            scaleY = 1.374f,
            translationX = 0f,
            translationY = 0f,
        )
    })

    leadActor.state.errorText = "errorText"
    leadActor.state.error = true
    delay(1000)
    leadActor.state.error = false
    delay(1000)

    leadActor.state.multiline = true
    typeText(loremIpsum(8).replace(" ", "\n"), 20)
    delay(300)
    leadActor.state.iconAreaAlignment = TextFieldIconAlignment.TOP
    delay(1000)
    leadActor.state.iconAreaAlignment = TextFieldIconAlignment.CENTER
    delay(1000)
    leadActor.state.iconAreaAlignment = TextFieldIconAlignment.BOTTOM
    delay(1000)

    //zoom out to showcase maxLines
    layer0.animateTo(
        rotationX = 0f,
        rotationY = 0f,
        scaleX = 1f,
        scaleY = 1f,
        translationX = 0f,
        translationY = 0f,
    )

    leadActor.state.maxVisibleLines = 40
    delay(1000)
    leadActor.state.maxVisibleLines = 4
    delay(1000)
}

private suspend fun PlotScope<TextFieldActor, FlamingoStage>.showcaseSizes() {
    //zoom in to showcase sizes
    parallel({
        layer0.animateTo(
            rotationX = 0f,
            rotationY = 0f,
            scaleX = 1.374f,
            scaleY = 1.374f,
            translationX = 0f,
            translationY = 0f,
        )
    }, {
        deleteText(20)
    })

    delay(500)
    TextFieldSize.values().forEach {
        leadActor.state.size = it
        delay(1000)
    }
    delay(1000)
}
