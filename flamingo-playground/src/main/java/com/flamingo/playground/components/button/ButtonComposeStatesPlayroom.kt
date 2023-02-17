package com.flamingo.playground.components.button

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.Flamingo
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.button.ButtonEndItem
import com.flamingo.components.button.ButtonSize
import com.flamingo.components.button.ButtonVariant
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.WhiteModeDemo
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
class ButtonComposeStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_button_compose, rootKey)
        preferenceScreen.tintIcons()
    }

    @Suppress("LongMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var label by mutableStateOf("")
        var loading by mutableStateOf(false)
        var size by mutableStateOf(ButtonSize.MEDIUM)
        var color by mutableStateOf<ButtonColor>(ButtonColor.Default)
        var variant by mutableStateOf(ButtonVariant.CONTAINED)
        var startIcon by mutableStateOf<FlamingoIcon?>(null)
        var endItem by mutableStateOf<ButtonEndItem?>(null)
        var endIcon by mutableStateOf<FlamingoIcon?>(null)
        var badgeLabel by mutableStateOf("")
        var disabled by mutableStateOf(false)
        var fillMaxWidth by mutableStateOf(false)
        var widthPolicy by mutableStateOf(ButtonWidthPolicy.MULTILINE)
        var white by mutableStateOf(false)
        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            WhiteModeDemo(white = white) {
                Button(
                    onClick = boast("Click"),
                    label = label,
                    loading = loading,
                    size = size,
                    color = color,
                    variant = variant,
                    startIcon = startIcon,
                    endItem = endItem,
                    disabled = disabled,
                    fillMaxWidth = fillMaxWidth,
                    widthPolicy = widthPolicy,
                )
            }
        }

        configurePreference<EditTextPreference>("label") {
            onChange { newValue ->
                summary = "\"$newValue\""
                label = newValue
                true
            }
            initPref(savedInstanceState, defVal = "Button")
        }

        configurePreference<SwitchPreferenceCompat>("loading") {
            onChange { loading = it as? Boolean ?: return@onChange false; true }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("size") {
            entries = ButtonSize.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { size = ButtonSize.valueOf(it); true }
            initPref(savedInstanceState, defVal = ButtonSize.MEDIUM)
        }

        configurePreference<DropDownPreference>("color") {
            val colorClasses = ButtonColor::class.sealedSubclasses
            entries = colorClasses.map { it.simpleName }.toTypedArray()
            entryValues = colorClasses.map { it.qualifiedName }.toTypedArray()
            onChange { newValue ->
                color = colorClasses.first { it.qualifiedName == newValue }.objectInstance!!
                true
            }
            initPref(savedInstanceState, defVal = ButtonColor.Default::class.qualifiedName!!)
        }

        configurePreference<DropDownPreference>("variant") {
            entries = ButtonVariant.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { variant = ButtonVariant.valueOf(it); true }
            initPref(savedInstanceState, defVal = ButtonVariant.CONTAINED)
        }

        configurePreference<DropDownPreference>("widthPolicy") {
            entries = ButtonWidthPolicy.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { widthPolicy = ButtonWidthPolicy.valueOf(it); true }
            initPref(savedInstanceState, defVal = ButtonWidthPolicy.MULTILINE)
        }

        configurePreference<DropDownPreference>("endItem") {
            val endItemClasses = arrayOf(
                "null",
                "Icon",
                "Badge",
            )
            entries = endItemClasses
            entryValues = endItemClasses
            onChange { newValue ->
                endItem = when (newValue) {
                    "null" -> null
                    "Icon" -> ButtonEndItem.Icon(endIcon ?: Flamingo.icons.Bell)
                    "Badge" -> ButtonEndItem.Badge(badgeLabel)
                    else -> null
                }

                findPreference("endIcon").isVisible =
                    endItem != null && endItem is ButtonEndItem.Icon

                findPreference("badgeLabel").isVisible =
                    endItem != null && endItem is ButtonEndItem.Badge

                true
            }
            initPref(savedInstanceState, defVal = "null")
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

        configurePreference<EditTextPreference>("badgeLabel") {
            onChange { newValue ->
                summary = "\"$newValue\""
                badgeLabel = newValue
                endItem = if (endItem != null) ButtonEndItem.Badge(badgeLabel) else null
                true
            }
            initPref(savedInstanceState, defVal = "badge")
        }

        configurePreference<DropDownPreference>("endIcon") {
            entries = arrayOf(
                "Airplay",
                "Bell",
                "Aperture",
            )
            entryValues = arrayOf(
                Flamingo.icons.Airplay.getName(context),
                Flamingo.icons.Bell.getName(context),
                Flamingo.icons.Aperture.getName(context),
            )
            onChange {
                endIcon = (it as? String)?.let { Flamingo.icons.fromName(context, it) }
                endItem = if (endItem != null) ButtonEndItem.Icon(
                    endIcon ?: Flamingo.icons.Airplay
                ) else null
                true
            }
            initPref(savedInstanceState, defVal = "airplay")
        }

        configurePreference<SwitchPreferenceCompat>("fillMaxWidth") {
            onChange { fillMaxWidth = it as? Boolean ?: return@onChange false; true }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("disabled") {
            onChange { disabled = it as? Boolean ?: return@onChange false; true }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("white") {
            onChange { white = it; true }
            initPref(savedInstanceState, defVal = false)
        }
    }
}
