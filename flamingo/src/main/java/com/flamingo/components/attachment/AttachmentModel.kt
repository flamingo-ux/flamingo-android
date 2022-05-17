package com.flamingo.components.attachment

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

public data class AttachmentModel(
    val preview: Preview = Preview.Default,
    val fileName: String,
    /**
     * Status of the uploading process (e.g. uploading, failed: no internet connection, uploaded)
     */
    val status: String? = null,
    val fileSize: String? = null,
    /**
     * If true, [Attachments.List.onClick] will be called. Used to open an attachment in a
     * viewer.
     */
    val clickable: Boolean,
    /**
     * If true, [Attachments.List.onDelete] will be called. Used to remove the attachment from the
     * server.
     */
    val deletable: Boolean,
) {
    /**
     * Left (start) area of the attachment
     */
    public sealed class Preview {
        /**
         * Indicates that the attachment is currently uploading
         */
        public object Loading : Preview()

        /**
         * Default icon. Used when there is no preview [Image] of the file available.
         */
        public object Default : Preview()

        /**
         * Displays a preview of the contents of the file
         */
        public data class Image(
            val image: Painter,
            val contentScale: ContentScale = ContentScale.Fit,
        ) : Preview()

        /**
         * Indicates that the uploading has failed. [Attachments.List.onRetry] is called when user
         * clicks on this preview.
         */
        public object Fail : Preview()
    }
}
