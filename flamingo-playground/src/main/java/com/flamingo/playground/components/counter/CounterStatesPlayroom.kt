package com.flamingo.playground.components.counter

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.components.counter.Counter
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.playground.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@StatesPlayroomDemo
class CounterStatesPlayroom : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_counter, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val count = MutableStateFlow(0)
        var maxCount by mutableStateOf(Int.MAX_VALUE)
        var loading by mutableStateOf(false)
        var minusDisabled by mutableStateOf(false)
        var plusDisabled by mutableStateOf(false)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            Counter(
                count = count.collectAsState().value,
                maxCount = maxCount,
                loading = loading,
                minusDisabled = minusDisabled,
                plusDisabled = plusDisabled,
                onCountChange = {
                    count.value = it
                })
        }

        configurePreference<EditTextPreference>("count") {
            onChange { newValue ->
                count.value = newValue.toIntOrNull() ?: return@onChange false
                summary = count.value.toString()
                true
            }
            initPref(savedInstanceState, defVal = "0")

            lifecycleScope.launch {
                count.collectLatest {
                    summary = it.toString()
                    text = it.toString()
                }
            }
        }

        configurePreference<EditTextPreference>("maxCount") {
            onChange { newValue ->
                maxCount = newValue.toIntOrNull() ?: return@onChange false
                summary = maxCount.toString()
                true
            }
            initPref(savedInstanceState, defVal = Int.MAX_VALUE.toString())
        }

        configurePreference<SwitchPreferenceCompat>("loading") {
            onChange { newValue ->
                loading = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("minusDisabled") {
            onChange { newValue ->
                minusDisabled = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("plusDisabled") {
            onChange { newValue ->
                plusDisabled = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }
    }
}