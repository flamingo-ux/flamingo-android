package com.flamingo.playground.components.chipgroup

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.Flamingo
import com.flamingo.components.chipgroup.ChipData
import com.flamingo.components.chipgroup.ChipGroup
import com.flamingo.components.dropdown.DropdownItem
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.parceNull
import com.flamingo.demoapi.wrapWithBraces
import com.flamingo.playground.R


@StatesPlayroomDemo
class ChipGroupStatesPlayroom : PreferenceFragmentCompat() {

    private val chipsList = listOf(
        ChipData(label = "Chip", onClick = {}, disabled = true),
        ChipData(label = "Long Long Chip", onClick = {}),
        ChipData(label = "Long Chip", onClick = {}, disabled = true, onDelete = {}),
        ChipData(label = "Long Long Long Long Long Long Long Long Chip", onClick = {}),
        ChipData(label = "dropdown Chip", onClick = {}, selected = true, dropdownItems = listOf(
            DropdownItem(label = "item 1"),
            DropdownItem(label = "item 2", icon = Flamingo.icons.Bell),
            DropdownItem(label = "long long item 3"),
        )),
        ChipData(label = "Long Long Long Chip", onClick = {}),
        ChipData(label = "Chip", icon = Flamingo.icons.Bell, onClick = {}),
        ChipData(label = "Chip", onClick = {}),
        ChipData(
            label = "Long Long Chip",
            icon = Flamingo.icons.Aperture,
            onDelete = {},
            onClick = {}),
        ChipData(label = "Long Chip", onClick = {}),
    )

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_chip_group, rootKey)
    }

    @Suppress("LongMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var label: String? by mutableStateOf(null)
        var description: String? by mutableStateOf(null)
        var errorText: String? by mutableStateOf(null)
        var required by mutableStateOf(false)
        var disabled by mutableStateOf(false)
        var chipAmount by mutableStateOf(1)
        var list by mutableStateOf(chipsList)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            ChipGroup(
                chips = list.take(chipAmount),
                label = label,
                required = required,
                disabled = disabled,
                description = description,
                errorText = errorText
            )
        }

        configurePreference<SeekBarPreference>("chips") {
            onChange { newValue ->
                chipAmount = newValue
                true
            }
            initPref(savedInstanceState, defVal = 1)
            this.max = 10
            this.min = 1
        }

        configurePreference<Preference>("shuffle") {
            setOnPreferenceClickListener {
                list = list.shuffled()
                true
            }
        }

        configurePreference<EditTextPreference>("label") {
            onChange { newValue ->
                label = newValue.parceNull()
                summary = label?.wrapWithBraces() ?: "null"
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        configurePreference<EditTextPreference>("description") {
            onChange { newValue ->
                description = newValue.parceNull()
                summary = description?.wrapWithBraces() ?: "null"
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        configurePreference<EditTextPreference>("errorText") {
            onChange { newValue ->
                errorText = newValue.parceNull()
                summary = errorText?.wrapWithBraces() ?: "null"
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        configurePreference<SwitchPreferenceCompat>("required") {
            onChange { newValue ->
                required = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
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