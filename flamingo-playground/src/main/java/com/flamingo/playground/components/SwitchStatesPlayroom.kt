package com.flamingo.playground.components

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.components.Switch
import com.flamingo.playground.R
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.WhiteModeDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.onChange
import com.flamingo.playground.utils.Boast

@StatesPlayroomDemo
class SwitchStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_checkbox, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var on by mutableStateOf(true)
        var disabled by mutableStateOf(false)
        var white by mutableStateOf(false)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            WhiteModeDemo(white = white) {
                val context = LocalContext.current
                Switch(
                    checked = on,
                    disabled = disabled,
                    onCheckedChange = {
                        on = it
                        Boast.showText(context, "Click: isChecked = $it")
                    },
                )
            }
        }

        configurePreference<SwitchPreferenceCompat>("disabled") {
            onChange { disabled = it; true }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("white") {
            onChange { white = it; true }
            initPref(savedInstanceState, defVal = false)
        }
    }
}
