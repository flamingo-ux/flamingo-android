package com.flamingo.components.attachment

public sealed class Attachments {
    /**
     * Displays [number] of skeletons. Used, for example, when attachments are loaded from server.
     */
    public data class Loading(val number: Int = 3) : Attachments()

    public data class List(
        /**
         * User clicks on the whole attachment. Implementation is intended to open a preview of the
         * attachment. Will be called only if [AttachmentModel.clickable] is true.
         * @param index of the attachment in the [list]
         */
        val onClick: (index: Int) -> Unit,
        /**
         * User clicks on the retry button. Implementation is intended to start uploading again.
         * @param index of the attachment in the [list]
         */
        val onRetry: (index: Int) -> Unit,
        /**
         * User deletes the attachment. Implementation is intended to remove the attachment from the
         * server. Will be called only if [AttachmentModel.deletable] is true.
         * @param index of the attachment in the [list]
         */
        val onDelete: (index: Int) -> Unit,
        val list: kotlin.collections.List<AttachmentModel>,
    ) : Attachments()
}
