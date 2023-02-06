package com.flamingo.components.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.IconButton
import com.flamingo.components.IconButtonColor
import com.flamingo.components.IconButtonSize
import com.flamingo.components.IconButtonVariant
import com.flamingo.components.Text
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor


/**
 *  Displays custom [content] as a dialog with optional header and action buttons.
 *  Height of the dialog is capped at 600dp. [content] will scroll vertically if the limit is
 *  exceeded.
 *
 *  @param isVisible handles visibility of the [Modal] dialog
 *  @param title optional header title
 *  @param hasCloseButton optional button inside header, that invokes [onDismissRequest]
 *  @param onDismissRequest callback that is called when user tries to dismiss the dialog.
 *  It is __needed__ to include [isVisible] change inside this callback to properly close [Modal]
 *  @param properties default [Dialog] properties. __NOTE__ usePlatformDefaultWidth is ignored,
 *  because it is defaulted to false to avoid width issues.
 *  @param primaryButtonParams optional action button
 *  @param secondaryButtonParams secondary action button that is always present
 *  @param content content of the [Modal] dialog
 *
 * @sample com.flamingo.playground.preview.ModalPreview
 */
@OptIn(ExperimentalComposeUiApi::class)
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.ModalPreview",
    figma = "https://f.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=628%3A13&t=w1SoH0wu86xPKMre-1",
    specification = "https://confluence.companyname.ru/x/j4cjKQE",
    demo = ["com.flamingo.playground.components.modal.ModalStatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun Modal(
    isVisible: Boolean,
    title: String? = null,
    hasCloseButton: Boolean = true,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    primaryButtonParams: ButtonParams?,
    secondaryButtonParams: ButtonParams,
    content: @Composable () -> Unit
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                dismissOnClickOutside = properties.dismissOnClickOutside,
                dismissOnBackPress = properties.dismissOnBackPress,
                securePolicy = properties.securePolicy,
                usePlatformDefaultWidth = false
            )
        ) {
            Column(
                Modifier
                    .heightIn(max = 600.dp)
                    .fillMaxWidth(0.77f)
                    .background(Flamingo.colors.background)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                ModalHeaderLayout(Modifier.padding(horizontal = 8.dp)) {
                    if (title != null) Text(
                        modifier = Modifier
                            .layoutId("title")
                            .padding(vertical = 14.dp, horizontal = 8.dp),
                        text = title,
                        style = Flamingo.typography.h6,
                        color = Flamingo.colors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (hasCloseButton) Box(
                        modifier = Modifier
                            .layoutId("close")
                            .padding(vertical = 4.dp)
                    ) {
                        IconButton(
                            onClick = onDismissRequest,
                            icon = Flamingo.icons.X,
                            contentDescription = null,
                            variant = IconButtonVariant.TEXT,
                            size = IconButtonSize.MEDIUM,
                            color = IconButtonColor.DEFAULT
                        )
                    }
                }

                Box(
                    Modifier
                        .padding(top = 10.dp, bottom = 26.dp)
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                ) {
                    content()
                }

                if (primaryButtonParams != null) {
                    Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)) {
                        Button(
                            onClick = primaryButtonParams.onClick,
                            label = primaryButtonParams.label,
                            color = ButtonColor.Primary,
                            fillMaxWidth = true,
                            disabled = primaryButtonParams.disabled,
                            loading = primaryButtonParams.loading
                        )
                    }
                }

                Box(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                ) {
                    Button(
                        onClick = secondaryButtonParams.onClick,
                        label = secondaryButtonParams.label,
                        color = ButtonColor.Default,
                        fillMaxWidth = true,
                        disabled = secondaryButtonParams.disabled,
                        loading = secondaryButtonParams.loading
                    )
                }
            }
        }
    }
}

public data class ButtonParams(
    val label: String,
    val disabled: Boolean = false,
    val loading: Boolean = false,
    val onClick: () -> Unit
)
