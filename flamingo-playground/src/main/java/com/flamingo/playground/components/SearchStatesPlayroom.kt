package com.flamingo.playground.components

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.components.Search
import com.flamingo.components.SearchSize
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.WhiteModeDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.parceNull
import com.flamingo.demoapi.wrapWithBraces
import com.flamingo.playground.R
import com.flamingo.playground.boast
import com.flamingo.playground.internalComponents
import com.flamingo.R as FlamingoR

@StatesPlayroomDemo
class SearchStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_search, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var value by mutableStateOf("")
        var onClick by mutableStateOf(false)
        var placeholder by mutableStateOf<String?>(null)
        var loading by mutableStateOf(true)
        var disabled by mutableStateOf(false)
        var white by mutableStateOf(false)
        var size by mutableStateOf(SearchSize.SMALL)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            WhiteModeDemo(white = white) {
                internalComponents.Search(
                    value = value,
                    onClick = if (onClick) boast("Click") else null,
                    onValueChange = { value = it },
                    placeholder = placeholder,
                    loading = loading,
                    disabled = disabled,
                    size = size
                )
            }
        }

        configurePreference<SwitchPreferenceCompat>("onClick") {
            onChange { onClick = it; true }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<EditTextPreference>("placeholder") {
            onChange { newValue ->
                placeholder = (newValue as? String)?.parceNull()
                summary = placeholder?.wrapWithBraces() ?: "null"
                true
            }
            initPref(
                savedInstanceState,
                defVal = context.getString(FlamingoR.string.search_placeholder)
            )
        }

        configurePreference<SwitchPreferenceCompat>("loading") {
            onChange { loading = it; true }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("disabled") {
            onChange { disabled = it; true }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("white") {
            onChange { white = it; true }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("size") {
            val sizes = SearchSize.values().map { it.name }.toTypedArray()
            entries = sizes
            entryValues = sizes
            onChange { newValue ->
                size = SearchSize.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = SearchSize.SMALL)
        }
    }
}
