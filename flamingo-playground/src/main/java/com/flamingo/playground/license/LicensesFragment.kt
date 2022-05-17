package com.flamingo.playground.license

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import com.mikepenz.aboutlibraries.util.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.flamingo.components.Chip
import com.flamingo.components.EmptyState
import com.flamingo.components.EmptyStateImage
import com.flamingo.components.Text
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.listitem.AdvancedListItem
import com.flamingo.components.topappbar.CenterItem
import com.flamingo.components.topappbar.TopAppBar
import com.flamingo.playground.Compose
import com.flamingo.playground.Loading
import com.flamingo.playground.R
import com.flamingo.playground.openUrl
import com.flamingo.playground.utils.Boast

/**
 * A list of all libraries that this app depends on.
 */
class LicensesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose { Content() }

    @Composable
    private fun Content() {
        var libraries by remember { mutableStateOf<List<Library>?>(null) }
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) { libraries = loadLibraries(context) }
        }

        if (libraries == null) {
            Loading()
            return
        }

        Column {
            var searchRequest by remember { mutableStateOf("") }
            val listState = rememberLazyListState()
            TopAppBar(
                center = CenterItem.Search(
                    context = context,
                    value = searchRequest,
                    onValueChange = { searchRequest = it },
                ),
                listState = listState
            )
            LibrariesList(
                state = listState,
                libraries = libraries!!.filter {
                    it.name.contains(searchRequest) || it.artifactId.contains(searchRequest)
                },
            )
        }

        CreateReportButton(libraries!!, context)
    }

    private fun loadLibraries(context: Context) = Libs.Builder()
        .withContext(context)
        .build()
        .libraries
        .sortedByDescending { it.licenses.firstOrNull()?.name }

    @Composable
    private fun CreateReportButton(libraries: List<Library>, context: Context) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 18.dp, end = 18.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            val clipboardManager = LocalClipboardManager.current
            Button(
                onClick = { copyLibsReportToClipboard(libraries, clipboardManager, context) },
                label = stringResource(R.string.licenses_report_create),
                color = ButtonColor.Primary
            )
        }
    }

    private var licenseDialog by mutableStateOf<License?>(null)

    @Composable
    private fun LibrariesList(state: LazyListState, libraries: List<Library>) {
        if (libraries.isEmpty()) {
            NoLibraries()
            return
        }
        if (licenseDialog != null) {
            val license = licenseDialog!!
            LicenseDialog(onDismissRequest = { licenseDialog = null }, license = license)
        }
        val context = LocalContext.current
        LazyColumn(state = state) {
            items(libraries, key = { it.uniqueId }) { library -> library.LibraryItem(context) }
        }
    }

    @Composable
    private fun Library.LibraryItem(context: Context) = AdvancedListItem(
        title = { Text(text = name) },
        date = if (artifactVersion?.isNotBlank() == true) {
            AnnotatedString(artifactVersion!!)
        } else null,
        subtitle = { LibraryInfo(this) },
        description = { Licenses(this) },
        onClick = if (website?.isNotBlank() == true) {
            { context.openUrl(website!!) }
        } else null,
    )

    @Composable
    private fun NoLibraries() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            EmptyState(
                image = EmptyStateImage.EMPTY,
                title = stringResource(R.string.licenses_no_libraries),
                contentDescription = stringResource(R.string.licenses_no_libraries)
            )
        }
    }

    @Composable
    private fun Licenses(library: Library) {
        if (library.licenses.isEmpty()) return
        Row(modifier = Modifier.horizontalScroll(state = rememberScrollState())) {
            library.licenses.forEach { license ->
                Chip(label = license.name, onClick = { licenseDialog = license })
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }

    @Composable
    private fun LibraryInfo(library: Library) = Text(text = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(stringResource(R.string.licenses_report_identifier))
            append(": ")
        }
        append(library.uniqueId)
        append('\n')

        if (library.description?.isNotBlank() == true) {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(stringResource(R.string.licenses_report_description))
                append(": ")
            }
            append(library.description!!)
            append('\n')
        }

        if (library.website?.isNotBlank() == true) {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(stringResource(R.string.licenses_report_website))
                append(": ")
            }
            append(library.website!!)
            append('\n')
        }
    })

    private fun copyLibsReportToClipboard(
        libraries: List<Library>,
        clipboardManager: ClipboardManager,
        context: Context,
    ) {
        val libsReport = generateLibsReport(context, libraries)
        clipboardManager.setText(AnnotatedString(libsReport))
        Boast.showText(context, R.string.licenses_report_copied_to_clipboard)
    }
}
