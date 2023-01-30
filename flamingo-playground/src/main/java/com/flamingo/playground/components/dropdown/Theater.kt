package com.flamingo.playground.components.dropdown

import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Density
import com.flamingo.Flamingo
import com.flamingo.components.dropdown.BaseDropdownComponent
import com.flamingo.components.dropdown.Dropdown
import com.flamingo.components.dropdown.DropdownItem
import com.flamingo.playground.internalComponents
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

private class DropdownActor : Actor {

    private val items = listOf(
        DropdownItem(label = "item"),
        DropdownItem(label = "long long long long long long long item"),
        DropdownItem(label = "icon item", icon = Flamingo.icons.Bell),
        DropdownItem(label = "disabled item", disabled = true),
    )

    var baseComponent: BaseDropdownComponent by mutableStateOf(BaseDropdownComponent.Button("button"))
    var isDropdownShown by mutableStateOf(false)

    @Composable
    override fun ActorScope.Actor() {
        internalComponents.Dropdown(
            baseDropdownComponent = baseComponent,
            items = items,
            onDropdownItemSelected = {},
            isDropdownShown = isDropdownShown
        )
    }
}

class TheaterPkg : TheaterPackage {
    override val play: TheaterPlay<*, *> = TheaterPlay(
        stage = FlamingoStage(),
        leadActor = DropdownActor(),
        cast = listOf(
            EndScreenActor(Director.AlekseyBublyaev),
        ),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = dropdownPlot,
        backstages = listOf(
            Backstage(name = "Main", actor = DropdownActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AlekseyBublyaev)),
        )
    )
}

private val dropdownPlot = Plot<DropdownActor, FlamingoStage> {
    hideEndScreenActor()

    layer0.animateTo(translationX = -150f, translationY = -50f)

    leadActor.isDropdownShown = true
    delay(1500)
    leadActor.isDropdownShown = false
    delay(1000)

    parallel({
        layer0.animateTo(translationX = -50f, spec = tween(1000))
    }, {
        delay(300)
        leadActor.baseComponent = BaseDropdownComponent.Chip("chip")
    })

    delay(300)
    leadActor.isDropdownShown = true
    delay(1500)
    leadActor.isDropdownShown = false
    delay(1000)

    parallel({
        layer0.animateTo(translationX = 50f, spec = tween(1000))
    }, {
        delay(300)
        leadActor.baseComponent = BaseDropdownComponent.IconButton(Flamingo.icons.Menu)
    })

    delay(300)
    leadActor.isDropdownShown = true
    delay(1500)
    leadActor.isDropdownShown = false
    delay(1000)

    parallel({ goToEndScreen() }, { orchestra.decreaseVolume() })
}
