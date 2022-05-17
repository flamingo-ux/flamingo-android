package com.flamingo.playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.flamingo.Flamingo
import com.flamingo.components.BadgeColor
import com.flamingo.components.Text
import com.flamingo.components.tabrow.Tab
import com.flamingo.components.tabrow.TabRow
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

class PaletteColorsDemoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose { Content() }

    private data class Palette(
        val version: UInt,
        /**
         * Grouped by [PaletteColor.family]
         */
        val colors: Map<String, List<PaletteColor>>,
    )

    private data class PaletteColor(
        val family: String,
        val intensity: Int?,
        val value: Color,
    )

    private fun loadPalettes(): List<Palette> = Flamingo::class
        .declaredMemberProperties
        .filter { it.name.startsWith("palette") }
        .map { paletteProperty ->
            val palettePropertyInstance = paletteProperty.get(Flamingo)!!
            Palette(
                version = paletteProperty.name.substringAfterLast('V').toUIntOrNull() ?: 1u,
                colors = palettePropertyInstance::class
                    .declaredMemberProperties
                    .map { colorProperty ->
                        PaletteColor(
                            family = colorProperty.name.dropLastWhile { it.isDigit() },
                            intensity = colorProperty.name.takeLastWhile { it.isDigit() }
                                .toIntOrNull(),
                            value = (colorProperty as KProperty1<Any, Color>)
                                .get(palettePropertyInstance),
                        )
                    }
                    .sortedBy { it.intensity }
                    .groupBy { it.family },
            )
        }
        .sortedByDescending { it.version }

    private val deprecatedBadge = Tab.Badge("Deprecated", BadgeColor.Warning)

    @Composable
    private fun Content() {
        var palettes by remember { mutableStateOf<List<Palette>?>(null) }
        LaunchedEffect(Unit) { withContext(Dispatchers.IO) { palettes = loadPalettes() } }

        if (palettes == null) {
            Loading()
            return
        }

        var currentPalette by rememberSaveable { mutableStateOf(0) }
        LazyColumn {
            val palettes = palettes!!
            item { Introduction() }

            item { VersionSelectionTab(palettes, currentPalette) { currentPalette = it } }

            palettes[currentPalette].colors.entries.onEach { (family, colors) ->
                item { Separator(formatFamilyName(family)) }
                items(colors) { paletteColor -> ColorBar(paletteColor) }
            }
        }
    }

    private fun formatFamilyName(name: String): String =
        name.replaceFirstChar { it.titlecaseChar() }

    @Composable
    private fun VersionSelectionTab(
        palettes: List<Palette>,
        selectedTabIndex: Int,
        onTabSelect: (index: Int) -> Unit,
    ) {
        if (palettes.size < 2) return
        TabRow(
            tabs = palettes.mapIndexed { index, palette ->
                Tab(
                    label = "Version ${palette.version}",
                    badge = if (index == 0) null else deprecatedBadge,
                )
            },
            selectedTabIndex = selectedTabIndex,
            onTabSelect = onTabSelect,
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    @Composable
    private fun Introduction() {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            style = Flamingo.typography.body1,
            text = stringResource(R.string.palette_color_demo_introduction),
        )
    }

    @Composable
    private fun ColorBar(paletteColor: PaletteColor) {
        val clipboardManager = LocalClipboardManager.current
        val context = LocalContext.current
        val hexCopiedMsg = stringResource(R.string.palette_color_demo_hex_copied)

        val argb = paletteColor.value.toArgb()
        val hex = "#" + Integer.toHexString(argb).drop(2)
        val luminance = ColorUtils.calculateLuminance(argb)

        Text(
            modifier = Modifier
                .clickable {
                    clipboardManager.setText(AnnotatedString(hex))
                    context.showBoast("\"$hex\" $hexCopiedMsg")
                }
                .fillMaxWidth()
                .background(paletteColor.value)
                .padding(16.dp),
            textAlign = TextAlign.Center,
            color = with(Flamingo.palette) {
                if (luminance > LUMINANCE_THRESHOLD) black else white
            },
            text = buildString {
                append(paletteColor.family)
                paletteColor.intensity?.let {
                    append("-")
                    append(it)
                }
                append(" — ")
                append(hex)
            }
        )
    }

    @Composable
    private fun Separator(text: String) = Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(Flamingo.colors.separator)
            .padding(vertical = 4.dp, horizontal = 12.dp),
        text = text,
    )

    private companion object {
        private const val LUMINANCE_THRESHOLD = .5
    }
}
