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

package com.flamingo.components.listitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.alpha
import com.flamingo.annotations.DelicateFlamingoApi
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.components.Action
import com.flamingo.components.ActionGroup
import com.flamingo.components.Avatar
import com.flamingo.components.Checkbox
import com.flamingo.components.Divider
import com.flamingo.components.FlamingoComponentBase
import com.flamingo.components.Icon
import com.flamingo.components.IconButton
import com.flamingo.components.LocalFlamingoTextStyle
import com.flamingo.components.RadioButton
import com.flamingo.components.Switch
import com.flamingo.components.Text
import com.flamingo.components.UniversalText
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.button.ButtonColor.White
import com.flamingo.components.button.ButtonSize
import com.flamingo.components.button.ButtonVariant.CONTAINED
import com.flamingo.components.button.ButtonVariant.TEXT
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.overlay.LocalDebugOverlayImpl
import com.flamingo.theme.FlamingoIcon
import com.flamingo.theme.FlamingoTheme
import com.flamingo.theme.colors.FlamingoColorPalette
import com.flamingo.theme.colors.FlamingoColors
import com.flamingo.withStyle

/**
 * If constraints on the @[Composable] slots, described below, are violated, __🚨NO GUARANTIES🚨__
 * are provided in terms of defined behaviour, backwards compatibility and overall correctness of
 * the [ListItem].
 *
 * One of [title], [subtitle], [description] MUST BE provided. All of them are multiline. If
 * single line behaviour is needed, use [AdvancedListItem].
 *
 * @param start if null, slot will be hidden. Only __one__ of those components is allowed in this
 * slot:
 * - [Avatar]
 * - [IconButton]
 * - [Switch]
 * - [RadioButton]
 * - [Checkbox]
 * - [Icon] with [requiredSize] == 24[dp] and [FlamingoColors.textSecondary] tint
 * ([FlamingoColorPalette.white] in white mode). These params MUST BE set explicitly - you MUST NOT
 * rely on the default behaviour of the [Icon].
 *
 * @param end if null, slot will be hidden. Only __one__ of those components is allowed in the slot:
 * - [IconButton]
 * - [Switch]
 * - [RadioButton]
 * - [Checkbox]
 * - [Icon] with [requiredSize] == 24[dp] and [FlamingoColors.textSecondary] tint
 * ([FlamingoColorPalette.white] in white mode). These params MUST BE set explicitly - you MUST NOT
 * rely on the default behaviour of the [Icon].
 * - [Button], but [ButtonSize] MUST BE [ButtonSize.MEDIUM]
 *
 * @param title optional title text
 *
 * @param subtitle optional subtitle text, displayed after [title] (if [invertTitleAndSubtitle] is
 * false and [title] is non-null)
 *
 * @param description optional subtitle text, displayed after [title] and [subtitle]
 *
 * @param date optional text, displayed next to the [title] (when [invertTitleAndSubtitle] is false)
 * or [subtitle] (when [invertTitleAndSubtitle] is true). Single line only. If [date] is too long,
 * entirely displaces [title] (or [subtitle]) from the screen.
 *
 * @param isEndSlotClickableWhenDisabled if true, and [disabled] is true, contents of the [end] slot
 * will remain clickable and alpha will not be applied to it
 *
 * @param titleMaxLines can only be one of [1, 2, [Int.MAX_VALUE]]. If [title] is big enough to not
 * fit in [titleMaxLines] lines, [title] will be truncated (with [TextOverflow.Ellipsis]) to show
 * only [titleMaxLines] lines.
 *
 * @param actions optional [Button]s at the bottom of the texts section ([title], [subtitle],
 * [description]). If [ActionGroup.secondAction] is null, [Button] will be [TEXT]. If not, first
 * [Button] will be [CONTAINED], second - [TEXT]. If [Flamingo.LocalWhiteMode] is true, [TEXT]
 * variants will be converted to [CONTAINED] with color [White].
 * [Button]s support only [Action.widthPolicy] == [ButtonWidthPolicy.TRUNCATING]. Provided
 * [Action.widthPolicy] will be IGNORED!
 *
 * @param onClick if non-null, whole [ListItem] becomes clickable, except for the clickable
 * components in the slots and [actions]. If you want to switch, for example, [Switch], in one of
 * the slots, just remove onClick from [Switch] and create a [MutableState] to select a [Switch]
 * from [ListItem]'s [onClick].
 *
 * @param showDivider if true, shows [Divider] under the text sections and [end]
 *
 * @param invertTitleAndSubtitle if true, vertical order of [title] and [subtitle] will be switched
 *
 * @param disabled if true, [ListItem] will not be clickable and will appear in the disabled ui
 * state. All touch-sensitive (clickable) content inside slots will be non-interactive. You MUST NOT
 * disable them manually when setting this parameter to true, or else alpha will be applied twice.
 */
