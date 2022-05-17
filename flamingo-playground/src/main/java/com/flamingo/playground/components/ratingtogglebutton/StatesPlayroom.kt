package com.flamingo.playground.components.ratingtogglebutton

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.components.RatingToggleButton
import com.flamingo.playground.R
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.onChange
import com.flamingo.playground.utils.Boast

@StatesPlayroomDemo
class StatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_rating_toggle_button, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var disabled by mutableStateOf(false)
        var sameColor by mutableStateOf(false)
        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            var checked by remember { mutableStateOf(false) }
            RatingToggleButton(
                checked = checked,
                sameColor = sameColor,
                onCheckedChange = {
                    checked = it
                    Boast.showText(context, "Click: checked = $checked")
                },
                disabled = disabled,
            )
        }

        configurePreference<SwitchPreferenceCompat>("disabled") {
            onChange { newValue ->
                disabled = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("sameColor") {
            onChange { sameColor = it; true }
            initPref(savedInstanceState, defVal = false)
        }
    }
}
