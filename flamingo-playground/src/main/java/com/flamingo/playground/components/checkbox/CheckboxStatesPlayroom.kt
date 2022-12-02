package com.flamingo.playground.components.checkbox

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.components.CheckBoxState
import com.flamingo.components.Checkbox
import com.flamingo.demoapi.*
import com.flamingo.playground.R
import com.flamingo.playground.utils.Boast

@StatesPlayroomDemo
class CheckboxStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_checkbox, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var disabled by mutableStateOf(false)
        var white by mutableStateOf(false)
        var state by mutableStateOf(CheckBoxState.DEFAULT)
        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            WhiteModeDemo(white = white) {
                var checked by remember { mutableStateOf(false) }
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                        Boast.showText(context, "Click: checked = $checked")
                    },
                    disabled = disabled,
                    state = state
                )
            }
        }

        configurePreference<SwitchPreferenceCompat>("disabled") {
            onChange { newValue ->
                disabled = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("white") {
            onChange { white = it; true }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("state") {
            val states = CheckBoxState.values().map { it.name }.toTypedArray()
            entries = states
            entryValues = states
            onChange { newValue ->
                state = CheckBoxState.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = CheckBoxState.DEFAULT)
        }
    }
}