@FlamingoComponent(
    displayName = "List Item",
    preview = "com.flamingo.playground.preview.ListItemPreview",
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx/Friend-UI-kit?node-id=628%3A7",
    specification = "https://confluence.companyname.ru/x/jhEnKQE",
    demo = [
        "com.flamingo.playground.components.listitem.ListItemStatesPlayroom",
        "com.flamingo.playground.components.listitem.ListLoadingTypicalUsage",
        "com.flamingo.playground.components.listitem.AnnotatedTypicalUsage",
    ],
    supportsWhiteMode = false,
)
// strange bug, return type is required by detekt (NoUnitReturn rule) and explicit api mode
@Suppress("RedundantUnitReturnType")
@UsedInsteadOf("androidx.compose.material.ListItem")
@Composable
public fun ListItem(
    start: (@Composable () -> Unit)? = null,
    title: String? = null,
    subtitle: String? = null,
    description: String? = null,
    date: String? = null,
    end: (@Composable () -> Unit)? = null,
    isEndSlotClickableWhenDisabled: Boolean = false,
    titleMaxLines: Int = Int.MAX_VALUE,
    sideSlotsAlignment: SideSlotsAlignment = SideSlotsAlignment.CENTER,
    actions: ActionGroup? = null,
    onClick: (() -> Unit)? = null,
    showDivider: Boolean = true,
    invertTitleAndSubtitle: Boolean = false,
    disabled: Boolean = false,
): @Suppress("NoUnitReturn") Unit = FlamingoComponentBase {
    when (titleMaxLines) {
        1, 2, Int.MAX_VALUE -> Unit
        else -> throw IllegalArgumentException(
            "titleMaxLines can only be equal to 1, 2 or Int.MAX_VALUE"
        )
    }
    AdvancedListItem(
        start = start,
        title = title?.let {
            { Text(text = it, maxLines = titleMaxLines, overflow = TextOverflow.Ellipsis) }
        },
        subtitle = subtitle?.let { { Text(text = it) } },
        description = description?.let { { Text(text = it) } },
        date = date,
        end = end,
        isEndSlotClickableWhenDisabled = isEndSlotClickableWhenDisabled,
        sideSlotsAlignment = sideSlotsAlignment,
        actions = actions,
        onClick = onClick,
        showDivider = showDivider,
        invertTitleAndSubtitle = invertTitleAndSubtitle,
        disabled = disabled,
    )
}

/**
 * Differs from the [ListItem] only in the type of [title], [subtitle], [description] parameters.
 *
 * __🚨WARNING🚨__: be extremely careful when specifying other parameters in [AnnotatedString]. Text
 * coloring, font size changes, links and other customizations should be discussed in details with
 * design system team and __MULTIPLE__ designers first.
 *
 * To apply typography style from [Flamingo.typography], use special [withStyle] function.
 *
 * Rest of the docs can be found in [ListItem].
 */
// strange bug, return type is required by detekt (NoUnitReturn rule) and explicit api mode
@Suppress("RedundantUnitReturnType")
@Composable
public fun ListItem(
    start: (@Composable () -> Unit)? = null,
    title: AnnotatedString? = null,
    subtitle: AnnotatedString? = null,
    description: AnnotatedString? = null,
    date: AnnotatedString? = null,
    end: (@Composable () -> Unit)? = null,
    isEndSlotClickableWhenDisabled: Boolean = false,
    titleMaxLines: Int = Int.MAX_VALUE,
    sideSlotsAlignment: SideSlotsAlignment = SideSlotsAlignment.CENTER,
    actions: ActionGroup? = null,
    onClick: (() -> Unit)? = null,
    showDivider: Boolean = true,
    invertTitleAndSubtitle: Boolean = false,
    disabled: Boolean = false,
): @Suppress("NoUnitReturn") Unit = FlamingoComponentBase {
    when (titleMaxLines) {
        1, 2, Int.MAX_VALUE -> Unit
        else -> throw IllegalArgumentException(
            "titleMaxLines can only be equal to 1, 2 or Int.MAX_VALUE"
        )
    }
    AdvancedListItem(
        start = start,
        title = title?.let {
            { Text(text = it, maxLines = titleMaxLines, overflow = TextOverflow.Ellipsis) }
        },
        subtitle = subtitle?.let { { Text(text = it) } },
        description = description?.let { { Text(text = it) } },
        date = date,
        end = end,
        isEndSlotClickableWhenDisabled = isEndSlotClickableWhenDisabled,
        sideSlotsAlignment = sideSlotsAlignment,
        actions = actions,
        onClick = onClick,
        showDivider = showDivider,
        invertTitleAndSubtitle = invertTitleAndSubtitle,
        disabled = disabled,
    )
}

