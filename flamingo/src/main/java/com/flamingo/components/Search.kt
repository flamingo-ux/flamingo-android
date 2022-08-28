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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.InternalComponents
import com.flamingo.R
import com.flamingo.alpha
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.theme.FlamingoTheme

/**
 * Used for entering search requests. Works just like [TextField].
 *
 * @param onClick if non-null, all text editing capabilities are disabled ([value] and
 * [onValueChange] are ignored), and entirety of the field becomes clickable, if [disabled] is
 * false. Else - non-clickable
 *
 * @param placeholder if null, placeholder won't be shown
 *
 * @param loading if true, magnifier will be replaced with a spinner. CANNOT be true simultaneously
 * with [disabled]
 *
 * @param disabled indicates to the user that this component is not interactive. Text will be
 * neither editable nor focusable, the input of the text field will not be selectable. Changes of
 * this parameter are animated. CANNOT be true simultaneously with [loading]
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.SearchPreview",
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx/Friend-UI-kit?node-id=5484%3A50186",
    specification = "https://confluence.companyname.ru/x/egLDOAE",
    demo = ["com.flamingo.playground.components.SearchStatesPlayroom"],
    supportsWhiteMode = true,
)
@Composable
public fun InternalComponents.Search(
    value: String,
    onValueChange: (String) -> Unit,
    onClick: (() -> Unit)? = null,
    placeholder: String? = stringResource(R.string.search_placeholder),
    loading: Boolean = false,
    disabled: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    focusRequester: FocusRequester? = null,
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    Search(
        value = textFieldValue,
        onValueChange = {
            textFieldValueState = it
            if (value != it.text) onValueChange(it.text)
        },
        onClick = onClick,
        placeholder = placeholder,
        loading = loading,
        disabled = disabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        focusRequester = focusRequester,
    )
}

/**
 * This overload provides access to the input text, cursor position, selection range and
 * IME composition. If you only want to observe an input text change, use the [Search]
 * overload with the [String] parameter instead.
 *
 * See [Search] for docs.
 */
@Composable
public fun InternalComponents.Search(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onClick: (() -> Unit)? = null,
    placeholder: String? = stringResource(R.string.search_placeholder),
    loading: Boolean = false,
    disabled: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    focusRequester: FocusRequester? = null,
): Unit = FlamingoComponentBase {
    require(!(loading && disabled)) {
        "Only ONE of the following properties can be true: loading, disabled"
    }
    val search: @Composable () -> Unit = {
        if (onClick != null) {
            Box(
                modifier = Modifier
                    .clip(searchShape)
                    .clickable(enabled = !disabled, onClick = onClick)
            ) {
                SearchTypingArea(TextFieldValue(), {}, placeholder, loading, disabled, {})
            }
        } else BasicTextField(
            value = value,
            onValueChange = { if (!disabled) onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .run { if (focusRequester != null) focusRequester(focusRequester) else this },
            enabled = !disabled,
            readOnly = false,
            textStyle = Flamingo.typography.body1,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            cursorBrush = SolidColor(
                if (Flamingo.isWhiteMode) Flamingo.palette.white else Flamingo.colors.primary
            ),
            decorationBox = { textField ->
                SearchTypingArea(value, onValueChange, placeholder, loading, disabled, textField)
            },
        )
    }
    if (Flamingo.isWhiteMode) FlamingoTheme(darkTheme = true, content = search) else search()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SearchTypingArea(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String?,
    loading: Boolean,
    disabled: Boolean,
    innerTextField: @Composable () -> Unit
) {
    val vertical = 4.dp
    val iconTint =
        if (Flamingo.isWhiteMode) Flamingo.palette.white else Flamingo.colors.textTertiary

    Row(
        modifier = Modifier
            .alpha(disabled, animate = true)
            .clip(searchShape)
            .background(Flamingo.colors.backgroundTextField)
            .padding(start = 8.dp)
            .requiredHeightIn(min = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Crossfade(targetState = loading, animationSpec = animSpec) { loading ->
            when {
                loading -> Box(Modifier.padding(end = 8.dp, vertical = vertical)) {
                    CircularLoader(CircularLoaderSize.MEDIUM, CircularLoaderColor.PRIMARY)
                }
                else -> Icon(
                    modifier = Modifier
                        .padding(end = 8.dp, vertical = vertical)
                        .requiredSize(24.dp),
                    icon = Flamingo.icons.Search,
                    tint = iconTint
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp, vertical = vertical)
        ) {
            Placeholder(placeholder, value.annotatedString)
            Box { innerTextField() }
        }

        AnimatedVisibility(
            visible = value.annotatedString.isNotEmpty(),
            modifier = Modifier.padding(end = 1.dp, vertical = 1.dp),
            enter = fadeIn(animationSpec = animSpec),
            exit = fadeOut(animationSpec = animSpec),
        ) {
            Box(
                modifier = Modifier
                    .requiredSize(30.dp)
                    .clip(CircleShape)
                    .clickable(
                        enabled = !disabled,
                        role = Role.Button,
                        onClick = { onValueChange(TextFieldValue()) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.requiredSize(24.dp),
                    icon = Flamingo.icons.XCircle,
                    tint = iconTint
                )
            }
        }
    }
}

private fun Modifier.padding(
    start: Dp = 0.dp,
    end: Dp = 0.dp,
    vertical: Dp = 0.dp,
): Modifier = this.padding(
    start = start,
    end = end,
    top = vertical,
    bottom = vertical,
)

private val animSpec = tween<Float>(100, easing = LinearEasing)
private val searchShape = RoundedCornerShape(CornerSize(100))
