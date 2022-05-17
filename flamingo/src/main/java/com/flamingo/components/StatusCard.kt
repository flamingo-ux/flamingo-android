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
    "SpacingAroundParens"
)

package com.flamingo.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Constraints
import com.flamingo.R
import com.flamingo.annotations.FlamingoComponent

/**
 * Has width of [MAX_WIDTH]. If [StatusCard] doesn't fit on the screen, width will shrink (up to 0)
 * to respect incoming constraints.
 *
 * @param verticalScroll if true, and vertical [Constraints] are too small for the content of
 * [StatusCard] to fit in them, vertical scroll is used to give the user an ability to see all of
 * the content.
 * Is `false` by default because of needed caution when placing [StatusCard] in another
 * [verticalScroll] container: this placement is not supported by Compose
 *
 * @param contentDescription text used by accessibility services to describe what this [StatusCard]
 * represents. This should always be provided. This text should be localized, such as by using
 * [androidx.compose.ui.res.stringResource] or similar
 */
@FlamingoComponent(
    displayName = "Status Card",
    preview = "com.flamingo.playground.preview.StatusCardPreview",
    figma = "https://todo.com/file/qVO8jDuABDK9vsuLqRXeMx?node-id=1302%3A4417",
    specification = "https://todo.com/x/gALDOAE",
    demo = ["com.flamingo.playground.components.listitem.ListLoadingTypicalUsage"],
    supportsWhiteMode = true,
)
@Composable
public fun StatusCard(
    image: StatusCardImage,
    title: String? = null,
    description: String? = null,
    actions: ActionGroup? = null,
    verticalScroll: Boolean = false,
    contentDescription: String?,
): Unit = FlamingoComponentBase {
    EmptyStateBase(
        image = image.toPainter(),
        icon = null,
        title = title,
        description = description,
        actions = actions,
        verticalScroll = verticalScroll,
        contentDescription = contentDescription,
    )
}

public enum class StatusCardImage(@DrawableRes private val drawableRes: Int) {
    Success(R.drawable.success),
    Fail(R.drawable.fail),
    ;

    @Composable
    internal fun toPainter(): Painter = painterResource(id = drawableRes)
}
