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
import com.flamingo.components.rating.Rating
import com.flamingo.components.rating.RatingSize
import com.flamingo.playground.R
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.tintIcons
import com.flamingo.playground.showBoast

@StatesPlayroomDemo
class RatingStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_rating, rootKey)
        preferenceScreen.tintIcons()
    }

    @Suppress("LongMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var value by mutableStateOf(0u)
        var onSelected by mutableStateOf(true)
        var halves by mutableStateOf(false)
        var dragEnabled by mutableStateOf(true)
        var vibrationEnabled by mutableStateOf(true)
        var wholeSteps by mutableStateOf(5u)
        var size by mutableStateOf(RatingSize.MEDIUM)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            Rating(
                value = value,
                onSelected = if (onSelected) {
                    { value = it }
                } else null,
                halves = halves,
                dragEnabled = dragEnabled,
                vibrationEnabled = vibrationEnabled,
                wholeSteps = wholeSteps,
                size = size,
            )
        }

        configurePreference<EditTextPreference>("wholeSteps") {
            onChange { newValue ->
                summary = "\"$newValue\""
                val uInt = newValue.toUIntOrNull()
                if (uInt == null) {
                    requireContext().showBoast("Wrong number format")
                    return@onChange false
                }
                wholeSteps = uInt
                true
            }
            initPref(savedInstanceState, defVal = wholeSteps.toString())
        }

        configurePreference<SwitchPreferenceCompat>("onSelected") {
            onChange { onSelected = it as? Boolean ?: return@onChange false; true }
            initPref(savedInstanceState, defVal = onSelected)
        }

        configurePreference<SwitchPreferenceCompat>("halves") {
            onChange { halves = it as? Boolean ?: return@onChange false; true }
            initPref(savedInstanceState, defVal = halves)
        }

        configurePreference<SwitchPreferenceCompat>("dragEnabled") {
            onChange { dragEnabled = it as? Boolean ?: return@onChange false; true }
            initPref(savedInstanceState, defVal = dragEnabled)
        }

        configurePreference<SwitchPreferenceCompat>("vibrationEnabled") {
            onChange { vibrationEnabled = it as? Boolean ?: return@onChange false; true }
            initPref(savedInstanceState, defVal = vibrationEnabled)
        }

        configurePreference<DropDownPreference>("size") {
            entries = RatingSize.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { size = RatingSize.valueOf(it); true }
            initPref(savedInstanceState, defVal = size)
        }
    }
}