/**
 * Differs from the [ListItem] only in the type of [title], [subtitle], [description] parameters.
 *
 * [TextWrapper] enables to add [FlamingoIcon] to the start or the end of the text.
 * Also [PaddingValues] can be added to separate texts
 *
 * __🚨WARNING🚨__: these customizations can greatly change the appearance of the component, be
 * careful
 *
 * Rest of the docs can be found in [ListItem].
 */
@Suppress("RedundantUnitReturnType")
@Composable
public fun ListItem(
    start: (@Composable () -> Unit)? = null,
    title: TextWrapper? = null,
    subtitle: TextWrapper? = null,
    description: TextWrapper? = null,
    date: AnnotatedString? = null,
    end: (@Composable () -> Unit)? = null,
    isEndSlotClickableWhenDisabled: Boolean = false,
    titleMaxLines: Int = Int.MAX_VALUE,
    sideSlotsAlignment: SideSlotsAlignment = SideSlotsAlignment.CENTER,
    actions: ActionGroup? = null,
    onClick: (() -> Unit)? = null,
    showDivider: Boolean = true,
    invertTitleAndSubtitle: Boolean = false,
    disabled: Boolean = false,
): @Suppress("NoUnitReturn") Unit = FlamingoComponentBase {
    when (titleMaxLines) {
        1, 2, Int.MAX_VALUE -> Unit
        else -> throw IllegalArgumentException(
            "titleMaxLines can only be equal to 1, 2 or Int.MAX_VALUE"
        )
    }
    val titleColor = Flamingo.colors.textPrimary
    val descriptionColor = Flamingo.colors.textSecondary
    val subtitleColor = Flamingo.typography.body2.color
    AdvancedListItem(
        start = start,
        title = title?.let {
            val text: @Composable RowScope.() -> Unit = {
                Text(
                    text = it.text,
                    maxLines = titleMaxLines,
                    overflow = TextOverflow.Ellipsis,
                    //modifier = Modifier.weight(1f)
                )
            }
            return@let {
                TextByTextWrapper(text, it, titleColor)
            }
        },
        subtitle = subtitle?.let {
            val text: @Composable RowScope.() -> Unit = {
                Text(
                    text = it.text,
                    //modifier = Modifier.weight(1f)
                )
            }
            return@let {
                TextByTextWrapper(text, it, subtitleColor)
            }
        },
        description = description?.let {
            val text: @Composable RowScope.() -> Unit = {
                Text(
                    text = it.text,
                    //modifier = Modifier.weight(1f)
                )
            }
            return@let {
                TextByTextWrapper(text, it, descriptionColor)
            }
        },
        date = date,
        end = end,
        isEndSlotClickableWhenDisabled = isEndSlotClickableWhenDisabled,
        sideSlotsAlignment = sideSlotsAlignment,
        actions = actions,
        onClick = onClick,
        showDivider = showDivider,
        invertTitleAndSubtitle = invertTitleAndSubtitle,
        disabled = disabled,
    )
}

@Composable
private fun TextByTextWrapper(
    text: @Composable RowScope.() -> Unit,
    textWrapper: TextWrapper,
    defaultIconTint: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(textWrapper.padding)
    ) {
        if (textWrapper.icon == null) {
            text()
        } else {
            if (textWrapper.iconPosition == TextWrapper.TextIconPosition.START) {
                Icon(
                    icon = textWrapper.icon,
                    tint = textWrapper.iconTint ?: defaultIconTint,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(16.dp)
                )
                text()
            } else {
                text()
                Icon(
                    icon = textWrapper.icon,
                    tint = textWrapper.iconTint ?: defaultIconTint,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(16.dp)
                )
            }
        }
    }
}

