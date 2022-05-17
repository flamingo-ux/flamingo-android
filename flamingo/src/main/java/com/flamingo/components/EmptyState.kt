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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.R
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.contentDescriptionSemantics
import com.flamingo.theme.FlamingoIcon
import com.flamingo.theme.FlamingoTheme

/**
 * Has width of [MAX_WIDTH]. If [EmptyState] doesn't fit on the screen, width will shrink (up to 0)
 * to respect incoming constraints.
 *
 * @param verticalScroll if true, and vertical [Constraints] are too small for the content of
 * [EmptyState] to fit in them, vertical scroll is used to give the user an ability to see all of
 * the content.
 * Is `false` by default because of needed caution when placing [EmptyState] in another
 * [verticalScroll] container: this placement is not supported by Compose
 *
 * @param actions doesn't support [Action.widthPolicy] == [ButtonWidthPolicy.STRICT]
 *
 * @param contentDescription text used by accessibility services to describe what this [EmptyState]
 * represents. This should always be provided. This text should be localized, such as by using
 * [androidx.compose.ui.res.stringResource] or similar
 */
@FlamingoComponent(
    displayName = "Empty State",
    preview = "com.flamingo.playground.preview.EmptyStateComposePreview",
    figma = "https://todo.com/file/qVO8jDuABDK9vsuLqRXeMx/Friend-UI-kit?node-id=830%3A621",
    specification = "https://todo.com/x/M4YjKQE",
    viewImplementation = "com.flamingo.view.components.EmptyState",
    demo = ["com.flamingo.playground.components.listitem.ListLoadingTypicalUsage"],
    supportsWhiteMode = true,
)
@Composable
public fun EmptyState(
    image: EmptyStateImage? = null,
    icon: FlamingoIcon? = null,
    title: String? = null,
    description: String? = null,
    actions: ActionGroup? = null,
    verticalScroll: Boolean = false,
    contentDescription: String?,
): Unit = FlamingoComponentBase {
    EmptyStateBase(
        image = image?.toPainter(),
        icon = icon,
        title = title,
        description = description,
        actions = actions,
        verticalScroll = verticalScroll,
        contentDescription = contentDescription,
    )
}

@Composable
internal fun EmptyStateBase(
    image: Painter? = null,
    icon: FlamingoIcon? = null,
    title: String? = null,
    description: String? = null,
    actions: ActionGroup? = null,
    verticalScroll: Boolean = false,
    contentDescription: String?,
) {
    Column(
        modifier = Modifier
            .contentDescriptionSemantics(contentDescription)
            .widthIn(min = 0.dp, max = MAX_WIDTH.dp)
            .run { if (verticalScroll) verticalScroll(rememberScrollState()) else this }
            .padding(vertical = 40.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (icon != null) {
            Avatar(
                content = AvatarContent.Icon(icon, AvatarBackground.GREEN),
                size = AvatarSize.SIZE_88,
                shape = AvatarShape.CIRCLE,
                contentDescription = contentDescription
            )
        } else if (image != null) {
            Image(painter = image, contentDescription = null)
        }

        if (Flamingo.isWhiteMode) FlamingoTheme(darkTheme = true) {
            TextBlocks(title, description)
        } else {
            TextBlocks(title, description)
        }
        if (actions != null) Actions(actions)
    }
}

@Composable
private fun TextBlocks(title: String?, description: String?) {
    if (title != null) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = Flamingo.typography.h6
        )
    }
    if (description != null) {
        Text(
            text = description,
            textAlign = TextAlign.Center,
            style = Flamingo.typography.body2
        )
    }
}

@Composable
private fun Actions(actions: ActionGroup) = with(actions) {
    require(actions.firstAction.widthPolicy != ButtonWidthPolicy.STRICT) {
        "${ButtonWidthPolicy.STRICT} is not supported"
    }
    require(actions.secondAction?.widthPolicy != ButtonWidthPolicy.STRICT) {
        "${ButtonWidthPolicy.STRICT} is not supported"
    }
    Column {
        if (secondAction == null) {
            with(firstAction) {
                Button(
                    fillMaxWidth = true,
                    label = label,
                    onClick = onClick,
                    loading = loading,
                    disabled = disabled,
                    widthPolicy = widthPolicy,
                )
            }
        } else {
            Button(
                fillMaxWidth = true,
                color = ButtonColor.Primary,
                label = firstAction.label,
                onClick = firstAction.onClick,
                loading = firstAction.loading,
                disabled = firstAction.disabled,
                widthPolicy = firstAction.widthPolicy,
            )
            Spacer(modifier = Modifier.requiredHeight(8.dp))
            Button(
                fillMaxWidth = true,
                label = secondAction.label,
                onClick = secondAction.onClick,
                loading = secondAction.loading,
                disabled = secondAction.disabled,
                widthPolicy = secondAction.widthPolicy,
            )
        }
    }
}

public enum class EmptyStateImage(@DrawableRes private val drawableRes: Int) {
    UPDATES(R.drawable.ds_image_updates),
    NOT_INSTALLED(R.drawable.ds_image_not_installed),
    NO_NETWORK(R.drawable.ds_image_no_network),
    NOTIFICATION(R.drawable.ds_image_notification),
    FEED(R.drawable.ds_image_feed),
    EMPTY(R.drawable.ds_image_empty),
    CALENDAR(R.drawable.ds_image_calendar),
    COMMUNITY(R.drawable.ds_image_community),
    WEB(R.drawable.ds_image_web),
    CRASH(R.drawable.ds_image_crash),
    PEOPLE(R.drawable.ds_image_people),
    SHIELD(R.drawable.ds_image_shield),
    LOCK(R.drawable.ds_image_lock),
    SEARCH(R.drawable.ds_image_search),
    IMAGE_MAINTENANCE(R.drawable.ds_image_maintenance),
    ;

    @Composable
    internal fun toPainter(): Painter = painterResource(id = drawableRes)
}

private const val MAX_WIDTH = 336
