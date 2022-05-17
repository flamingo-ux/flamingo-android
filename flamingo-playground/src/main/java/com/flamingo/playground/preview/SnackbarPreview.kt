package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.snackbar.Snackbar
import com.flamingo.components.snackbar.SnackbarData
import com.flamingo.components.snackbar.SnackbarDuration
import com.flamingo.loremIpsum
import com.flamingo.playground.internalComponents

@Preview
@Composable
@Suppress("MagicNumber")
fun SnackbarPreview() = internalComponents.Snackbar(snackbarData = object : SnackbarData {
    override val message: String = loremIpsum(5)
    override val actionLabel: String? = loremIpsum(1)
    override val duration: SnackbarDuration = SnackbarDuration.Indefinite
    override val actionOnNewLine: Boolean = false
    override val isError: Boolean = false

    override fun performAction() = Unit
    override fun dismiss() = Unit
})
