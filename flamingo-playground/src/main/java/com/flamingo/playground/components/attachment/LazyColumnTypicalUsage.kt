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

package com.flamingo.playground.components.attachment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.Fragment
import com.flamingo.components.attachment.Attachment
import com.flamingo.components.attachment.AttachmentModel
import com.flamingo.components.attachment.Attachments
import com.flamingo.components.attachment.FilePickerButton
import com.flamingo.demoapi.FlamingoComponentDemoName
import com.flamingo.demoapi.R
import com.flamingo.demoapi.TypicalUsageDemo
import com.flamingo.loremIpsum
import com.flamingo.playground.Compose
import com.flamingo.playground.boast
import com.flamingo.playground.preview.EmptyStateComposePreview
import com.flamingo.playground.showBoast

@TypicalUsageDemo
@FlamingoComponentDemoName("In LazyColumn")
class LazyColumnTypicalUsage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose { Content() }

    @Composable
    private fun Content() {
        val list = attachmentPreviewList()
        val context = LocalContext.current
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            item { EmptyStateComposePreview() }
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
            Attachment(
                label = loremIpsum(20),
                labelAsterisk = false,
                filePickerButton = FilePickerButton(
                    context = context,
                    onClick = context.boast(msg = "filePickerButton click"),
                    label = loremIpsum(20),
                    isPrimary = true,
                ),
                shortDescription = loremIpsum(10),
                attachments = Attachments.List(
                    onClick = { index -> context.showBoast("onClick: ${list[index].fileName}") },
                    onRetry = { index -> context.showBoast("onRetry: ${list[index].fileName}") },
                    onDelete = { index -> context.showBoast("onDelete: ${list[index].fileName}") },
                    list = list
                )
            )
            items(3) { EmptyStateComposePreview() }
        }
    }

    @Composable
    private fun attachmentPreviewList(): List<AttachmentModel> = listOf(
        AttachmentModel(
            preview = AttachmentModel.Preview.Default,
            fileName = "File name.docs",
            status = "Uploaded",
            fileSize = "45KB",
            clickable = true,
            deletable = true
        ),
        AttachmentModel(
            preview = AttachmentModel.Preview.Loading,
            fileName = "File name2.docs",
            status = "Uploaded",
            fileSize = "45KB",
            clickable = true,
            deletable = false
        ),
        AttachmentModel(
            preview = AttachmentModel.Preview.Fail,
            fileName = "File name3.docs",
            status = "Fail",
            fileSize = "45KB",
            clickable = true,
            deletable = false
        ),
        AttachmentModel(
            preview = AttachmentModel.Preview.Image(painterResource(R.drawable.example_dog)),
            fileName = "File name4.docs",
            status = "Uploaded",
            fileSize = "45KB",
            clickable = true,
            deletable = true
        ),
        AttachmentModel(
            preview = AttachmentModel.Preview.Default,
            fileName = "File name5.docs",
            status = "Uploaded",
            fileSize = null,
            clickable = false,
            deletable = true
        ),
        AttachmentModel(
            preview = AttachmentModel.Preview.Default,
            fileName = "File name6.docs",
            status = null,
            fileSize = "45KB",
            clickable = true,
            deletable = true
        ),
        AttachmentModel(
            preview = AttachmentModel.Preview.Default,
            fileName = "File name7.docs",
            status = null,
            fileSize = null,
            clickable = true,
            deletable = true
        ),
    )
}
