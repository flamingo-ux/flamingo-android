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

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flamingo.ALPHA_DISABLED
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.components.TextFieldIcon.IconPlacement
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.button.ButtonSize
import com.flamingo.components.button.ButtonVariant
import com.flamingo.theme.FlamingoIcon
import com.flamingo.theme.FlamingoTheme

/**
 * Allows displaying [Icon] or [IconButton] in the [TextField]. If [onClick] is null, [Icon] will be
 * displayed. Else - [IconButton].
 *
 * @param placement in the [TextField]. If [IconPlacement.START], [onClick] MUST BE null
 *
 * @param contentDescription text used by accessibility services to describe what this [Icon] or
 * [IconButton] represents. This should always be provided unless this [Icon] or [IconButton] is
 * used for decorative purposes, and does not represent a meaningful action that a user can take.
 * This text should be localized, such as by using [androidx.compose.ui.res.stringResource] or
 * similar
 */
public data class TextFieldIcon(
    val icon: FlamingoIcon,
    val onClick: (() -> Unit)? = null,
    val placement: IconPlacement = IconPlacement.END,
    val contentDescription: String?,
) {
    init {
        if (placement == IconPlacement.START) require(onClick == null) {
            "If placement == IconPlacement.START, onClick MUST BE null"
        }
    }

    public enum class IconPlacement { START, END, }
}

/**
 * Allows displaying [Button] at the end of the [TextField]
 */
public data class TextFieldButton(
    val label: String,
    val onClick: () -> Unit,
    val loading: Boolean = false,
    val disabled: Boolean = false,
)

/**
 * Used for text input.
 *
 * @param value the input [TextFieldValue] to be shown in the text field
 *
 * @param onValueChange the callback that is triggered when the input service updates values in
 * [TextFieldValue]. An updated [TextFieldValue] comes as a parameter of the callback
 *
 * @param disabled controls the disabled state of the [TextField]. When `true`, the text field will
 * be neither editable nor focusable, the input of the text field will not be selectable,
 * visually text field will appear in the disabled UI state. Changes of this parameter are animated
 *
 * @param label the optional label to be displayed outside the text field container
 *
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and
 * the input text is empty. Appearance/disappearance of the [placeholder] is animated
 *
 * @param error indicates if the text field's current value is in error state. If set to
 * true, the [label], [helperText], char counter and text field's border will be displayed in error
 * color. If [errorText] is not null, it will be shown instead of [helperText] whether it is null or
 * not. Changes of this parameter are animated
 *
 * @param errorText regardless of whether [helperText] is null or not, if [error] is true,
 * [errorText] will be shown instead of [helperText]. Changes of this parameter are animated
 *
 * @param helperText if non-null, [helperText] will be shown under the text field. If [error] is
 * true and [errorText] is non-null, [helperText] will be replaced with [errorText]. Changes of this
 * parameter are animated
 *
 * @param multiline when set to false, this text field becomes a single horizontally scrolling
 * text field instead of wrapping onto multiple lines. The keyboard will be informed to not show
 * the return key as the [ImeAction]. Note that [maxVisibleLines] parameter will be ignored as the
 * maxLines attribute will be automatically set to 1
 *
 * @param maxVisibleLines the maximum height in terms of maximum number of visible lines. Should be
 * equal or greater than 4. Note that this parameter will be ignored and instead [maxVisibleLines]
 * will be set to 1 if [multiline] is set to false
 *
 * @param required if true and [label] is not null, red asterisk will be appended at the end of the
 * [label]. Changes of this parameter are animated
 *
 * @param maxCharNumber if not null, char counter will be shown under the text field container. If
 * [value] contains more than [maxCharNumber] chars, only the first [maxCharNumber] will be drawn.
 * Length of chars is the length of the [value], NOT the length of the [value] after
 * [visualTransformation]. Must be in the range of [5, [Int.MAX_VALUE]]
 *
 * @param loading if true, [CircularLoader] will be displayed in the icon area, and [icon] will be
 * hidden
 *
 * @param iconAreaAlignment defines vertical alignment of the contents in the icon area - either
 * [icon], or [CircularLoader] from [loading], or [button]. But when [value] contains no `\n`,
 * [iconAreaAlignment] is overridden with [TextFieldIconAlignment.CENTER]
 *
 * @param icon allows displaying [Icon] or [IconButton] in the [TextField]. If
 * [TextFieldIcon.onClick] is null, [Icon] will be displayed. Else - [IconButton]
 *
 * @param button if non-null, [Button] in [ButtonVariant.TEXT] is displayed at the end of the
 * [TextField]
 *
 * @param size controls vertical size of the [TextField]
 *
 * @param onClick if non-null, and [disabled] is false, editing capabilities of the [TextField]
 * become disabled, [TextField] becomes clickable
 *
 * @param bottomPadding specifies padding from the bottom of the whole [TextField]
 *
 * @param keyboardOptions software keyboard options that contains configuration such as
 * [KeyboardType] and [ImeAction]
 *
 * @param keyboardActions when the input service emits an IME action, the corresponding callback
 * is called. Note that this IME action may be different from what you specified in
 * [KeyboardOptions.imeAction]
 *
 * @param visualTransformation transforms the visual representation of the input [value].
 * For example, you can use [androidx.compose.ui.text.input.PasswordVisualTransformation] to create
 * a password text field. By default, no visual transformation is applied
 */
