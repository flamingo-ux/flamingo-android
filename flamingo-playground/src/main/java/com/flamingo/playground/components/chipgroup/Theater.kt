package com.flamingo.playground.components.chipgroup

import androidx.compose.animation.core.snap
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
import com.flamingo.Flamingo
import com.flamingo.components.chipgroup.ChipData
import com.flamingo.components.chipgroup.ChipGroup
import com.flamingo.components.dropdown.DropdownItem
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

private class ChipGroupActor : Actor {

    val chipsList = listOf(
        ChipData(label = "Chip", onClick = {}, disabled = true),
        ChipData(label = "Long Long Chip", onClick = {}),
        ChipData(label = "Long Chip", onClick = {}, disabled = true, onDelete = {}),
        ChipData(label = "Long Long Long Long Long Long Long Long Chip", onClick = {}),
        ChipData(
            label = "dropdown Chip", onClick = {}, selected = true, dropdownItems = listOf(
                DropdownItem(label = "item 1"),
                DropdownItem(label = "item 2", icon = Flamingo.icons.Bell),
                DropdownItem(label = "long long item 3"),
            )
        ),
        ChipData(label = "Long Long Long Chip", onClick = {}),
        ChipData(label = "Chip", icon = Flamingo.icons.Bell, onClick = {}),
        ChipData(label = "Chip", onClick = {}),
        ChipData(
            label = "Long Long Chip",
            icon = Flamingo.icons.Aperture,
            onDelete = {},
            onClick = {}),
        ChipData(label = "Long Chip", onClick = {}),
    )

    var chipsAmount by mutableStateOf(2)
    var label: String? by mutableStateOf(null)
    var description: String? by mutableStateOf(null)
    var errorText: String? by mutableStateOf(null)
    var required by mutableStateOf(false)
    var disabled by mutableStateOf(false)


    @Composable
    override fun ActorScope.Actor() {
        Box(modifier = Modifier.requiredWidth(500.dp)) {
            ChipGroup(
                chips = chipsList.take(chipsAmount),
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
        leadActor = ChipGroupActor(),
        cast = listOf(
            EndScreenActor(Director.AlekseyBublyaev),
        ),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = chipGroupPlot,
        backstages = listOf(
            Backstage(name = "Main", actor = ChipGroupActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AlekseyBublyaev)),
        )
    )
}

private val chipGroupPlot = Plot<ChipGroupActor, FlamingoStage> {
    hideEndScreenActor()

    layer0.animateTo(
        scaleY = 0.9f,
        scaleX = 0.9f,
        spec = snap()
    )

    delay(2000)

    layer0.animateTo(
        translationY = 46.6f,
        translationX = 458f,
        scaleX = 2.63f,
        scaleY = 2.63f,
        spec = tween(2000)
    )

    leadActor.label = "label"
    delay(1000)
    leadActor.required = true
    delay(1000)

    layer0.animateTo(
        translationY = -98f,
        spec = tween(1000)
    )

    leadActor.description = "description"
    delay(1000)
    leadActor.errorText = "error text"
    delay(1000)

    layer0.animateTo(
        translationY = 0f,
        translationX = 0f,
        scaleY = 0.9f,
        scaleX = 0.9f,
        spec = tween(1000)
    )

    while (leadActor.chipsAmount < leadActor.chipsList.size) {
        leadActor.chipsAmount++
        delay(200)
    }

    delay(1000)
    leadActor.disabled = true
    delay(1000)
    leadActor.disabled = false
    delay(1000)

    while (leadActor.chipsAmount > 2) {
        leadActor.chipsAmount--
        delay(100)
    }

    delay(500)

    parallel({ goToEndScreen() }, { orchestra.decreaseVolume() })
}
