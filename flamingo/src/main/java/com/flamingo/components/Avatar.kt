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

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flamingo.Flamingo
import com.flamingo.R
import com.flamingo.alpha
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.theme.FlamingoIcon
import com.flamingo.uiTestingTag
import com.flamingo.utils.exhaustive

/**
 *
 * @param disabled indicates to the user that this component is not interactive. Clicks will be
 * ignored
 *
 * @param onClick if null, [Avatar] will not be clickable (no ripple)
 *
 * @param indicator if null, no [Indicator] is showed. If [Indicator] is sticking out of the
 * [Avatar], it will be drawn outside the [Avatar]'s box and, consequently, will not influence
 * centering behaviour, for example.
 *
 * [Indicator] has a [Indicator.trench] — a __transparent__ gap between [Avatar] and body of the
 * [Indicator].
 *
 * Using [AvatarIndicator.icon], it is possible to display an icon inside of the [Indicator]. Our
 * designers invented a __novel__ concept: only a __small__ subset of all [FlamingoIcon]s are
 * correctly displayed inside the [Indicator]. Edges of others are cut off because of the small
 * [Indicator] size. Beware of this behaviour and __always check__ how your particular icon is
 * displayed.
 *
 * @param contentDescription text used by accessibility services to describe what this [Avatar]
 * represents. This should always be provided unless this [Avatar] is used for decorative purposes,
 * and does not represent a meaningful action that a user can take. This text should be
 * localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.AvatarComposePreview",
    figma = "https://f.com/file/sPbkUbBGkp5Mstc0IQYubk/4.1.-UI-Android-kit?node-id=830%3A17",
    specification = "https://confluence.companyname.ru/x/MhEnKQE",
    viewImplementation = "com.flamingo.view.components.Avatar",
    demo = [],
    supportsWhiteMode = false,
)
@Composable
public fun Avatar(
    content: AvatarContent,
    onClick: (() -> Unit)? = null,
    size: AvatarSize = AvatarSize.SIZE_112,
    shape: AvatarShape = AvatarShape.CIRCLE,
    indicator: AvatarIndicator? = null,
    disabled: Boolean = false,
    contentDescription: String?,
): Unit = FlamingoComponentBase {
    Box(Modifier.alpha(disabled)) {
        val cutoutRadius = if (indicator != null) {
            size.indicator.size(iconPresence = indicator.icon != null) / 2
        } else {
            0.dp
        }
        if (indicator != null) AvatarIndicator(indicator, size, shape, cutoutRadius)
        Box(
            modifier = Modifier
                .requiredSize(size.avatar)
                .clip(RoundedRectWithCutoutShape(
                    cornerRadius = shape.cornerRadius,
                    cutoutRadius = cutoutRadius,
                    cutoutPlacement = CutoutPlacement.BottomEnd,
                ))
                .run {
                    if (onClick != null) clickable(onClick = onClick, enabled = !disabled) else this
                },
            contentAlignment = Alignment.Center
        ) {
            when (content) {
                is AvatarContent.Image -> Image(
                    modifier = Modifier
                        .size(size.avatar)
                        .uiTestingTag("Image"),
                    painter = content.image,
                    contentDescription = contentDescription
                )
                is AvatarContent.Icon -> {
                    Background(background = content.background, size = size)
                    Icon(
                        modifier = Modifier
                            .size(size.icon)
                            .uiTestingTag("Icon"),
                        icon = content.icon,
                        contentDescription = contentDescription,
                        tint = foregroundColor(content.background)
                    )
                }
                is AvatarContent.Letters -> {
                    Background(background = content.background, size = size)
                    Text(
                        modifier = Modifier.uiTestingTag("Letters"),
                        text = with(content) { "$first$second" },
                        fontSize = size.text,
                        color = foregroundColor(content.background)
                    )
                }
            }.exhaustive
        }
    }
}

@Composable
private fun AvatarIndicator(
    indicator: AvatarIndicator,
    size: AvatarSize,
    shape: AvatarShape,
    cutoutRadius: Dp,
) = Box(modifier = Modifier.circleOffset(
    rectSize = DpSize(size.avatar, size.avatar),
    cornerRadius = shape.cornerRadius,
    circleRadius = cutoutRadius,
    cutoutPlacement = CutoutPlacement.BottomEnd,
)) {
    Indicator(
        size = size.indicator,
        color = indicator.color,
        icon = indicator.icon,
        trench = true
    )
}

@Composable
private fun Background(background: AvatarBackground, size: AvatarSize) {
    Image(
        modifier = Modifier
            .size(size.avatar)
            .uiTestingTag("Background"),
        painter = background.toPainter(),
        contentDescription = null
    )
}

@Composable
private fun foregroundColor(background: AvatarBackground): Color {
    return when (background) {
        AvatarBackground.PRIMARY -> Flamingo.colors.primary
        AvatarBackground.GREY -> Flamingo.colors.textSecondary
        else -> Flamingo.palette.white
    }
}

public data class AvatarIndicator(
    val color: IndicatorColor,
    val icon: FlamingoIcon? = null,
)

public enum class AvatarSize(
    public val avatar: Dp,
    public val text: TextUnit,
    public val icon: Dp,
    public val indicator: IndicatorSize,
) {
    SIZE_24(avatar = 24.dp, text = 9.sp, icon = 16.dp, indicator = IndicatorSize.SMALL),
    SIZE_32(avatar = 32.dp, text = 12.sp, icon = 16.dp, indicator = IndicatorSize.SMALL),
    SIZE_40(avatar = 40.dp, text = 16.sp, icon = 16.dp, indicator = IndicatorSize.SMALL),
    SIZE_56(avatar = 56.dp, text = 20.sp, icon = 24.dp, indicator = IndicatorSize.SMALL),
    SIZE_72(avatar = 72.dp, text = 24.sp, icon = 24.dp, indicator = IndicatorSize.BIG),
    SIZE_88(avatar = 88.dp, text = 28.sp, icon = 40.dp, indicator = IndicatorSize.BIG),
    SIZE_112(avatar = 112.dp, text = 40.sp, icon = 40.dp, indicator = IndicatorSize.BIG),
}

public enum class AvatarShape(internal val cornerRadius: Dp) {
    CIRCLE(Dp.Infinity),
    ROUNDED_CORNERS_SMALL(8.dp),
    ROUNDED_CORNERS_MEDIUM(12.dp),
    ROUNDED_CORNERS_BIG(20.dp);

    internal fun shape(): Shape {
        return if (cornerRadius == Dp.Infinity) CircleShape else RoundedCornerShape(cornerRadius)
    }
}

public enum class AvatarBackground(@DrawableRes private val drawableRes: Int) {
    PRIMARY(0),
    GREY(0),
    BLUE(R.drawable.ds_gradient_blue),
    GREEN(R.drawable.ds_gradient_green),
    ORANGE(R.drawable.ds_gradient_orange),
    RED(R.drawable.ds_gradient_red),
    PURPLE(R.drawable.ds_gradient_purple),
    PINK(R.drawable.ds_gradient_pink),
    YELLOW(R.drawable.ds_gradient_yellow),
    ;

    @Composable
    internal fun toPainter(): Painter = when (this) {
        PRIMARY -> ColorPainter(Flamingo.colors.primary.copy(alpha = 0.12f))
        GREY -> ColorPainter(Flamingo.colors.backgroundQuaternary)
        else -> painterResource(id = drawableRes)
    }
}

public sealed class AvatarContent {
    public data class Image(
        public val image: Painter,
        public val contentScale: ContentScale = ContentScale.Fit,
    ) : AvatarContent()

    public data class Icon(
        public val icon: FlamingoIcon,
        public val background: AvatarBackground,
    ) : AvatarContent()

    /**
     * Avatar supports only 2 letters. You MUST explicitly handle cases when your [String] has fewer
     * than 2 characters in it.
     */
    public data class Letters(
        public val first: Char,
        public val second: Char,
        public val background: AvatarBackground,
    ) : AvatarContent()
}