@FlamingoComponent(
    displayName = "Text Field",
    preview = "com.flamingo.playground.preview.TextFieldPreview",
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx/Friend-UI-kit?node-id=628%3A9",
    specification = "https://confluence.companyname.ru/x/KQr3KgE",
    demo = ["com.flamingo.playground.components.TextFieldStatesPlayroom"],
    supportsWhiteMode = true,
)
@UsedInsteadOf("androidx.compose.material.TextField", "androidx.compose.material.OutlinedTextField")
@Composable
public fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String? = null,
    required: Boolean = false,
    size: TextFieldSize = TextFieldSize.MEDIUM,
    error: Boolean = false,
    errorText: String? = null,
    helperText: String? = null,
    maxCharNumber: Int? = null,
    loading: Boolean = false,
    disabled: Boolean = false,
    multiline: Boolean = false,
    maxVisibleLines: Int = 4,
    icon: TextFieldIcon? = null,
    iconAreaAlignment: TextFieldIconAlignment = TextFieldIconAlignment.TOP,
    button: TextFieldButton? = null,
    onClick: (() -> Unit)? = null,
    bottomPadding: TextFieldBottomPadding = TextFieldBottomPadding.MEDIUM,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    TextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValueState = it
            if (value != it.text) onValueChange(it.text)
        },
        label = label,
        placeholder = placeholder,
        required = required,
        size = size,
        error = error,
        errorText = errorText,
        helperText = helperText,
        maxCharNumber = maxCharNumber,
        loading = loading,
        disabled = disabled,
        multiline = multiline,
        maxVisibleLines = maxVisibleLines,
        icon = icon,
        button = button,
        iconAreaAlignment = iconAreaAlignment,
        onClick = onClick,
        bottomPadding = bottomPadding,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

