package com.flamingo.playground.components.radiogroup

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
import com.flamingo.components.radiogroup.RadioButtonData
import com.flamingo.components.radiogroup.RadioGroup
import com.flamingo.playground.theater.Director
import com.flamingo.playground.theater.EndScreenActor
import com.flamingo.playground.theater.FlamingoStage
import com.flamingo.playground.theater.goToEndScreen
import com.flamingo.playground.theater.hideEndScreenActor
import com.theater.Actor
import com.theater.ActorScope
import com.theater.Backstage
import com.theater.Plot
import com.theater.TheaterPackage
import com.theater.TheaterPlay
import kotlinx.coroutines.delay

private class RadioGroupActor : Actor {

    var selectedRadio by mutableStateOf(0)
    val listSize = 6

    var radiosAmount by mutableStateOf(2)
    var label: String? by mutableStateOf(null)
    var description: String? by mutableStateOf(null)
    var errorText: String? by mutableStateOf(null)
    var required by mutableStateOf(false)
    var disabled by mutableStateOf(false)


    @Composable
    override fun ActorScope.Actor() {
        Box(modifier = Modifier.requiredWidth(400.dp)) {
            val list = listOf(
                RadioButtonData(
                    label = "radioButton",
                    selected = selectedRadio == 0,
                    onClick = {}),
                RadioButtonData(
                    label = "long radioButton",
                    selected = selectedRadio == 1,
                    onClick = {}),
                RadioButtonData(
                    label = "long long long long long long long long long long radioButton",
                    selected = selectedRadio == 2,
                    onClick = {}),
                RadioButtonData(
                    label = "long radioButton",
                    disabled = true,
                    selected = selectedRadio == 3,
                    onClick = {}),
                RadioButtonData(
                    label = "long long long long radioButton",
                    disabled = true,
                    selected = selectedRadio == 4,
                    onClick = {}),
                RadioButtonData(
                    label = "long radioButton",
                    selected = selectedRadio == 5,
                    onClick = {}),
            )
            RadioGroup(
                radioButtons = list.take(radiosAmount),
                label = label,
                required = required,
                disabled = disabled,
                description = description,
                errorText = errorText
            )
        }
    }
}

class TheaterPkg : TheaterPackage {
    override val play: TheaterPlay<*, *> = TheaterPlay(
        stage = FlamingoStage(),
        leadActor = RadioGroupActor(),
        cast = listOf(
            EndScreenActor(Director.AlekseyBublyaev),
        ),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = radioGroupPlot,
        backstages = listOf(
            Backstage(name = "Main", actor = RadioGroupActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AlekseyBublyaev)),
        )
    )
}

private val radioGroupPlot = Plot<RadioGroupActor, FlamingoStage> {
    hideEndScreenActor()

    delay(2000)

    layer0.animateTo(
        translationY = 146.6f,
        translationX = 320f,
        scaleX = 2.63f,
        scaleY = 2.63f,
        spec = tween(2000)
    )

    leadActor.label = "label"
    delay(1000)
    leadActor.required = true
    delay(1000)

    layer0.animateTo(
        translationY = -186f,
        spec = tween(1000)
    )

    leadActor.description = "description"
    delay(1000)
    leadActor.errorText = "error text"
    delay(1000)

    layer0.animateTo(
        translationY = 0f,
        translationX = 0f,
        scaleY = 0.5f,
        scaleX = 0.5f,
        spec = tween(1000)
    )

    while (leadActor.radiosAmount < leadActor.listSize) {
        leadActor.radiosAmount++
        delay(200)
    }

    delay(1000)
    leadActor.disabled = true
    delay(1000)
    leadActor.disabled = false
    delay(1000)

    leadActor.selectedRadio = 2
    leadActor.description = " " // needed to display selection
    delay(500)
    leadActor.selectedRadio = 1
    leadActor.description = "  " // needed to display selection
    delay(500)
    leadActor.selectedRadio = 5
    leadActor.description = "   " // needed to display selection
    delay(1000)

    while (leadActor.radiosAmount > 2) {
        leadActor.radiosAmount--
        delay(100)
    }

    delay(500)

    parallel({ goToEndScreen() }, { orchestra.decreaseVolume() })
}
