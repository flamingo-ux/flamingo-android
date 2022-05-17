package com.flamingo.playground.components

import android.os.Bundle
import android.view.View
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.loremIpsum
import com.flamingo.playground.R
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.playground.getProperties
import com.flamingo.playground.utils.Boast
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.view.components.AlertMessage

@StatesPlayroomDemo
class AlertMessageStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_alert_message, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alertMessage = AlertMessage(requireContext())

        findPreference<DemoPreference>("component")?.setDesignComponent(alertMessage)
        alertMessage.ds.setMessage(loremIpsum(@Suppress("MagicNumber") 10))

        configurePreference<DropDownPreference>("variant") {
            val properties =
                getProperties<AlertMessage.Companion, Int>(AlertMessage, "VARIANT_")
            entries = properties.map { it.name }.toTypedArray()
            entryValues = properties.map { it.value.toString() }.toTypedArray()
            onChange { newValue ->
                alertMessage.ds.variant = newValue.toIntOrNull()
                    ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = AlertMessage.VARIANT_ERROR)
        }

        val onClickListener = View.OnClickListener { Boast.showText(context, "Click") }

        configurePreference<SwitchPreferenceCompat>("hasOnCloseClickListener") {
            onChange { newValue ->
                val newValue = newValue as? Boolean ?: return@onChange false
                alertMessage.ds.onCloseClick(if (newValue) onClickListener else null)
                true
            }
            initPref(savedInstanceState, defVal = true)
        }
    }
}