/**
 * Used for text input.
 *
 * This overload provides access to the input text, cursor position, selection range and
 * IME composition. If you only want to observe an input text change, use the [TextField]
 * overload with the [String] parameter instead.
 *
 * See [TextField] for docs.
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
public fun TextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String? = null,
    placeholder: String? = null,
    required: Boolean = false,
    size: TextFieldSize = TextFieldSize.MEDIUM,
    error: Boolean = false,
    errorText: String? = null,
    helperText: String? = null,
    maxCharNumber: Int? = null,
    loading: Boolean = false,
    disabled: Boolean = false,
    multiline: Boolean = false,
    maxVisibleLines: Int = 4,
    icon: TextFieldIcon? = null,
    button: TextFieldButton? = null,
    iconAreaAlignment: TextFieldIconAlignment = TextFieldIconAlignment.TOP,
    onClick: (() -> Unit)? = null,
    bottomPadding: TextFieldBottomPadding = TextFieldBottomPadding.MEDIUM,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
): Unit = FlamingoComponentBase {
    require(maxVisibleLines >= 4)
    if (maxCharNumber != null) require(maxCharNumber in 5..Int.MAX_VALUE)

    val textField: @Composable () -> Unit = {
        val isFocused = interactionSource.collectIsFocusedAsState().value
        val transformedText = remember(value.annotatedString, visualTransformation) {
            visualTransformation.filter(value.annotatedString)
        }.text
        val errorDependantTextColor = Flamingo.colors
            .run { if (error) this.error else textSecondary }
            .let { animateColorAsState(it, spring(stiffness = SPRING_STIFFNESS)).value }

        val alpha by animateFloatAsState(
            if (disabled) ALPHA_DISABLED else 1f,
            animationSpec = spring(stiffness = SPRING_STIFFNESS)
        )
        Column(
            modifier = Modifier
                .alpha(alpha)
                .padding(bottom = bottomPadding.value)
        ) {
            if (label != null) Label(label, required, errorDependantTextColor)

            BasicTextField(
                value = value.limitChars(maxCharNumber),
                onValueChange = { if (!disabled) onValueChange(it.limitChars(maxCharNumber)) },
                modifier = Modifier.fillMaxWidth().clip(fieldShape).run {
                    if (onClick != null && !disabled) {
                        clickable(role = Role.Button, onClick = onClick)
                    } else this
                },
                enabled = !disabled && onClick == null,
                readOnly = false,
                textStyle = Flamingo.typography.body1,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = !multiline,
                maxLines = if (multiline) maxVisibleLines else 1,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                cursorBrush = SolidColor(
                    if (Flamingo.isWhiteMode) {
                        Flamingo.palette.white
                    } else {
                        Flamingo.colors.primary
                    }
                ),
                decorationBox = { innerTextField ->
                    TypingArea(
                        iconAreaAlignment,
                        disabled,
                        multiline,
                        isFocused,
                        size,
                        transformedText,
                        error,
                        placeholder,
                        icon,
                        button,
                        loading,
                        innerTextField,
                    )
                },
            )

            if (helperText != null || errorText != null || maxCharNumber != null) {
                BottomTextBlock(
                    error, helperText, errorText, errorDependantTextColor, maxCharNumber,
                    textLength = value.annotatedString.length
                )
            }
        }
    }
    if (Flamingo.isWhiteMode) FlamingoTheme(darkTheme = true, content = textField) else textField()
}

private fun TextFieldValue.limitChars(maxCharNumber: Int?): TextFieldValue {
    return if (maxCharNumber != null && text.length >= maxCharNumber) take(maxCharNumber) else this
}

@Composable
private fun Label(
    label: String,
    required: Boolean,
    errorDependantTextColor: Color,
) {
    Text(
        modifier = Modifier
            .padding(bottom = 6.dp)
            .animateContentSize(spring(stiffness = SPRING_STIFFNESS)),
        text = buildAnnotatedString {
            append(label)
            if (required) withStyle(SpanStyle(color = Flamingo.colors.error)) { append("*") }
        },
        color = errorDependantTextColor,
        style = Flamingo.typography.caption2
    )
}

/**
 * If [helperText] is non-null, it will be shown. If [error] is true AND [errorText] is non-null,
 * it will be shown instead of [helperText] (whether or not [helperText] is null).
 */
@Composable
private fun BottomTextBlock(
    error: Boolean,
    helperText: String?,
    errorText: String?,
    errorDependantTextColor: Color,
    maxCharNumber: Int?,
    textLength: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
    ) {
        /** box will always be drawn, because [Counter] MUST BE glued to the end of the row */
        Box(modifier = Modifier.weight(1f)) {
            val errorText = if (error) errorText else null
            val text = errorText ?: helperText
            if (text != null) {
                HelperText(
                    text = text,
                    color = errorDependantTextColor
                )
            }
        }
        maxCharNumber?.let { Counter(textLength, it, errorDependantTextColor) }
    }
}

@Composable
private fun RowScope.Counter(
    textLength: Int,
    maxCharNumber: Int?,
    errorDependantTextColor: Color,
) = Text(
    modifier = Modifier.Companion
        .align(Alignment.Top)
        .padding(horizontal = 8.dp),
    text = "$textLength/$maxCharNumber",
    color = errorDependantTextColor,
    style = Flamingo.typography.caption2
)

@Composable
private fun HelperText(text: String, color: Color) {
    Crossfade(targetState = text, animationSpec = spring(stiffness = SPRING_STIFFNESS)) {
        Text(
            text = it,
            color = color,
            style = Flamingo.typography.caption2
        )
    }
}

