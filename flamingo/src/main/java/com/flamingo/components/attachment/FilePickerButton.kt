package com.flamingo.components.attachment

import android.content.Context
import com.flamingo.R
import com.flamingo.components.button.ButtonColor

/**
 * Click on this button must open the native file picker.
 */
public data class FilePickerButton(
    val context: Context,
    val onClick: () -> Unit,
    val label: String = context.getString(R.string.attachment_file_picker_button_text),
    /**
     * If true — [ButtonColor.Primary], else - [ButtonColor.Default]
     */
    val isPrimary: Boolean = false,
    /**
     * If true, [onClick] would not be called
     */
    val disabled: Boolean = false,
)
