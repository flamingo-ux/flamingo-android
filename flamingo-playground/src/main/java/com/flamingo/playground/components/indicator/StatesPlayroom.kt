package com.flamingo.playground.components.indicator

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import com.flamingo.Flamingo
import com.flamingo.components.Indicator
import com.flamingo.components.IndicatorColor
import com.flamingo.components.IndicatorSize
import com.flamingo.playground.R
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.parceNull
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.tintIcons
import com.flamingo.theme.FlamingoIcon

@StatesPlayroomDemo
class StatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_indicator_compose, rootKey)
        preferenceScreen.tintIcons()
    }

    @Suppress("LongMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var size by mutableStateOf(IndicatorSize.SMALL)
        var color by mutableStateOf(IndicatorColor.DEFAULT)
        var icon by mutableStateOf<FlamingoIcon?>(null)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            Indicator(
                size = size,
                color = color,
                icon = icon,
            )
        }

        configurePreference<DropDownPreference>("size") {
            entries = IndicatorSize.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { size = IndicatorSize.valueOf(it); true }
            initPref(savedInstanceState, defVal = size)
        }

        configurePreference<DropDownPreference>("color") {
            entries = IndicatorColor.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { newValue -> color = IndicatorColor.valueOf(newValue); true }
            initPref(savedInstanceState, defVal = color)
        }

        configurePreference<DropDownPreference>("icon") {
            entries = arrayOf(
                "null",
                "Airplay",
                "Bell",
                "Aperture",
            )
            entryValues = arrayOf(
                "null",
                Flamingo.icons.Airplay.getName(context),
                Flamingo.icons.Bell.getName(context),
                Flamingo.icons.Aperture.getName(context),
            )
            onChange {
                icon = (it as? String)?.parceNull()?.let { Flamingo.icons.fromName(context, it) }
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }
    }
}
