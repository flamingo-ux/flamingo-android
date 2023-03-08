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
    "ComplexMethod",
    "SpacingAroundParens"
)

package com.flamingo.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.theme.FlamingoIcon
import com.flamingo.theme.FlamingoRippleTheme
import kotlinx.coroutines.flow.collectLatest

/**
 * @param label Link to be displayed. '\n' will be respected only in [ButtonWidthPolicy.MULTILINE].
 * Otherwise, they will be replaced with spaces.
 *
 * @param onClick optional. Passes [label] as a parameter in lambda.
 * If null, [Link] will not be clickable
 *
 * @param size size of the Link, affects components' height
 *
 * @param color [Color] to apply to the text and icons.
 *
 * @param loading if true, [onClick] won't be called, and [CircularLoader] will be displayed
 *
 * @param startIcon optional. Any [FlamingoIcon] can be used to display before label
 *
 * @param endIcon optional. Any [FlamingoIcon] can be used to display after label
 */
@FlamingoComponent(
    displayName = "Link",
    preview = "com.flamingo.playground.preview.LinkPreview",
    figma = "https://f.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=27216%3A177530",
    specification = "https://confluence.companyname.ru/x/_CTwHAI",
    theaterPackage = "com.flamingo.playground.components.link.TheaterPkg",
    demo = ["com.flamingo.playground.components.link.LinkStatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun Link(
    label: String,
    onClick: (() -> Unit)?,
    size: LinkSize = LinkSize.NORMAL,
    color: Color = Flamingo.colors.primary,
    loading: Boolean = false,
    disabled: Boolean = false,
    startIcon: FlamingoIcon? = null,
    endIcon: FlamingoIcon? = null
): Unit = FlamingoComponentBase {
    val disabled = disabled || loading || onClick == null
    val textStartPadding = if (loading || startIcon != null) 8.dp else 0.dp
    val textEndPadding = if (endIcon != null) 8.dp else 0.dp
    val interactionSource = remember { MutableInteractionSource() }

    val newColor = remember { Animatable(color) }

    val hoverColor = if (color == Flamingo.colors.primary) Flamingo.colors.greenHover else
        FlamingoRippleTheme.defaultColor().copy(alpha = .12f).compositeOver(color)

    // text ripple effect
    LaunchedEffect(key1 = color) {
        // this is needed to update color when new color is passed in component
        newColor.animateTo(color, tween(0))
        interactionSource.interactions.collectLatest {
            if (it is PressInteraction.Press) {
                newColor.animateTo(hoverColor, animationSpec)
            } else {
                newColor.animateTo(hoverColor, animationSpec)
                newColor.animateTo(color, animationSpec)
            }
        }
    }

    Row(
        modifier = Modifier.padding(vertical = size.verticalPadding)
            .run {
                if (onClick != null) clickable(
                    onClick = onClick,
                    enabled = !disabled,
                    interactionSource = interactionSource,
                    indication = null,
                ) else this
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (loading) {
            CircularLoader(size = CircularLoaderSize.SMALL, color = newColor.value)
        } else if (startIcon != null) {
            Icon(
                modifier = Modifier
                    .requiredSize(16.dp),
                tint = newColor.value,
                icon = startIcon,
                contentDescription = null
            )
        }
        Text(
            color = newColor.value,
            text = label,
            modifier = Modifier
                .padding(start = textStartPadding, end = textEndPadding),
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = Flamingo.typography.run { if (size == LinkSize.SMALL) body2 else body1 }
        )

        if (endIcon != null) {
            Icon(
                modifier = Modifier
                    .requiredSize(16.dp),
                tint = newColor.value,
                icon = endIcon,
                contentDescription = null
            )
        }
    }
}

private val animationSpec = tween<Color>(100)

public enum class LinkSize(
    internal val verticalPadding: Dp
) { SMALL(6.dp), NORMAL(8.dp) }