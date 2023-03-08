package com.flamingo.playground.components.link

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Density
import com.flamingo.Flamingo
import com.flamingo.components.CheckBoxState
import com.flamingo.components.Checkbox
import com.flamingo.components.Link
import com.flamingo.components.LinkSize
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
import com.theater.TheaterPackage
import com.theater.TheaterPlay
import kotlinx.coroutines.delay

private class LinkActor : Actor {

    var disabled: Boolean by mutableStateOf(false)
    var loading: Boolean by mutableStateOf(false)
    var label by mutableStateOf(loremIpsum(2))
    var size: LinkSize by mutableStateOf(LinkSize.NORMAL)
    var startIcon: FlamingoIcon? by mutableStateOf(null)
    var endIcon: FlamingoIcon? by mutableStateOf(null)

    @Composable
    override fun ActorScope.Actor() {
        Link(
            label = label,
            onClick = { },
            size = size,
            color = Flamingo.colors.primary,
            loading = loading,
            disabled = disabled,
            startIcon = startIcon,
            endIcon = endIcon
        )
    }
}

class TheaterPkg : TheaterPackage {
    override val play: TheaterPlay<*, *> = TheaterPlay(
        stage = FlamingoStage(),
        leadActor = LinkActor(),
        cast = listOf(
            EndScreenActor(Director.AlekseyBublyaev),
        ),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = linkPlot,
        backstages = listOf(
            Backstage(name = "Link", actor = LinkActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AlekseyBublyaev)),
        )
    )
}

private val linkPlot = Plot<LinkActor, FlamingoStage> {
    hideEndScreenActor()
    delay(1000)

    layer0.animateTo(
        rotationX = 0f,
        rotationY = 46f,
        scaleX = 2.13f,
        scaleY = 2.13f,
        translationX = 99f,
        translationY = 0f,
    )

    leadActor.startIcon = Flamingo.icons.Download
    delay(1000)
    leadActor.loading = true
    delay(1000)
    leadActor.loading = false
    delay(1000)

    layer0.animateTo(
        rotationX = 0f,
        rotationY = -46f,
        scaleX = 2.13f,
        scaleY = 2.13f,
        translationX = -99f,
        translationY = 0f,
    )

    leadActor.endIcon = Flamingo.icons.Share
    delay(1000)
    leadActor.loading = true
    delay(1000)
    leadActor.loading = false
    delay(1000)

    layer0.animateTo(
        rotationX = 0f,
        rotationY = 0f,
        scaleX = 2.6f,
        scaleY = 2.6f,
        translationX = 0f,
        translationY = 0f,
    )

    leadActor.size = LinkSize.SMALL
    delay(1000)
    leadActor.size = LinkSize.NORMAL
    delay(1000)

    parallel({ goToEndScreen() }, { orchestra.decreaseVolume() })
}
