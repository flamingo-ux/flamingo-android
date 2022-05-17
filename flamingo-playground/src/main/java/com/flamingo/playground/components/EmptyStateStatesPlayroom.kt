package com.flamingo.playground.components

import android.os.Bundle
import android.view.View
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.components.Action
import com.flamingo.components.ActionGroup
import com.flamingo.playground.R
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.playground.getProperties
import com.flamingo.playground.utils.Boast
import com.flamingo.demoapi.wrapWithBraces
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.view.components.EmptyState
import com.flamingo.R as FlamingoR

@StatesPlayroomDemo
class EmptyStateStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_empty_state, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emptyState = EmptyState(requireContext())

        findPreference<DemoPreference>("component")?.setDesignComponent(emptyState)

        imageType(emptyState, savedInstanceState)
        icon(emptyState, savedInstanceState)
        hasOnButtonClickListener(emptyState, savedInstanceState)

        configurePreference<EditTextPreference>("title") {
            onChange { newValue ->
                emptyState.ds.title = newValue as? String ?: return@onChange false
                summary = emptyState.ds.title.wrapWithBraces()
                true
            }
            initPref(savedInstanceState, defVal = "")
        }

        configurePreference<EditTextPreference>("description") {
            onChange { newValue ->
                emptyState.ds.description = newValue as? String ?: return@onChange false
                summary = emptyState.ds.description.wrapWithBraces()
                true
            }
            initPref(savedInstanceState, defVal = "")
        }

        configurePreference<EditTextPreference>("buttonText") {
            onChange { newValue ->
                val actions = emptyState.ds.actions
                emptyState.ds.actions = actions?.copy(
                    firstAction = actions.firstAction.copy(
                        label = newValue as? String ?: return@onChange false
                    )
                ) ?: ActionGroup(
                    Action(
                        label = newValue as? String ?: return@onChange false,
                        onClick = {})
                )
                summary = (emptyState.ds.actions?.firstAction?.label ?: "").wrapWithBraces()
                true
            }
            initPref(savedInstanceState, defVal = "")
        }
    }

    private fun imageType(emptyState: EmptyState, savedInstanceState: Bundle?) {
        configurePreference<DropDownPreference>("imageType") {
            val properties = getProperties<EmptyState.Companion, Int>(EmptyState, "IMAGE_")
            entries = properties.map { it.name }.toTypedArray()
            entryValues = properties.map { it.value.toString() }.toTypedArray()
            onChange { newValue ->
                emptyState.ds.imageType = newValue.toIntOrNull() ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = EmptyState.IMAGE_NO_IMAGE)
        }
    }

    private fun hasOnButtonClickListener(emptyState: EmptyState, savedInstanceState: Bundle?) {
        val onClickListener = { Boast.showText(context, "Click") }

        configurePreference<SwitchPreferenceCompat>("hasOnButtonClickListener") {
            onChange { newValue ->
                val newValue = newValue as? Boolean ?: return@onChange false
                val actions = emptyState.ds.actions
                emptyState.ds.actions = actions?.copy(firstAction = actions.firstAction.copy(
                    onClick = { if (newValue) onClickListener.invoke() }
                ))
                true
            }
            initPref(savedInstanceState, defVal = true)
        }
    }

    private fun icon(emptyState: EmptyState, savedInstanceState: Bundle?) {
        configurePreference<DropDownPreference>("icon") {
            entries = arrayOf(
                "null",
                "ds_ic_airplay",
                "ds_ic_bell",
                "ds_ic_aperture",
                "ic_apps_box (not from design system icon set)",
            )
            entryValues = arrayOf(
                "null",
                FlamingoR.drawable.ds_ic_airplay.toString(),
                FlamingoR.drawable.ds_ic_bell.toString(),
                FlamingoR.drawable.ds_ic_aperture.toString(),
                R.drawable.ic_apps_box.toString(),
            )
            onChange { newValue ->
                emptyState.ds.iconRes = (newValue as? String ?: return@onChange false)
                    .let { if (it == "null") null else it.toInt() }
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }
    }
}
