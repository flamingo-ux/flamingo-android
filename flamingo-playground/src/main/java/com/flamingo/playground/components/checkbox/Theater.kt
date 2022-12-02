package com.flamingo.playground.components.checkbox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Density
import com.flamingo.components.CheckBoxState
import com.flamingo.components.Checkbox
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

private class CheckboxActor : Actor {

    var checked: Boolean by mutableStateOf(false)
    var disabled: Boolean by mutableStateOf(false)
    var state: CheckBoxState by mutableStateOf(CheckBoxState.DEFAULT)

    @Composable
    override fun ActorScope.Actor() {
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it },
            disabled = disabled,
            state = state
        )
    }
}

class TheaterPkg : TheaterPackage {
    override val play: TheaterPlay<*, *> = TheaterPlay(
        stage = FlamingoStage(),
        leadActor = CheckboxActor(),
        cast = listOf(
            EndScreenActor(Director.AlekseyBublyaev),
        ),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = checkboxPlot,
        backstages = listOf(
            Backstage(name = "Checkbox", actor = CheckboxActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AlekseyBublyaev)),
        )
    )
}

private val checkboxPlot = Plot<CheckboxActor, FlamingoStage> {
    hideEndScreenActor()

    delay(1000)

    layer0.animateTo(
        rotationX = 0f,
        rotationY = -42f,
        scaleX = 4.4f,
        scaleY = 4.4f,
        translationX = 0f,
        translationY = 0f,
    )

    leadActor.checked = true
    delay(1000)
    leadActor.checked = false
    delay(1000)
    leadActor.checked = true
    delay(1000)
    leadActor.checked = false
    delay(1000)
    leadActor.disabled = true
    delay(1000)
    leadActor.disabled = false
    delay(1000)
    leadActor.checked = true
    delay(1000)
    leadActor.disabled = true
    delay(1000)
    leadActor.disabled = false
    delay(1000)


    parallel({
        leadActor.state = CheckBoxState.MINUS
        delay(500)
        leadActor.state = CheckBoxState.DEFAULT
        delay(500)
        leadActor.state = CheckBoxState.MINUS
    }, {
        layer0.animateTo(
            rotationX = 0f,
            rotationY = 42f,
            scaleX = 4.4f,
            scaleY = 4.4f,
            translationX = 0f,
            translationY = 0f,
        )
    })


    leadActor.checked = true
    delay(1000)
    leadActor.checked = false
    delay(1000)
    leadActor.checked = true
    delay(1000)
    leadActor.checked = false
    delay(1000)
    leadActor.disabled = true
    delay(1000)
    leadActor.disabled = false
    delay(1000)
    leadActor.checked = true
    delay(1000)
    leadActor.disabled = true
    delay(1000)
    leadActor.disabled = false
    delay(1000)

    parallel({ goToEndScreen() }, { orchestra.decreaseVolume() })
}
