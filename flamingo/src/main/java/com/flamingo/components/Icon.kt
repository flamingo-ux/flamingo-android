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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toolingGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.theme.FlamingoIcon
import com.flamingo.theme.colors.FlamingoColors
import com.flamingo.uiTestingTag

/**
 * Icon component that draws [FlamingoIcon] using [tint], defaulting to
 * [FlamingoColors.textSecondary]. Does not support [clickable] modifier. For a clickable icon, see
 * [IconButton].
 *
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents. This should always be provided unless this icon is used for decorative purposes,
 * and does not represent a meaningful action that a user can take. This text should be
 * localized, such as by using [androidx.compose.ui.res.stringResource] or similar
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.IconPreview",
    figma = "https://todo.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=381%3A2116",
    specification = "https://todo.com/x/LBhzNgE",
    demo = ["com.flamingo.playground.IconsDemoFragment"],
    supportsWhiteMode = true,
)
@UsedInsteadOf("androidx.compose.material.Icon")
@Composable
public fun Icon(
    icon: FlamingoIcon,
    contentDescription: String? = icon.getName(LocalContext.current).replace('_', ' '),
    modifier: Modifier = Modifier,
    tint: Color =
        if (Flamingo.isWhiteMode) Flamingo.palette.white else Flamingo.colors.textSecondary,
) {
    val colorFilter = if (tint == Color.Unspecified) null else ColorFilter.tint(tint)
    val semantics = if (contentDescription != null && !Flamingo.uiTestingTagsEnabled) {
        Modifier.semantics {
            this.contentDescription = contentDescription
            this.role = Role.Image
        }
    } else {
        Modifier
    }
    val painter = icon.toPainter()
    val context = LocalContext.current
    Box(
        Modifier
            .defaultSizeFor(painter)
            .then(modifier)
            .toolingGraphicsLayer()
            .paint(
                painter = painter,
                colorFilter = colorFilter,
                contentScale = ContentScale.Fit
            )
            .then(semantics)
            .uiTestingTag {
                icon
                    .getName(context)
                    .replace('_', ' ')
            }
    )
}

private fun Modifier.defaultSizeFor(painter: Painter) =
    this.then(
        if (painter.intrinsicSize == Size.Unspecified || painter.intrinsicSize.isInfinite()) {
            DefaultIconSizeModifier
        } else {
            Modifier
        }
    )

private fun Size.isInfinite() = width.isInfinite() && height.isInfinite()

// Default icon size, for icons with no intrinsic size information
private val DefaultIconSizeModifier = Modifier.size(24.dp)

