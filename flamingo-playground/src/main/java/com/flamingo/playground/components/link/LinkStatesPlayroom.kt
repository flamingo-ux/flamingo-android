package com.flamingo.playground.components.link

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.Flamingo
import com.flamingo.components.Link
import com.flamingo.components.LinkSize
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.parceNull
import com.flamingo.demoapi.tintIcons
import com.flamingo.playground.R
import com.flamingo.playground.boast
import com.flamingo.theme.FlamingoIcon

@StatesPlayroomDemo
class LinkStatesPlayroom : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_link, rootKey)
        preferenceScreen.tintIcons()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var hasOnClick by mutableStateOf(false)
        var label by mutableStateOf("Link")
        var size by mutableStateOf(LinkSize.NORMAL)
        var colorName by mutableStateOf(LinkDemoColors.Primary)
        var loading by mutableStateOf(false)
        var startIcon: FlamingoIcon? by mutableStateOf(null)
        var endIcon: FlamingoIcon? by mutableStateOf(null)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            val color = getColorByName(colorName)
            Link(
                label = label,
                onClick = if (hasOnClick) boast("Link onClick") else null,
                size = size,
                color = color,
                loading = loading,
                startIcon = startIcon,
                endIcon = endIcon
            )
        }

        configurePreference<EditTextPreference>("label") {
            onChange { newValue ->
                summary = "\"$newValue\""
                label = newValue
                true
            }
            initPref(savedInstanceState, defVal = "Link")
        }

        configurePreference<SwitchPreferenceCompat>("loading") {
            onChange { loading = it as? Boolean ?: return@onChange false; true }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("hasOnClick") {
            onChange { hasOnClick = it as? Boolean ?: return@onChange false; true }
            initPref(savedInstanceState, defVal = true)
        }

        configurePreference<DropDownPreference>("size") {
            entries = LinkSize.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { size = LinkSize.valueOf(it); true }
            initPref(savedInstanceState, defVal = LinkSize.NORMAL)
        }

        configurePreference<DropDownPreference>("color") {
            entries = LinkDemoColors.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { newValue ->
                colorName = LinkDemoColors.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = LinkDemoColors.Primary)
        }

        configurePreference<DropDownPreference>("startIcon") {
            entries = arrayOf(
                "null",
                "Airplay",
                "Bell",
                "Aperture",
            )
            entryValues = arrayOf(
                "null",
                Flamingo.icons.Airplay.getName(context),
                Flamingo.icons.Bell.getName(context),
                Flamingo.icons.Aperture.getName(context),
            )
            onChange {
                startIcon =
                    (it as? String)?.parceNull()?.let { Flamingo.icons.fromName(context, it) }
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        configurePreference<DropDownPreference>("endIcon") {
            entries = arrayOf(
                "null",
                "Airplay",
                "Bell",
                "Aperture",
            )
            entryValues = arrayOf(
                "null",
                Flamingo.icons.Airplay.getName(context),
                Flamingo.icons.Bell.getName(context),
                Flamingo.icons.Aperture.getName(context),
            )
            onChange {
                endIcon = (it as? String)?.parceNull()?.let { Flamingo.icons.fromName(context, it) }
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }
    }

    // needed because Flamingo.colors is a @Composable function, that can't be called in configurePreference
    @Composable
    private fun getColorByName(color: LinkDemoColors): Color {
        return when (color) {
            LinkDemoColors.Primary -> Flamingo.colors.primary
            LinkDemoColors.TextPrimary -> Flamingo.colors.textPrimary
            LinkDemoColors.TextSecondary -> Flamingo.colors.textSecondary
            LinkDemoColors.TextTertiary -> Flamingo.colors.textTertiary
            LinkDemoColors.Error -> Flamingo.colors.error
            LinkDemoColors.Warning -> Flamingo.colors.warning
        }
    }

    private enum class LinkDemoColors {
        Primary,
        TextPrimary,
        TextSecondary,
        TextTertiary,
        Error,
        Warning
    }
}