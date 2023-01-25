package com.flamingo.playground.components.radiogroup

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.components.radiogroup.RadioButtonData
import com.flamingo.components.radiogroup.RadioGroup
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
class RadioGroupStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_radio_group, rootKey)
    }

    @Suppress("LongMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var label: String? by mutableStateOf(null)
        var description: String? by mutableStateOf(null)
        var errorText: String? by mutableStateOf(null)
        var required by mutableStateOf(false)
        var disabled by mutableStateOf(false)
        var radioAmount by mutableStateOf(2)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            var selectedRadio by remember { mutableStateOf(-1) }
            RadioGroup(
                radioButtons = listOf(
                    RadioButtonData(
                        label = "radioButton",
                        selected = selectedRadio == 0,
                        onClick = { selectedRadio = 0 }),
                    RadioButtonData(
                        label = "long radioButton",
                        selected = selectedRadio == 1,
                        onClick = { selectedRadio = 1 }),
                    RadioButtonData(
                        label = "long long long long long long long long long long radioButton",
                        selected = selectedRadio == 2,
                        onClick = { selectedRadio = 2 }),
                    RadioButtonData(
                        label = "long radioButton",
                        disabled = true,
                        selected = selectedRadio == 3,
                        onClick = { selectedRadio = 3 }),
                    RadioButtonData(
                        label = "long long long long radioButton",
                        disabled = true,
                        selected = selectedRadio == 4,
                        onClick = { selectedRadio = 4 }),
                    RadioButtonData(
                        label = "long radioButton",
                        selected = selectedRadio == 5,
                        onClick = { selectedRadio = 5 }),
                ).take(radioAmount),
                label = label,
                required = required,
                disabled = disabled,
                description = description,
                errorText = errorText
            )
        }

        configurePreference<SeekBarPreference>("radios") {
            onChange { newValue ->
                radioAmount = newValue
                true
            }
            initPref(savedInstanceState, defVal = 2)
            this.max = 6
            this.min = 2
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