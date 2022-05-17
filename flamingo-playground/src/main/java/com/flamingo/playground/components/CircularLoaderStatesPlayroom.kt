package com.flamingo.playground.components

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.components.CircularLoader
import com.flamingo.components.CircularLoaderColor
import com.flamingo.components.CircularLoaderSize
import com.flamingo.playground.R
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.WhiteModeDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.findPreference

@StatesPlayroomDemo
class CircularLoaderStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_circular_progress, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var size by mutableStateOf(CircularLoaderSize.MEDIUM)
        var color by mutableStateOf(CircularLoaderColor.DEFAULT)
        var white by mutableStateOf(false)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            WhiteModeDemo(white = white) {
                CircularLoader(size, color)
            }
        }

        configurePreference<DropDownPreference>("size") {
            entries = CircularLoaderSize.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { newValue ->
                size = CircularLoaderSize.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = size)
        }

        configurePreference<DropDownPreference>("color") {
            entries = CircularLoaderColor.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { newValue ->
                color = CircularLoaderColor.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = color)
        }

        configurePreference<SwitchPreferenceCompat>("white") {
            onChange { white = it; true }
            initPref(savedInstanceState, defVal = false)
        }
    }
}
