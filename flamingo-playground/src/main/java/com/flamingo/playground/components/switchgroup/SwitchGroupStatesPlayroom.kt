package com.flamingo.playground.components.switchgroup

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.components.switchgroup.SwitchData
import com.flamingo.components.switchgroup.SwitchGroup
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
class SwitchGroupStatesPlayroom : PreferenceFragmentCompat() {

    private val switchList = listOf(
        SwitchData(label = "switch", checked = true, onCheckedChange = {}),
        SwitchData(label = "long switch", onCheckedChange = {}),
        SwitchData(
            label = "long long long long long long long long long long switch",
            onCheckedChange = {}),
        SwitchData(label = "long switch", checked = true, disabled = true, onCheckedChange = {}),
        SwitchData(label = "long long long long switch", disabled = true, onCheckedChange = {}),
        SwitchData(label = "long switch", onCheckedChange = {}),
    )

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_switch_group, rootKey)
    }

    @Suppress("LongMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var label: String? by mutableStateOf(null)
        var description: String? by mutableStateOf(null)
        var errorText: String? by mutableStateOf(null)
        var required by mutableStateOf(false)
        var disabled by mutableStateOf(false)
        var switchAmount by mutableStateOf(2)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            SwitchGroup(
                switches = switchList.take(switchAmount),
                label = label,
                required = required,
                disabled = disabled,
                description = description,
                errorText = errorText
            )
        }

        configurePreference<SeekBarPreference>("switches") {
            onChange { newValue ->
                switchAmount = newValue
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