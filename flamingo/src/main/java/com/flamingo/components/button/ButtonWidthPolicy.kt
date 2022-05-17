package com.flamingo.components.button

import androidx.compose.foundation.layout.Row
import androidx.compose.ui.text.style.TextOverflow

/**
 * Determines how [Button]'s width will be calculated. Works with [Button.fillMaxWidth].
 */
public enum class ButtonWidthPolicy {
    /**
     * Width will never be reduced and restricted by incoming constraints. [Button] will draw all of
     * it's content - icon and all label in one line.
     *
     * Label will always be displayed in one line. If label contains '\n's, they will be ignored.
     *
     * Must be used when [Button] is placed in a scrollable [Row].
     *
     * @sample com.flamingo.playground.components.button.ButtonsInRow
     * @sample com.flamingo.playground.components.button.Squeezed
     */
    STRICT,

    /**
     * If incoming width constrains are smaller than what is needed to draw a full [Button] (like in
     * [STRICT]), label will be truncated and [TextOverflow.Ellipsis] will be appended to the label.
     *
     * Label will always be displayed in one line. If label contains '\n's, they will be ignored.
     *
     * Icon will always be shown, even if incoming constrains are too small for it to be shown.
     *
     * @sample com.flamingo.playground.components.button.SplitBigText
     */
    TRUNCATING,

    /**
     * If incoming width constrains are smaller than what is needed to draw a full [Button] (like in
     * [STRICT]), label will be split into multiple lines.
     *
     * Icon will always be shown, even if incoming constrains are too small for it to be shown.
     *
     * @sample com.flamingo.playground.components.button.SplitMultiline
     */
    MULTILINE,
}
