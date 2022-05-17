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
import com.flamingo.components.button.ButtonIconPosition
import com.flamingo.components.button.ButtonSize
import com.flamingo.components.button.ButtonVariant
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.playground.R
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.playground.boast
import com.flamingo.demoapi.WhiteModeDemo
import com.flamingo.demoapi.parceNull
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.tintIcons
import com.flamingo.demoapi.findPreference
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
        var icon by mutableStateOf<FlamingoIcon?>(null)
        var iconPosition by mutableStateOf(ButtonIconPosition.START)
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
                    icon = icon,
                    iconPosition = iconPosition,
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

        configurePreference<DropDownPreference>("iconPosition") {
            val positions = ButtonIconPosition.values().map { it.toString() }.toTypedArray()
            entries = positions
            entryValues = positions
            onChange { iconPosition = ButtonIconPosition.valueOf(it); true }
            initPref(savedInstanceState, defVal = ButtonIconPosition.START)
        }

        configurePreference<DropDownPreference>("icon") {
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
                icon = (it as? String)?.parceNull()?.let { Flamingo.icons.fromName(context, it) }
                true
            }
            initPref(savedInstanceState, defVal = "null")
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
