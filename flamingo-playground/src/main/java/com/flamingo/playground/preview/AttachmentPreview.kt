package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.attachment.Attachment
import com.flamingo.components.attachment.AttachmentModel
import com.flamingo.components.attachment.Attachments
import com.flamingo.components.attachment.FilePickerButton
import com.flamingo.loremIpsum
import com.flamingo.playground.boast
import com.flamingo.playground.showBoast

@Preview
@Composable
@Suppress("MagicNumber")
fun AttachmentPreview() {
    val list = attachmentPreviewShortList
    val context = LocalContext.current
    Attachment(
        label = "Label",
        filePickerButton = FilePickerButton(
            context = context,
            onClick = context.boast(msg = "filePickerButton click")
        ),
        shortDescription = loremIpsum(10),
        longDescription = loremIpsum(30),
        attachments = Attachments.List(
            onClick = { index -> context.showBoast("onClick: ${list[index].fileName}") },
            onRetry = { index -> context.showBoast("onRetry: ${list[index].fileName}") },
            onDelete = { index -> context.showBoast("onDelete: ${list[index].fileName}") },
            list = list
        )
    )
}

private val attachmentPreviewShortList = listOf(
    AttachmentModel(
        preview = AttachmentModel.Preview.Loading,
        fileName = "File name2.docs",
        status = "Uploaded",
        fileSize = "45KB",
        clickable = true,
        deletable = false
    ),
    AttachmentModel(
        preview = AttachmentModel.Preview.Default,
        fileName = "File name.docs",
        status = "Uploaded",
        fileSize = "45KB",
        clickable = true,
        deletable = true
    ),
)