/**
 * If constraints on the @[Composable] slots, described in [ListItem]'s docs, are violated,
 * __🚨NO GUARANTIES🚨__ are provided in terms of defined behaviour, backwards compatibility and
 * overall correctness of the [AdvancedListItem].
 *
 * Differs from the [ListItem] in the type of [title], [subtitle], [description] parameters.
 * When [Text] is called in those [Composable]s, correct [TextStyle]s will be used (because of
 * [LocalFlamingoTextStyle]). As an example, single line behaviour can be achieved.
 *
 * Calling other @[Composable] functions inside these lambdas is __🚨PROHIBITED🚨__ without a strong
 * consensus among many designers. Without it, only [Text] fun can be called there.
 *
 * @param date must be either [String] or [AnnotatedString]
 *
 * __🚨WARNING🚨__: be extremely careful when specifying other parameters in [Text]. Text coloring,
 * font size changes, links and other customizations should be discussed in details with design
 * system team and __MULTIPLE__ designers first.
 *
 * Rest of the docs can be found in [ListItem].
 *
 * @sample com.flamingo.playground.preview.AdvancedListItemPreview
 */
@DelicateFlamingoApi
@Composable
public fun AdvancedListItem(
    start: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
    date: CharSequence? = null,
    end: (@Composable () -> Unit)? = null,
    isEndSlotClickableWhenDisabled: Boolean = false,
    sideSlotsAlignment: SideSlotsAlignment = SideSlotsAlignment.CENTER,
    actions: ActionGroup? = null,
    onClick: (() -> Unit)? = null,
    showDivider: Boolean = true,
    invertTitleAndSubtitle: Boolean = false,
    disabled: Boolean = false,
): Unit = with(ListItemScope) {
    val dateInTitle =
        date != null && title != null && (subtitle == null || !invertTitleAndSubtitle)
    val dateInSubtitle =
        date != null && subtitle != null && (title == null || invertTitleAndSubtitle)

    val title: (@Composable () -> Unit)? = if (dateInTitle) {
        { HeaderWithDate(date = date!!, header = title!!) }
    } else title

    val subtitle: (@Composable () -> Unit)? = if (dateInSubtitle) {
        { HeaderWithDate(date = date!!, header = subtitle!!) }
    } else subtitle

    ListItemLayout(
        modifier = Modifier
            .fillMaxWidth()
            .run { onClick?.let { clickable(enabled = !disabled, onClick = onClick) } ?: this }
            .padding(start = hPadding, end = hPadding, top = vPadding),
        sideSlotsAlignment = sideSlotsAlignment
    ) {
        if (start != null) StartSlot(Modifier.layoutId("start"), disabled, start)

        CenterSlot(
            Modifier.layoutId("center"),
            title, subtitle, description, actions, invertTitleAndSubtitle, disabled
        )

        if (end != null) EndSlot(
            Modifier.layoutId("end"), end, disabled, isEndSlotClickableWhenDisabled
        )

        CompositionLocalProvider(LocalDebugOverlayImpl provides null) {
            if (showDivider) Divider(
                Modifier
                    .layoutId("divider")
                    .alpha(disabled)
            )
        }
    }
}

@Composable
private fun HeaderWithDate(date: CharSequence, header: @Composable () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.weight(1f)) { header() }
        UniversalText(
            modifier = Modifier.padding(end = 8.dp),
            text = date,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = Flamingo.typography.caption2
        )
    }
}

public enum class SideSlotsAlignment {
    TOP, CENTER, BOTTOM;