private fun TextFieldValue.take(
    maxCharNumber: Int,
) = copy(annotatedString = annotatedString.subSequence(0, maxCharNumber))

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
private fun TypingArea(
    iconAreaAlignment: TextFieldIconAlignment,
    disabled: Boolean,
    multiline: Boolean,
    isFocused: Boolean,
    size: TextFieldSize,
    transformedText: AnnotatedString,
    error: Boolean,
    placeholder: String?,
    icon: TextFieldIcon?,
    button: TextFieldButton?,
    loading: Boolean,
    innerTextField: @Composable () -> Unit,
) {
    val borderWidth = 1.dp
    val verticalPadding = size.verticalPadding - borderWidth

    val borderColor = when {
        error -> Flamingo.colors.error
        isFocused -> Flamingo.colors.primary
        else -> Flamingo.colors.outlineDark
    }.let { animateColorAsState(targetValue = it, spring(stiffness = SPRING_STIFFNESS)).value }

    Row(
        modifier = Modifier
            .clip(fieldShape)
            .border(borderWidth, borderColor, fieldShape)
            .padding(
                top = verticalPadding,
                bottom = verticalPadding,
                start = 12.dp - borderWidth,
                end = 12.dp - borderWidth,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val alignmentModifier = Modifier.align(
            alignment = if (multiline && transformedText.contains('\n')) {
                iconAreaAlignment.toComposeAlignment()
            } else Alignment.CenterVertically
        )

        if (icon != null && icon.placement == IconPlacement.START) {
            IconArea(alignmentModifier, disabled, loading = false, icon)
        }

        Box(modifier = Modifier.weight(1f)) {
            Placeholder(placeholder, transformedText)
            Box { innerTextField() }
        }

        if (loading || (icon != null && icon.placement == IconPlacement.END)) {
            IconArea(alignmentModifier, disabled, loading, icon)
        }

        if (button != null) Box(
            modifier = alignmentModifier
                .padding(start = 8.dp)
                .pointerInteropFilter { disabled }
        ) {
            Button(
                onClick = button.onClick,
                label = button.label,
                loading = button.loading,
                disabled = button.disabled,
                variant = ButtonVariant.TEXT,
                color = ButtonColor.Primary,
                size = ButtonSize.SMALL
            )
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun RowScope.IconArea(
    @Suppress("ModifierParameter") alignmentModifier: Modifier,
    disabled: Boolean,
    loading: Boolean,
    icon: TextFieldIcon?,
) {
    Box(
        modifier = Modifier
            .run {
                if (loading || icon == null) padding(start = 8.dp)
                else when (icon.placement) {
                    IconPlacement.START -> padding(end = 8.dp)
                    IconPlacement.END -> padding(start = 8.dp)
                }
            }
            /**
             * [IconButton] will go out of the bounds: icon will be centered inside the box
             * (icon in [IconButtonSize.SMALL] is 24 dp), but ripple will be drawn outside
             * the box, which is expected
             */
            .requiredSize(24.dp)
            .then(alignmentModifier)
            /** blocks clicks on the [IconButton] */
            .pointerInteropFilter { disabled }
    ) {
        when {
            loading -> CircularLoader(color = CircularLoaderColor.PRIMARY)
            icon?.onClick != null -> IconButton(
                size = IconButtonSize.SMALL,
                color = IconButtonColor.DEFAULT,
                variant = IconButtonVariant.TEXT,
                onClick = icon.onClick,
                icon = icon.icon,
                contentDescription = icon.contentDescription
            )
            icon != null -> Icon(
                modifier = Modifier.requiredSize(24.dp),
                icon = icon.icon,
                contentDescription = icon.contentDescription,
                tint = Flamingo.colors.textTertiary
            )
        }
    }
}

public enum class TextFieldIconAlignment {
    TOP, CENTER, BOTTOM;

    @Composable
    internal fun toComposeAlignment(): Alignment.Vertical = when (this) {
        TOP -> Alignment.Top
        CENTER -> Alignment.CenterVertically
        BOTTOM -> Alignment.Bottom
    }
}

public enum class TextFieldSize(internal val verticalPadding: Dp) {
    SMALL(4.dp),
    MEDIUM(8.dp),
    LARGE(12.dp),
}

public enum class TextFieldBottomPadding(internal val value: Dp) {
    NONE(0.dp),
    SMALL(16.dp),
    MEDIUM(24.dp),
    LARGE(32.dp),
}

private val fieldShape = RoundedCornerShape(8.dp)
private const val SPRING_STIFFNESS = 700f
