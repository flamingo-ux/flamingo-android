package com.flamingo.playground.components.chip

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
import com.flamingo.components.Chip
import com.flamingo.components.ChipColor
import com.flamingo.components.ChipVariant
import com.flamingo.playground.R
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.playground.boast
import com.flamingo.demoapi.parceNull
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.theme.FlamingoIcon

@StatesPlayroomDemo
class ChipStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_chip, rootKey)
    }

    @Suppress("LongMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var label by mutableStateOf("Chip")
        var selected by mutableStateOf(false)
        var hasOnClick by mutableStateOf(false)
        var hasOnDelete by mutableStateOf(false)
        var icon by mutableStateOf<FlamingoIcon?>(null)
        var disabled by mutableStateOf(false)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            Chip(
                label = label,
                selected = selected,
                onClick = if (hasOnClick) boast(msg = "onClick") else null,
                onDelete = if (hasOnDelete) boast(msg = "onDelete") else null,
                icon = icon,
                disabled = disabled,
            )
        }

        configurePreference<EditTextPreference>("label") {
            onChange { newValue ->
                label = newValue
                summary = label
                true
            }
            initPref(savedInstanceState, defVal = "Chip")
        }

        configurePreference<SwitchPreferenceCompat>("selected") {
            onChange { newValue ->
                selected = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("hasOnClick") {
            onChange { newValue ->
                hasOnClick = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("hasOnDelete") {
            onChange { newValue ->
                hasOnDelete = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
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
            onChange { newValue ->
                icon = newValue.parceNull()?.let { Flamingo.icons.fromName(context, it) }
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        configurePreference<SwitchPreferenceCompat>("disabled") {
            onChange { newValue ->
                disabled = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }
    }
}
