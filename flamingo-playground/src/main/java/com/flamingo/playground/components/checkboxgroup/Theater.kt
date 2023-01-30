package com.flamingo.playground.components.checkboxgroup

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
import com.flamingo.components.checkboxgroup.CheckBoxData
import com.flamingo.components.checkboxgroup.CheckBoxGroup
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

private class CheckBoxGroupActor : Actor {

    val checkboxList = listOf(
        CheckBoxData(label = "checkbox", checked = true, onCheckedChange = {}),
        CheckBoxData(label = "long checkbox", onCheckedChange = {}),
        CheckBoxData(
            label = "long long long long long long long long long long checkbox",
            onCheckedChange = {}),
        CheckBoxData(
            label = "long checkbox",
            checked = true,
            disabled = true,
            onCheckedChange = {}),
        CheckBoxData(label = "long long long long checkbox", disabled = true, onCheckedChange = {}),
        CheckBoxData(label = "long checkbox", onCheckedChange = {}),
    )

    var checkBoxesAmount by mutableStateOf(2)
    var label: String? by mutableStateOf(null)
    var description: String? by mutableStateOf(null)
    var errorText: String? by mutableStateOf(null)
    var required by mutableStateOf(false)
    var disabled by mutableStateOf(false)


    @Composable
    override fun ActorScope.Actor() {
        Box(modifier = Modifier.requiredWidth(400.dp)) {
            CheckBoxGroup(
                checkBoxes = checkboxList.take(checkBoxesAmount),
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
        leadActor = CheckBoxGroupActor(),
        cast = listOf(
            EndScreenActor(Director.AlekseyBublyaev),
        ),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = radioGroupPlot,
        backstages = listOf(
            Backstage(name = "Main", actor = CheckBoxGroupActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AlekseyBublyaev)),
        )
    )
}

private val radioGroupPlot = Plot<CheckBoxGroupActor, FlamingoStage> {
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

    while (leadActor.checkBoxesAmount < leadActor.checkboxList.size) {
        leadActor.checkBoxesAmount++
        delay(200)
    }

    delay(1000)
    leadActor.disabled = true
    delay(1000)
    leadActor.disabled = false
    delay(1000)

    while (leadActor.checkBoxesAmount > 2) {
        leadActor.checkBoxesAmount--
        delay(100)
    }

    delay(500)

    parallel({ goToEndScreen() }, { orchestra.decreaseVolume() })
}
