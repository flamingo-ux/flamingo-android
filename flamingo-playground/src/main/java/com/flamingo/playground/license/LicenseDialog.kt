package com.flamingo.playground.license

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mikepenz.aboutlibraries.entity.License
import com.flamingo.Flamingo
import com.flamingo.components.Icon
import com.flamingo.components.Text
import com.flamingo.components.listitem.ListItem
import com.flamingo.playground.openUrl

/**
 * Displays detailed information about [license].
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun LicenseDialog(onDismissRequest: () -> Unit, license: License) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(Flamingo.colors.background, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp)),
        ) {
            val url = license.url?.takeIf { it.isNotBlank() }
            val context = LocalContext.current
            TopBar(license, url, context)
            license.licenseContent?.let { LicenseText(it) }
        }
    }
}

@Composable
private fun TopBar(
    license: License,
    url: String?,
    context: Context,
) {
    ListItem(
        title = license.name,
        subtitle = url,
        onClick = if (url?.isNotBlank() == true) {
            { context.openUrl(url) }
        } else null,
        end = { Icon(icon = Flamingo.icons.ExternalLink) }
    )
}

@Composable
private fun LicenseText(text: String) {
    Text(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState()),
        text = text,
        color = Flamingo.colors.textPrimary,
    )
}
