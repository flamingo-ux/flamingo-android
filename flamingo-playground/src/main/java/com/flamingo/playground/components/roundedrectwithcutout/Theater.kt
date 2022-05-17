@file:Suppress("MagicNumber", "MatchingDeclarationName")

package com.flamingo.playground.components.roundedrectwithcutout

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
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
import com.flamingo.demoapi.R as DemoR

private class RoundedRectWithCutoutShapeActor : Actor {
    var rectWidth = Animatable(120f)
    var rectHeight = Animatable(80f)
    var cornerRadius = Animatable(30f)
    var circleRadius = Animatable(6f)
    var cutoutRadiusPadding = Animatable(2f)

    @Composable
    override fun ActorScope.Actor() = RoundedRectWithCutoutShapeSample(
        rectSize = DpSize(rectWidth.value.dp, rectHeight.value.dp),
        cornerRadius = cornerRadius.value.dp,
        circleRadius = circleRadius.value.dp,
        cutoutRadiusPadding = cutoutRadiusPadding.value.dp,
    )
}

private class BackgroundImageActor : Actor {
    @Composable
    override fun ActorScope.Actor() = Image(
        modifier = Modifier
            .requiredSize(300.dp)
            .clip(CircleShape),
        painter = painterResource(id = DemoR.drawable.example_dog),
        contentDescription = null
    )
}

class TheaterPkg : TheaterPackage {
    override val play: TheaterPlay<*, *> = TheaterPlay(
        stage = FlamingoStage(),
        leadActor = RoundedRectWithCutoutShapeActor(),
        cast = listOf(
            BackgroundImageActor(),
            EndScreenActor(Director.AntonPopov)
        ),
        sizeConfig = TheaterPlay.SizeConfig(density = Density(4f)),
        plot = plot,
        backstages = listOf(
            Backstage(name = "Main", actor = RoundedRectWithCutoutShapeActor()),
            Backstage(name = "End screen", actor = EndScreenActor(Director.AntonPopov)),
        )
    )
}

private val plot = Plot<RoundedRectWithCutoutShapeActor, FlamingoStage> {
    val backgroundImageActor = actorOfType<BackgroundImageActor>()

    hideEndScreenActor()
    backgroundImageActor.layer().alpha.snapTo(0f)

    delay(2000)

    leadActor.cornerRadius.animateTo(-5f, animationSpec = tween(1500))
    leadActor.circleRadius.animateTo(15f, animationSpec = tween(1500))
    leadActor.cornerRadius.animateTo(60f, animationSpec = tween(1500))
    leadActor.circleRadius.animateTo(-10f, animationSpec = tween(1500))
    leadActor.circleRadius.animateTo(8f, animationSpec = tween(1500))
    leadActor.rectWidth.animateTo(80f, animationSpec = tween(1500))
    leadActor.cutoutRadiusPadding.animateTo(52f, animationSpec = tween(1500))
    leadActor.cutoutRadiusPadding.animateTo(0f, animationSpec = tween(1800))
    leadActor.cutoutRadiusPadding.animateTo(2f, animationSpec = tween(800))

    parallel(
        { leadActor.rectWidth.animateTo(15f, animationSpec = tween(1500)) },
        { leadActor.rectHeight.animateTo(30f, animationSpec = tween(1500)) },
    )
    parallel(
        { leadActor.rectWidth.animateTo(120f, animationSpec = tween(1500)) },
        { leadActor.rectHeight.animateTo(80f, animationSpec = tween(1500)) },
    )

    backgroundImageActor.layer().alpha.animateTo(0.7f, tween(700))

    parallel(
        { leadActor.circleRadius.animateTo(12f, animationSpec = tween(1500)) },
        { leadActor.cutoutRadiusPadding.animateTo(20f, animationSpec = tween(1500)) },
    )

    backgroundImageActor.layer().rotationZ.animateTo(360f, tween(5000, easing = LinearEasing))

    parallel(
        { leadActor.circleRadius.animateTo(6f, animationSpec = tween(800)) },
        { leadActor.cutoutRadiusPadding.animateTo(2f, animationSpec = tween(800)) },
        { backgroundImageActor.layer().alpha.animateTo(0f, tween(800)) },
    )

    delay(800)

    // outro
    parallel(
        {
            layer1.animateTo(
                scaleX = 4f,
                scaleY = 4f,
                translationX = -472.5536f,
                translationY = -377.33588f,
                spec = tween(2500)
            )
        },
        {
            layer0.animateTo(
                rotationZ = -270f,
                spec = tween(3500)
            )
        },
        {
            delay(2000)
            parallel(
                { goToEndScreen(spec = tween(1500)) },
                { orchestra.decreaseVolume() },
            )
        },
    )
}
