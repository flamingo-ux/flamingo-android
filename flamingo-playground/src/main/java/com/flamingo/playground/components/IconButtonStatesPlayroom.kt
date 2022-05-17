package com.flamingo.playground.components

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnAttach
import androidx.core.view.updateLayoutParams
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.playground.R
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.playground.getProperties
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.tintIcons
import com.flamingo.view.components.Button
import com.flamingo.view.components.IconButton
import com.flamingo.playground.utils.Boast
import com.flamingo.R as FlamingoR

@StatesPlayroomDemo
class IconButtonStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_icon_button, rootKey)
        preferenceScreen.tintIcons()
    }

    @Suppress("LongMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = IconButton(requireContext())

        findPreference<DemoPreference>("component")?.setDesignComponent(button)
        button.doOnAttach { it.updateLayoutParams { width = ViewGroup.LayoutParams.WRAP_CONTENT } }

        configurePreference<SwitchPreferenceCompat>("loading") {
            onChange { button.ds.loading = it as? Boolean ?: return@onChange false; true }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("size") {
            val properties = getProperties<IconButton.Companion, Int>(IconButton, "SIZE_")
            entries = properties.map { it.name }.toTypedArray()
            entryValues = properties.map { it.value.toString() }.toTypedArray()
            onChange { button.ds.size = it.toIntOrNull() ?: return@onChange false; true }
            initPref(savedInstanceState, defVal = Button.SIZE_MEDIUM)
        }

        configurePreference<DropDownPreference>("color") {
            val properties = getProperties<IconButton.Companion, Int>(IconButton, "COLOR_")
            entries = properties.map { it.name }.toTypedArray()
            entryValues = properties.map { it.value.toString() }.toTypedArray()
            onChange {
                button.ds.color = it.toIntOrNull() ?: return@onChange false; true
            }
            initPref(savedInstanceState, defVal = Button.COLOR_PRIMARY)
        }

        configurePreference<DropDownPreference>("variant") {
            val properties = getProperties<IconButton.Companion, Int>(IconButton, "VARIANT_")
            entries = properties.map { it.name }.toTypedArray()
            entryValues = properties.map { it.value.toString() }.toTypedArray()
            onChange {
                button.ds.variant = it.toIntOrNull() ?: return@onChange false; true
            }
            initPref(savedInstanceState, defVal = Button.VARIANT_CONTAINED)
        }

        configurePreference<DropDownPreference>("icon") {
            entries = arrayOf(
                "0 (no icon)",
                "ds_ic_airplay",
                "ds_ic_bell",
                "ds_ic_aperture",
                "ic_apps_box (not from design system icon set)",
            )
            entryValues = arrayOf(
                "0",
                FlamingoR.drawable.ds_ic_airplay.toString(),
                FlamingoR.drawable.ds_ic_bell.toString(),
                FlamingoR.drawable.ds_ic_aperture.toString(),
                R.drawable.ic_apps_box.toString(),
            )
            onChange { button.ds.icon = it.toIntOrNull() ?: return@onChange false; true }
            initPref(savedInstanceState, defVal = "0")
        }

        configurePreference<SwitchPreferenceCompat>("disabled") {
            onChange { button.ds.disabled = it as? Boolean ?: return@onChange false; true }
            initPref(savedInstanceState, defVal = false)
        }

        val onClickListener = View.OnClickListener { Boast.showText(context, "Click") }

        configurePreference<SwitchPreferenceCompat>("hasOnClickListener") {
            onChange { newValue ->
                val newValue = newValue as? Boolean ?: return@onChange false
                button.ds.setOnClickListener(if (newValue) onClickListener else null)
                true
            }
            initPref(savedInstanceState, defVal = true)
        }
    }
}
