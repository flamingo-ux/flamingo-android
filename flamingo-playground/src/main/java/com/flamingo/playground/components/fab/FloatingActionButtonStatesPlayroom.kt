package com.flamingo.playground.components.fab

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.Flamingo
import com.flamingo.components.IconButtonColor
import com.flamingo.components.IconButtonIndicator
import com.flamingo.components.IconButtonShape
import com.flamingo.components.IconButtonSize
import com.flamingo.components.IndicatorColor
import com.flamingo.components.IndicatorPlacement
import com.flamingo.components.fab.FloatingActionButton
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.playground.R
import com.flamingo.playground.boast

@StatesPlayroomDemo
class FloatingActionButtonStatesPlayroom : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_floating_action_button, rootKey)
    }

    @Suppress("LongMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var icon by mutableStateOf(Flamingo.icons.Aperture)
        var size by mutableStateOf(IconButtonSize.MEDIUM)
        var color by mutableStateOf(IconButtonColor.DEFAULT)
        var shape by mutableStateOf(IconButtonShape.CIRCLE)
        var indicator by mutableStateOf(false)
        var loading by mutableStateOf(false)
        var disabled by mutableStateOf(false)
        var indicatorColor by mutableStateOf(IndicatorColor.DEFAULT)
        var indicatorPlacement by mutableStateOf(IndicatorPlacement.TopEnd)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            FloatingActionButton(
                onClick = boast("Click"),
                icon = icon,
                contentDescription = null,
                size = size,
                color = color,
                shape = shape,
                indicator = if (indicator) IconButtonIndicator(
                    indicatorColor,
                    indicatorPlacement
                ) else null,
                loading = loading,
                disabled = disabled,
            )
        }

        configurePreference<SwitchPreferenceCompat>("loading") {
            onChange { loading = it as? Boolean ?: return@onChange false; true }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("size") {
            val sizes = IconButtonSize.values().map { it.name }.toTypedArray()
            entries = sizes
            entryValues = sizes
            onChange { size = IconButtonSize.valueOf(it); true }
            initPref(savedInstanceState, defVal = IconButtonSize.MEDIUM)
        }

        configurePreference<DropDownPreference>("color") {
            val colors = IconButtonColor.values().map { it.name }.toTypedArray()
            entries = colors
            entryValues = colors
            onChange {
                color = IconButtonColor.valueOf(it); true
            }
            initPref(savedInstanceState, defVal = IconButtonColor.DEFAULT)
        }

        configurePreference<DropDownPreference>("shape") {
            val shapes = IconButtonShape.values().map { it.name }.toTypedArray()
            entries = shapes
            entryValues = shapes
            onChange {
                shape = IconButtonShape.valueOf(it); true
            }
            initPref(savedInstanceState, defVal = IconButtonShape.CIRCLE)
        }

        configurePreference<DropDownPreference>("icon") {
            entries = arrayOf(
                "Airplay",
                "Bell",
                "Aperture",
            )
            entryValues = arrayOf(
                Flamingo.icons.Airplay.getName(context),
                Flamingo.icons.Bell.getName(context),
                Flamingo.icons.Aperture.getName(context),
            )
            onChange {
                icon = it.let { Flamingo.icons.fromName(context, it) }
                true
            }
            initPref(savedInstanceState, defVal = Flamingo.icons.Airplay.getName(context))
        }

        configurePreference<SwitchPreferenceCompat>("disabled") {
            onChange { disabled = it as? Boolean ?: return@onChange false; true }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("indicator") {
            onChange {
                indicator = it as? Boolean ?: return@onChange false
                findPreference("indicatorColor").isVisible = indicator
                findPreference("indicatorPlacement").isVisible = indicator

                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("indicatorColor") {
            val colors = IndicatorColor.values().map { it.name }.toTypedArray()
            entries = colors
            entryValues = colors
            onChange {
                indicatorColor = IndicatorColor.valueOf(it); true
            }
            initPref(savedInstanceState, defVal = IndicatorColor.DEFAULT)
        }

        configurePreference<DropDownPreference>("indicatorPlacement") {
            val placement = IndicatorPlacement.values().map { it.name }.toTypedArray()
            entries = placement
            entryValues = placement
            onChange {
                indicatorPlacement = IndicatorPlacement.valueOf(it); true
            }
            initPref(savedInstanceState, defVal = IndicatorPlacement.TopEnd)
        }
    }
}