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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.button.ButtonSize
import com.flamingo.components.button.ButtonVariant
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.theme.FlamingoIcon

/**
 * @param actions support only [Action.widthPolicy] == [ButtonWidthPolicy.TRUNCATING].
 * Provided [Action.widthPolicy] will be IGNORED!
 */
@FlamingoComponent(
    displayName = "Alert Message",
    preview = "com.flamingo.playground.preview.AlertMessageComposePreview",
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx/Friend-UI-kit?node-id=8378%3A17126",
    specification = "https://confluence.companyname.ru/x/C4YjKQE",
    theaterPackage = "com.flamingo.playground.components.alertmessage.TheaterPkg",
    viewImplementation = "com.flamingo.view.components.AlertMessage",
    demo = ["com.flamingo.playground.components.AlertMessageComposeStatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun AlertMessage(
    text: String,
    variant: AlertMessageVariant,
    onClose: (() -> Unit)? = null,
    actions: ActionGroup? = null,
): Unit = AlertMessage(
    text = { AnnotatedString(text) },
    variant = variant,
    onClose = onClose,
    actions = actions,
)

/**
 * Used to display rich text, primarily links with accent color. [Color] parameter is provided in
 * [text] to let you know, what accent color should be used to color the links.
 *
 * See [this](https://developer.android.com/jetpack/compose/text#click-with-annotation)
 *
 * @param onTextClick is called when user clicked somewhere on the text. [Int] parameter is a
 * character offset from the beginning of the [text]
 */
@Composable
public fun AlertMessage(
    text: (Color) -> AnnotatedString,
    variant: AlertMessageVariant,
    onTextClick: ((Int) -> Unit)? = null,
    onClose: (() -> Unit)? = null,
    actions: ActionGroup? = null,
): Unit = FlamingoComponentBase {
    val borderShape = RoundedCornerShape(20.dp)
    Row(
        modifier = Modifier
            .clip(borderShape)
            .animateContentSize()
            .background(animateColorAsState(variant.backgroundColor).value)
            .border(1.dp, variant.borderColor, borderShape)
            .padding(16.dp)
    ) {
        val iconRotation by animateFloatAsState(variant.iconRotation)
        Icon(
            modifier = Modifier
                .padding(end = 12.dp)
                .graphicsLayer { rotationZ = iconRotation },
            icon = variant.icon,
            tint = variant.iconColor,
            contentDescription = variant.contentDescription
        )
        Column(
            modifier = Modifier.weight(1f),
        ) {
            ClickableText(
                style = Flamingo.typography.body1,
                text = text.invoke(variant.textColor),
                onClick = { onTextClick?.invoke(it) }
            )
            if (actions != null) Actions(
                modifier = Modifier.padding(top = 8.dp),
                actions = actions,
                variant = variant
            )
        }
        if (onClose != null) Box(modifier = Modifier.offset(8.dp, -8.dp)) {
            IconButton(
                onClick = { onClose.invoke() },
                icon = Flamingo.icons.X,
                variant = IconButtonVariant.TEXT,
                contentDescription = "close"
            )
        }
    }
}

@Composable
private fun Actions(
    modifier: Modifier = Modifier,
    actions: ActionGroup,
    variant: AlertMessageVariant,
) = with(actions) {
    Row(modifier = modifier) {
        if (secondAction == null) {
            Box(Modifier.offset(x = -8.dp)) {
                with(firstAction) {
                    Button(
                        size = ButtonSize.SMALL,
                        variant = ButtonVariant.TEXT,
                        color = variant.buttonColor,
                        label = label,
                        onClick = onClick,
                        loading = loading,
                        disabled = disabled,
                        widthPolicy = ButtonWidthPolicy.TRUNCATING,
                    )
                }
            }
        } else {
            Box(modifier = Modifier.weight(1f, fill = false)) {
                Button(
                    size = ButtonSize.SMALL,
                    variant = ButtonVariant.CONTAINED,
                    color = variant.buttonColor,
                    label = firstAction.label,
                    onClick = firstAction.onClick,
                    loading = firstAction.loading,
                    disabled = firstAction.disabled,
                    widthPolicy = ButtonWidthPolicy.TRUNCATING,
                )
            }
            Spacer(modifier = Modifier.requiredWidth(8.dp))
            Box(modifier = Modifier.weight(1f, fill = false)) {
                Button(
                    size = ButtonSize.SMALL,
                    variant = ButtonVariant.TEXT,
                    color = ButtonColor.Default,
                    label = secondAction.label,
                    onClick = secondAction.onClick,
                    loading = secondAction.loading,
                    disabled = secondAction.disabled,
                    widthPolicy = ButtonWidthPolicy.TRUNCATING,
                )
            }
        }
    }
}

public enum class AlertMessageVariant(
    internal val icon: FlamingoIcon,
    internal val iconRotation: Float,
    internal val contentDescription: String,
) {
    INFO(Flamingo.icons.Info, 0f, "info"),
    WARNING(Flamingo.icons.Info, 180f, "warning"),
    SUCCESS(Flamingo.icons.Info, 0f, "success"),
    ERROR(Flamingo.icons.Info, 180f, "error"),
    ;

    internal val backgroundColor: Color
        @Composable get() = with(Flamingo.colors.extensions.background) {
            when (this@AlertMessageVariant) {
                INFO -> info
                WARNING -> warning
                SUCCESS -> success
                ERROR -> error
            }
        }

    internal val iconColor: Color
        @Composable get() = with(Flamingo.colors) {
            when (this@AlertMessageVariant) {
                INFO -> info
                WARNING -> warning
                SUCCESS -> success
                ERROR -> error
            }
        }

    internal val textColor: Color @Composable get() = iconColor

    internal val borderColor: Color
        @Composable get() = with(Flamingo.colors.extensions.outline) {
            when (this@AlertMessageVariant) {
                INFO -> info
                WARNING -> warning
                SUCCESS -> success
                ERROR -> error
            }
        }

    internal val buttonColor: ButtonColor
        @Composable get() = when (this) {
            INFO -> ButtonColor.Default
            WARNING -> ButtonColor.Warning
            SUCCESS -> ButtonColor.Success
            ERROR -> ButtonColor.Error
        }
}
