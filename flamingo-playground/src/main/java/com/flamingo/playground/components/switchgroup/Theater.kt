package com.flamingo.playground.components.switchgroup

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
import com.flamingo.components.switchgroup.SwitchData
import com.flamingo.components.switchgroup.SwitchGroup
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

private class SwitchGroupActor : Actor {

    val switchList = listOf(
        SwitchData(label = "switch", checked = true, onCheckedChange = {}),
        SwitchData(label = "long switch", onCheckedChange = {}),
        SwitchData(
            label = "long long long long long long long long long long switch",
            onCheckedChange = {}),
        SwitchData(label = "long switch", checked = true, disabled = true, onCheckedChange = {}),
        SwitchData(label = "long long long long switch", disabled = true, onCheckedChange = {}),
        SwitchData(label = "long switch", onCheckedChange = {}),
    )

    var switchesAmount by mutableStateOf(2)
    var label: String? by mutableStateOf(null)
    var description: String? by mutableStateOf(null)
    var errorText: String? by mutableStateOf(null)
    var required by mutableStateOf(false)
    var disabled by mutableStateOf(false)


    @Composable
    override fun ActorScope.Actor() {
        Box(modifier = Modifier.requiredWidth(300.dp)) {
            SwitchGroup(
                switches = switchList.take(switchesAmount),
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
        leadActor = SwitchGroupActor(),
        cast = listOf(
            EndScreenActor(Director.AlekseyBublyaev),
        ),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = chipGroupPlot,
        backstages = listOf(
            Backstage(name = "Main", actor = SwitchGroupActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AlekseyBublyaev)),
        )
    )
}

private val chipGroupPlot = Plot<SwitchGroupActor, FlamingoStage> {
    hideEndScreenActor()

    delay(2000)

    layer0.animateTo(
        translationY = 124f,
        translationX = 185f,
        scaleX = 2.63f,
        scaleY = 2.63f,
        spec = tween(2000)
    )

    leadActor.label = "label"
    delay(1000)
    leadActor.required = true
    delay(1000)

    layer0.animateTo(
        translationY = -169f,
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

    while (leadActor.switchesAmount < leadActor.switchList.size) {
        leadActor.switchesAmount++
        delay(200)
    }

    delay(1000)
    leadActor.disabled = true
    delay(1000)
    leadActor.disabled = false
    delay(1000)

    while (leadActor.switchesAmount > 2) {
        leadActor.switchesAmount--
        delay(100)
    }

    delay(500)

    parallel({ goToEndScreen() }, { orchestra.decreaseVolume() })
}