    @Composable
    internal fun toComposeAlignment(): Alignment.Vertical = when (this) {
        TOP -> Alignment.Top
        CENTER -> Alignment.CenterVertically
        BOTTOM -> Alignment.Bottom
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ListItemScope.StartSlot(
    modifier: Modifier = Modifier,
    disabled: Boolean,
    start: (@Composable () -> Unit),
) = Box(
    modifier = modifier
        .padding(end = 12.dp)
        .alpha(disabled)
        .pointerInteropFilter { disabled },
    contentAlignment = Alignment.Center
) { start() }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ListItemScope.CenterSlot(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)?,
    subtitle: (@Composable () -> Unit)?,
    description: (@Composable () -> Unit)?,
    actions: ActionGroup?,
    invertTitleAndSubtitle: Boolean,
    disabled: Boolean,
) {
    require(title != null || subtitle != null || description != null) {
        "At least one of these params should be non-null: title, subtitle, description"
    }
    Column(
        modifier = modifier
            .alpha(disabled)
            .pointerInteropFilter { disabled },
        verticalArrangement = Arrangement.Center
    ) {
        if (Flamingo.isWhiteMode) FlamingoTheme(darkTheme = true) {
            TextBlock(invertTitleAndSubtitle, subtitle, title, description)
        } else TextBlock(invertTitleAndSubtitle, subtitle, title, description)

        actions?.let { Actions(modifier = Modifier.padding(top = 8.dp), actions) }
    }
}

@Composable
private fun TextBlock(
    invertTitleAndSubtitle: Boolean,
    subtitle: @Composable (() -> Unit)?,
    title: @Composable (() -> Unit)?,
    description: @Composable (() -> Unit)?,
) = with(Flamingo.typography) {
    val titleStyle = body1.copy(color = Flamingo.colors.textPrimary)
    val descriptionStyle = caption2.copy(color = Flamingo.colors.textSecondary)

    if (invertTitleAndSubtitle && subtitle != null) ProvideTextStyle(body2) { subtitle() }
    if (title != null) ProvideTextStyle(titleStyle) { title() }
    if (!invertTitleAndSubtitle && subtitle != null) ProvideTextStyle(body2) { subtitle() }
    if (description != null) ProvideTextStyle(descriptionStyle) { description() }
}

@Composable
private fun ProvideTextStyle(style: TextStyle, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalFlamingoTextStyle provides style, content = content)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ListItemScope.EndSlot(
    modifier: Modifier,
    end: @Composable () -> Unit,
    disabled: Boolean,
    isEndSlotClickableWhenDisabled: Boolean,
) = Box(
    modifier = modifier.padding(start = 8.dp)
        .run {
            if (!isEndSlotClickableWhenDisabled) {
                alpha(disabled).pointerInteropFilter { disabled }
            } else this
        },
    contentAlignment = Alignment.Center
) {
    end()
}

/**
 * Used to enforce scope in `internal` functions related to [ListItem] and not to pollute code
 * completion with them in `flamingo` module.
 */
internal object ListItemScope {
    val vPadding = 12.dp
    val hPadding = 16.dp
}

@Composable
private fun Actions(modifier: Modifier = Modifier, actions: ActionGroup) = with(actions) {
    Row(modifier = modifier) {
        if (secondAction == null) {
            with(firstAction) {
                Button(
                    size = ButtonSize.MEDIUM,
                    label = label,
                    onClick = onClick,
                    loading = loading,
                    disabled = disabled,
                    widthPolicy = ButtonWidthPolicy.TRUNCATING,
                )
            }
        } else {
            Box(modifier = Modifier.weight(1f)) {
                Button(
                    color = ButtonColor.Primary,
                    size = ButtonSize.MEDIUM,
                    label = firstAction.label,
                    onClick = firstAction.onClick,
                    loading = firstAction.loading,
                    disabled = firstAction.disabled,
                    widthPolicy = ButtonWidthPolicy.TRUNCATING,
                )
            }
            Spacer(modifier = Modifier.requiredWidth(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                Button(
                    size = ButtonSize.MEDIUM,
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

/**
 * Used to add padding and icons to [Text] inside [ListItem]
 * if [iconTint] == null, text color will be used for icon tint
 */
public data class TextWrapper(
    val text: AnnotatedString,
    val padding: PaddingValues = PaddingValues(0.dp),
    val icon: FlamingoIcon? = null,
    val iconTint: Color? = null,
    val iconPosition: TextIconPosition = TextIconPosition.START
) {
    public constructor(
        text: String,
        padding: PaddingValues = PaddingValues(0.dp),
        icon: FlamingoIcon? = null,
        iconTint: Color? = null,
        iconPosition: TextIconPosition = TextIconPosition.START
    ) : this(AnnotatedString(text), padding, icon, iconTint, iconPosition)

    public enum class TextIconPosition {
        START,
        END
    }
}


