@file:Suppress(
    "ModifierOrdering",
    "CommentSpacing",
    "NoBlankLineBeforeRbrace",
    "ModifierOrdering",
    "NoConsecutiveBlankLines",
    "MagicNumber",
    "FunctionName",
    "MatchingDeclarationName",
    "LongParameterList",
    "LongMethod",
    "SpacingAroundParens"
)

package com.flamingo.components

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.R
import com.flamingo.Say
import com.flamingo.annotations.DelicateFlamingoApi
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import kotlin.random.Random

@FlamingoComponent(
    preview = "com.flamingo.playground.preview.BadgePreview",
    figma = "https://todo.com/file/qVO8jDuABDK9vsuLqRXeMx?node-id=628%3A8",
    specification = "https://todo.com/x/gRUdbQE",
    theaterPackage = "com.flamingo.playground.components.badge.TheaterPkg",
    demo = ["com.flamingo.playground.components.badge.BadgeStatesPlayroom"],
    supportsWhiteMode = true,
)
@UsedInsteadOf("androidx.compose.material.BadgeBox")
@Composable
public fun Badge(
    label: String,
    color: BadgeColor = if (Flamingo.isWhiteMode) BadgeColor.White else BadgeColor.Default,
    size: BadgeSize = BadgeSize.BIG,
): Unit = FlamingoComponentBase {
    if (color == BadgeColor.PickleRick) Say(color, PICKLE_PUNCHLINE)
    Box(modifier = Modifier
        .run { if (color == BadgeColor.PickleRick) pickleRick() else this }
        .clip(CircleShape)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .paint(color.toPainter(), contentScale = ContentScale.FillBounds)
        )
        UniversalText(
            modifier = Modifier
                .run {
                    when (size) {
                        BadgeSize.SMALL -> padding(horizontal = 6.dp)
                        BadgeSize.BIG -> padding(horizontal = 8.dp, vertical = 4.dp)
                    }
                },
            text = if (color == BadgeColor.PickleRick) PICKLE_LABEL else label.replace("\n", " "),
            color = calculateTextColor(color),
            style = Flamingo.typography.caption,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/** [PICKLE_VIDEO] */
@DelicateFlamingoApi
@Composable
public fun PickleRick(): Unit = Badge(label = "", color = BadgeColor.PickleRick)

public enum class BadgeSize { SMALL, BIG, }

@Immutable
public sealed interface BadgeColor {
    public object Primary : BadgeColor
    public object White : BadgeColor
    public object Default : BadgeColor
    public object Error : BadgeColor
    public object Info : BadgeColor
    public object Warning : BadgeColor

    @DelicateFlamingoApi
    public object PickleRick : BadgeColor
    public enum class Gradient(@DrawableRes internal val drawableRes: Int) : BadgeColor {
        BLUE(R.drawable.ds_gradient_blue),
        GREEN(R.drawable.ds_gradient_green),
        ORANGE(R.drawable.ds_gradient_orange),
        RED(R.drawable.ds_gradient_red),
        PURPLE(R.drawable.ds_gradient_purple),
        PINK(R.drawable.ds_gradient_pink),
        YELLOW(R.drawable.ds_gradient_yellow),
    }
}

@Composable
private fun calculateTextColor(badgeColor: BadgeColor): Color = when (badgeColor) {
    BadgeColor.Default -> Flamingo.colors.textPrimary
    BadgeColor.White -> Flamingo.palette.black
    else -> Flamingo.palette.white
}

@Suppress("ComplexMethod")
@Composable
private fun BadgeColor.toPainter(): Painter = when (this) {
    BadgeColor.Default -> ColorPainter(Flamingo.colors.backgroundTertiary)
    BadgeColor.White -> ColorPainter(Flamingo.palette.white)
    BadgeColor.Error -> ColorPainter(Flamingo.colors.error)
    BadgeColor.Info -> ColorPainter(Flamingo.colors.info)
    BadgeColor.Primary -> ColorPainter(Flamingo.colors.primary)
    BadgeColor.Warning -> ColorPainter(Flamingo.colors.warning)
    BadgeColor.PickleRick -> ColorPainter(Flamingo.colors.primary)
    BadgeColor.Gradient.BLUE -> painterResource(R.drawable.ds_gradient_blue)
    BadgeColor.Gradient.GREEN -> painterResource(R.drawable.ds_gradient_green)
    BadgeColor.Gradient.ORANGE -> painterResource(R.drawable.ds_gradient_orange)
    BadgeColor.Gradient.RED -> painterResource(R.drawable.ds_gradient_red)
    BadgeColor.Gradient.PURPLE -> painterResource(R.drawable.ds_gradient_purple)
    BadgeColor.Gradient.PINK -> painterResource(R.drawable.ds_gradient_pink)
    BadgeColor.Gradient.YELLOW -> painterResource(R.drawable.ds_gradient_yellow)
}

private fun Modifier.pickleRick(): Modifier = composed {
    val targetAngle = remember { Random.nextInt(65, 90).toFloat() }
    val context = LocalContext.current
    graphicsLayer { rotationZ = targetAngle }
        .clip(CircleShape)
        .clickable { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PICKLE_VIDEO))) }
}

private val PICKLE_LABEL = buildAnnotatedString {
    val eyesStyle = SpanStyle(fontWeight = FontWeight.Bold)
    withStyle(eyesStyle) { append(':') }
    append(" ) ｡ º  ｡ º")
}
private const val PICKLE_VIDEO = "https://youtu.be/_gRnvDRFYN4?t=32"
private const val PICKLE_PUNCHLINE = "I'm Pickle Rick baby! I am a pickle!"
