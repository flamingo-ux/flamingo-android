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
    "SpacingAroundParens"
)

package com.flamingo.components.attachment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.R
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.Avatar
import com.flamingo.components.AvatarBackground
import com.flamingo.components.AvatarContent
import com.flamingo.components.AvatarShape
import com.flamingo.components.AvatarSize
import com.flamingo.components.CircularLoader
import com.flamingo.components.CircularLoaderColor
import com.flamingo.components.FlamingoComponentBase
import com.flamingo.components.IconButton
import com.flamingo.components.IconButtonVariant
import com.flamingo.components.Text
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.components.listitem.AvatarSkeleton
import com.flamingo.components.listitem.ListItem
import com.flamingo.components.listitem.ListItemSkeleton
import com.flamingo.components.listitem.TextBlocksSkeleton

/**
 * This overload must be used ONLY if small number of attachments will be used. Else, overload with
 * [LazyListScope] receiver MUST BE used.
 *
 * @param labelAsterisk if label != null and labelAsterisk is true, red asterisk is appended to the
 * end of the label
 * @param shortDescription required description of the details of the file uploading (max file size,
 * supported formats, etc.)
 * @param longDescription optional addition to the [shortDescription] that is collapsed (hidden) by
 * default, but can be expanded by user.
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.AttachmentPreview",
    figma = "https://todo.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=22240%3A144536",
    specification = "https://todo.com/x/L5ctuQE",
    demo = ["com.flamingo.playground.components.attachment.LazyColumnTypicalUsage"],
    supportsWhiteMode = false,
)
@Composable
public fun Attachment(
    label: String? = null,
    labelAsterisk: Boolean = true,
    filePickerButton: FilePickerButton,
    shortDescription: String,
    longDescription: String? = null,
    attachments: Attachments?,
): Unit = FlamingoComponentBase {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Header(label, labelAsterisk, filePickerButton, shortDescription, longDescription)
        AttachmentsSection(attachments)
    }
}

/**
 * MUST BE used in the case of [attachments].size == A LOT.
 * Parent's [LazyColumn.contentPadding.horizontal] MUST BE zero.
 *
 * @see Attachment
 */
public fun LazyListScope.Attachment(
    label: String? = null,
    labelAsterisk: Boolean = true,
    filePickerButton: FilePickerButton,
    shortDescription: String,
    longDescription: String? = null,
    attachments: Attachments?,
) {
    item {
        FlamingoComponentBase(
            componentFunName = "com.flamingo.components.attachment.Attachment"
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Header(label, labelAsterisk, filePickerButton, shortDescription, longDescription)
            }
        }
    }
    AttachmentsSection(attachments)
}

@Composable
private fun Header(
    label: String?,
    labelAsterisk: Boolean,
    filePickerButton: FilePickerButton,
    shortDescription: String,
    longDescription: String?,
) {
    if (label != null) Text(
        text = buildAnnotatedString {
            append(label)
            withStyle(SpanStyle(color = Flamingo.colors.error)) {
                if (labelAsterisk) append('*')
            }
        },
        style = Flamingo.typography.body2,
    )

    Button(
        onClick = filePickerButton.onClick,
        label = filePickerButton.label,
        color = if (filePickerButton.isPrimary) ButtonColor.Primary else ButtonColor.Default,
        icon = Flamingo.icons.Paperclip,
        disabled = filePickerButton.disabled,
        fillMaxWidth = true,
        widthPolicy = ButtonWidthPolicy.TRUNCATING,
    )

    Text(
        text = shortDescription,
        color = Flamingo.colors.textSecondary,
        style = Flamingo.typography.caption2,
    )

    if (longDescription != null) Column {
        var expanded by remember { mutableStateOf(false) }
        Text(
            modifier = Modifier.clickable { expanded = !expanded },
            text = stringResource(id = if (expanded) {
                R.string.attachment_long_description_collapse
            } else {
                R.string.attachment_long_description_expand
            }
            ),
            color = Flamingo.colors.primary,
            style = Flamingo.typography.caption2,
        )

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Text(
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .padding(top = 8.dp, bottom = 16.dp),
                text = longDescription,
                color = Flamingo.colors.textSecondary,
                style = Flamingo.typography.caption2,
            )
        }
    }
}

@Composable
private fun AttachmentsSection(attachments: Attachments?) {
    when (attachments) {
        is Attachments.List -> Column {
            attachments.list.forEachIndexed { index, attachment ->
                attachment.Attachment(index, attachments)
            }
        }
        is Attachments.Loading -> Column { repeat(attachments.number) { AttachmentSkeleton() } }
        null -> Unit
    }
}

private fun LazyListScope.AttachmentsSection(attachments: Attachments?) = when (attachments) {
    is Attachments.List -> with(this) {
        itemsIndexed(attachments.list) { index, attachment ->
            attachment.Attachment(index, attachments)
        }
    }
    is Attachments.Loading -> items(attachments.number) { AttachmentSkeleton() }
    null -> Unit
}

@Composable
private fun AttachmentSkeleton() = ListItemSkeleton(
    textBlocks = TextBlocksSkeleton.TWO,
    avatar = AvatarSkeleton(AvatarSize.SIZE_56, AvatarShape.ROUNDED_CORNERS_MEDIUM)
)

@Composable
private fun AttachmentModel.Attachment(
    index: Int,
    attachments: Attachments.List,
) = ListItem(
    start = { AttachmentPreview(preview, attachments, index) },
    title = fileName,
    subtitle = status,
    description = fileSize,
    end = if (deletable) {
        {
            IconButton(
                onClick = { attachments.onDelete(index) },
                icon = Flamingo.icons.Trash2,
                variant = IconButtonVariant.TEXT,
                contentDescription = "delete"
            )
        }
    } else null,
    showDivider = false,
    onClick = if (clickable) {
        { attachments.onClick(index) }
    } else null
)

@Composable
private fun AttachmentPreview(
    preview: AttachmentModel.Preview,
    attachments: Attachments.List,
    index: Int,
) = when (preview) {
    AttachmentModel.Preview.Default -> AvatarPreview(
        content = AvatarContent.Icon(Flamingo.icons.File, AvatarBackground.GREY),
    )
    AttachmentModel.Preview.Fail -> AvatarPreview(
        content = AvatarContent.Icon(Flamingo.icons.RotateCcw, AvatarBackground.GREY),
        onClick = { attachments.onRetry(index) },
    )
    is AttachmentModel.Preview.Image -> AvatarPreview(
        content = AvatarContent.Image(preview.image, preview.contentScale),
    )
    AttachmentModel.Preview.Loading -> Box(contentAlignment = Alignment.Center) {
        AvatarPreview(content = AvatarContent.Letters(' ', ' ', AvatarBackground.GREY))
        CircularLoader(color = CircularLoaderColor.DEFAULT)
    }
}

@Composable
private fun AvatarPreview(content: AvatarContent, onClick: (() -> Unit)? = null) = Avatar(
    content = content,
    size = AvatarSize.SIZE_56,
    shape = AvatarShape.ROUNDED_CORNERS_MEDIUM,
    onClick = onClick,
    contentDescription = null
)
