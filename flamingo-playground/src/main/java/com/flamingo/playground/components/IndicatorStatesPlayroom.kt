package com.flamingo.playground.components

import android.os.Bundle
import android.view.View
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import com.flamingo.playground.R
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.playground.getProperties
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.findPreference
import com.flamingo.view.components.Indicator

@StatesPlayroomDemo
class IndicatorStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_indicator, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val indicator = Indicator(requireContext())
        findPreference<DemoPreference>("component")?.setDesignComponent(indicator)

        configurePreference<DropDownPreference>("color") {
            val properties = getProperties<Indicator.Companion, Int>(Indicator, "COLOR_")
            entries = properties.map { it.name }.toTypedArray()
            entryValues = properties.map { it.value.toString() }.toTypedArray()
            onChange { newValue ->
                indicator.ds.color = newValue.toInt()
                true
            }
        }

        configurePreference<DropDownPreference>("size") {
            val properties = getProperties<Indicator.Companion, Int>(Indicator, "SIZE_")
            entries = properties.map { it.name }.toTypedArray()
            entryValues = properties.map { it.value.toString() }.toTypedArray()
            onChange { newValue ->
                indicator.ds.size = newValue.toInt()
                true
            }
        }
    }
}
