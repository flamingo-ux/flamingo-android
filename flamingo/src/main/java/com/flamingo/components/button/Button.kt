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

package com.flamingo.components.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.components.Badge
import com.flamingo.components.BadgeSize
import com.flamingo.components.CircularLoader
import com.flamingo.components.CircularLoaderSize
import com.flamingo.components.FlamingoComponentBase
import com.flamingo.components.Icon
import com.flamingo.components.Text
import com.flamingo.components.button.ButtonColor.Default
import com.flamingo.components.button.ButtonColor.White
import com.flamingo.components.button.ButtonSize.LARGE
import com.flamingo.components.button.ButtonSize.MEDIUM
import com.flamingo.components.button.ButtonSize.SMALL
import com.flamingo.components.button.ButtonVariant.CONTAINED
import com.flamingo.components.button.ButtonWidthPolicy.MULTILINE
import com.flamingo.theme.FlamingoIcon
import kotlinx.serialization.Serializable

/**
 * Height of the [Button] is not influenced by incoming constrains, meaning, that it will be the
 * same, no matter the height constrains (see [VerticallySqueezedMultilineButton] sample).
 *
 * @param label '\n' will be respected only in [ButtonWidthPolicy.MULTILINE]. Otherwise, they will
 * be replaced with spaces.
 *
 * @param fillMaxWidth if [fillMaxWidth] is true, and if [Button]'s width is smaller than the
 * incoming max width constrains, [Button]'s width will be equal to max width constrains ([Button]
 * will occupy all provided width)
 *
 * @param loading if true, [onClick] won't be called
 *
 * @param color default param value supports [Flamingo.LocalWhiteMode]. If you are setting your own
 * color, be sure to support [Flamingo.LocalWhiteMode], if you need it.
 *
 * @sample com.flamingo.playground.components.button.ButtonsInRow
 * @sample com.flamingo.playground.components.button.Squeezed
 * @sample com.flamingo.playground.components.button.SqueezedAndClipped
 * @sample com.flamingo.playground.components.button.Split
 * @sample com.flamingo.playground.components.button.SplitWrapContent
 * @sample com.flamingo.playground.components.button.SplitBigText
 * @sample com.flamingo.playground.components.button.SplitAndSqueezedBigText
 * @sample com.flamingo.playground.components.button.SplitMultiline
 * @sample com.flamingo.playground.components.button.VerticallySqueezedMultilineButton
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.ButtonComposePreview",
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx/UI-kit?node-id=628%3A5",
    specification = "https://confluence.companyname.ru/x/j4cjKQE",
    viewImplementation = "com.flamingo.view.components.Button",
    theaterPackage = "com.flamingo.playground.components.button.TheaterPkg",
    demo = [
        "com.flamingo.playground.components.button.ButtonComposeStatesPlayroom",
        "com.flamingo.playground.components.button.ButtonInRowTypicalUsage",
    ],
    supportsWhiteMode = true,
)
@UsedInsteadOf("androidx.compose.material.Button")
@Composable
public fun Button(
    onClick: () -> Unit,
    label: String,
    loading: Boolean = false,
    size: ButtonSize = MEDIUM,
    color: ButtonColor = if (Flamingo.isWhiteMode) White else Default,
    variant: ButtonVariant = CONTAINED,
    startIcon: FlamingoIcon? = null,
    endItem: ButtonEndItem? = null,
    disabled: Boolean = false,
    fillMaxWidth: Boolean = false,
    widthPolicy: ButtonWidthPolicy = MULTILINE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
): Unit = FlamingoComponentBase {
    @Suppress("NAME_SHADOWING") val disabled = disabled || loading

    val onColor = ButtonColorCalculation.onColor(variant, color)
    val rippleColor = ButtonColorCalculation.rippleColor(variant, color)
    val backgroundColor = ButtonColorCalculation.backgroundColor(variant, color)

    val verticalPadding: Dp = size.verticalPadding

    val paddings = remember { ButtonPaddings() }
    paddings.calculatePaddings(
        hasStartItem = startIcon != null,
        hasEndItem = endItem != null,
        loading = loading,
        size = size
    )

    val showShadow = variant == CONTAINED && when (color) {
        Default, ButtonColor.TopAppBar, White -> false
        else -> true
    }

    ButtonLayout(
        modifier = Modifier.alpha(disabled, animate = true),
        verticalPadding = verticalPadding,
        fillMaxWidth = fillMaxWidth,
        widthPolicy = widthPolicy,
    ) {
        Box(
            modifier = Modifier
                .layoutId("background")
                .shadow(if (showShadow) 2.dp else 0.dp, buttonShape(size))
                .clip(buttonShape(size))
                .background(backgroundColor)
                .clickable(
                    onClick = onClick,
                    enabled = !disabled,
                    role = Role.Button,
                    interactionSource = interactionSource,
                    indication = rememberRipple(color = rippleColor)
                ),
        )
        if (loading) {
            Box(
                modifier = Modifier
                    .layoutId("startItem")
                    .padding(start = paddings.iconStart)
            ) {
                CircularLoader(size = CircularLoaderSize.SMALL, color = onColor)
            }
        } else if (startIcon != null) {
            Icon(
                modifier = Modifier
                    .layoutId("startItem")
                    .padding(start = paddings.iconStart)
                    .requiredSize(16.dp),
                tint = onColor,
                icon = startIcon,
                contentDescription = null
            )
        }
        Text(
            color = onColor,
            text = if (widthPolicy != MULTILINE) label.replace("\n", " ") else label,
            modifier = Modifier
                .layoutId("text")
                .padding(start = paddings.textStart, end = paddings.textEnd),
            textAlign = TextAlign.Start,
            maxLines = if (widthPolicy == MULTILINE) Int.MAX_VALUE else 1,
            overflow = TextOverflow.Ellipsis,
            style = Flamingo.typography.run { if (size == SMALL) body2 else body1 }
        )

        if (endItem != null) {
            when (endItem) {
                is ButtonEndItem.Icon -> {
                    Icon(
                        modifier = Modifier
                            .layoutId("endItem")
                            .padding(end = paddings.iconEnd)
                            .requiredSize(16.dp)
                            .rotate(animateFloatAsState(endItem.rotation).value),
                        tint = onColor,
                        icon = endItem.icon,
                        contentDescription = null
                    )
                }
                is ButtonEndItem.Badge -> {
                    val badgeColor = ButtonColorCalculation.badgeColor(variant, color)
                    Box(
                        Modifier
                            .layoutId("endItem")
                            .padding(end = paddings.iconEnd)
                    ) {
                        Badge(label = endItem.label, color = badgeColor, size = BadgeSize.SMALL)
                    }
                }
            }
        }
    }
}

@Composable
internal fun Color.animateButtonColor(
    animationSpec: SpringSpec<Color> = spring(stiffness = 700f),
): Color = animateColorAsState(this, animationSpec = animationSpec).value

public enum class ButtonSize(
    internal val height: Dp,
    internal val verticalPadding: Dp
) { SMALL(32.dp, 6.dp), MEDIUM(40.dp, 10.dp), LARGE(48.dp, 14.dp), }

public enum class ButtonIconPosition { START, END, }
public enum class ButtonVariant { TEXT, CONTAINED, }

@Immutable
@Serializable
public sealed class ButtonColor {
    public object Default : ButtonColor()
    public object Primary : ButtonColor()
    public object Warning : ButtonColor()
    public object Error : ButtonColor()
    internal object Info : ButtonColor()

    public object TopAppBar : ButtonColor()
    internal object Success : ButtonColor()
    public object White : ButtonColor()
}

private fun buttonShape(size: ButtonSize) = RoundedCornerShape(if (size == LARGE) 12.dp else 8.dp)

public sealed class ButtonEndItem {
    public data class Icon(val icon: FlamingoIcon, val rotation: Float = 0f) : ButtonEndItem()
    public data class Badge(val label: String) : ButtonEndItem()
}
