package com.flamingo.playground.components

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.getDrawable
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.tintIcons
import com.flamingo.playground.R
import com.flamingo.playground.getProperties
import com.flamingo.playground.utils.Boast
import com.flamingo.playground.utils.exhaustive
import com.flamingo.view.components.Avatar
import com.flamingo.view.components.Indicator
import com.flamingo.R as FlamingoR
import com.flamingo.demoapi.R as DemoR

@StatesPlayroomDemo
class AvatarStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_avatar, rootKey)
        preferenceScreen.tintIcons()
    }

    @Suppress("LongMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val avatar = Avatar(requireContext())

        findPreference<DemoPreference>("component")?.setDesignComponent(avatar)

        configurePreference<DropDownPreference>("function") {
            val functions =
                arrayOf("setLetters", "setIcon", "setDrawable", "showIndicator", "hideIndicator")
            entries = functions
            entryValues = functions
            onChange { newValue ->
                val selectedFunction = newValue
                findPreference("call_function").isEnabled = selectedFunction.isNotBlank()
                updateFunctionParamPrefsVisibility(selectedFunction)
                true
            }
            initPref(savedInstanceState, defVal = "setLetters")
        }

        configurePreference<Preference>("call_function") {
            setOnPreferenceClickListener {
                avatar.callFunction(selectedFunction = dropDownValue("function")!!)
                true
            }
            isEnabled = dropDownValue("function") != null
        }

        configurePreference<EditTextPreference>("letters") {
            onChange { newValue -> summary = newValue; true }
            initPref(savedInstanceState, defVal = "AA")
        }

        configurePreference<DropDownPreference>("icon") {
            entries = arrayOf(
                "ds_ic_airplay",
                "ds_ic_bell",
                "ds_ic_aperture",
                "ic_apps_box (not from design system icon set)",
            )
            entryValues = arrayOf(
                FlamingoR.drawable.ds_ic_airplay.toString(),
                FlamingoR.drawable.ds_ic_bell.toString(),
                FlamingoR.drawable.ds_ic_aperture.toString(),
                R.drawable.ic_apps_box.toString(),
            )
        }

        configurePreference<DropDownPreference>("drawable") {
            entries = arrayOf("human", "dog", "plant")
            entryValues = arrayOf(
                DemoR.drawable.example_human.toString(),
                DemoR.drawable.example_dog.toString(),
                DemoR.drawable.example_plant.toString(),
            )
        }

        configurePreference<DropDownPreference>("background") {
            val properties = getProperties<Avatar.Companion, Int>(Avatar, "BACKGROUND_")
            entries = properties.map { it.name }.toTypedArray()
            entryValues = properties.map { it.value.toString() }.toTypedArray()
        }

        configurePreference<DropDownPreference>("shape") {
            val properties = getProperties<Avatar.Companion, Int>(Avatar, "SHAPE_")
            entries = properties.map { it.name }.toTypedArray()
            entryValues = properties.map { it.value.toString() }.toTypedArray()
        }

        configurePreference<DropDownPreference>("size") {
            val properties = getProperties<Avatar.Companion, Int>(Avatar, "SIZE_")
            entries = properties.map { it.name }.toTypedArray()
            entryValues = properties.map { it.value.toString() }.toTypedArray()
        }

        configurePreference<SwitchPreferenceCompat>("disabled") {
            onChange { newValue ->
                avatar.ds.disabled = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        val onClickListener = View.OnClickListener { Boast.showText(context, "Click") }

        configurePreference<SwitchPreferenceCompat>("hasOnClickListener") {
            onChange { newValue ->
                val newValue = newValue as? Boolean ?: return@onChange false
                avatar.ds.setOnClickListener(if (newValue) onClickListener else null)
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("indicatorColor") {
            val properties = getProperties<Indicator.Companion, Int>(Indicator, "COLOR_")
            entries = properties.map { it.name }.toTypedArray()
            entryValues = properties.map { it.value.toString() }.toTypedArray()
        }
    }

    private fun updateFunctionParamPrefsVisibility(selectedFunction: String) {
        when (selectedFunction) {
            "setLetters" -> {
                findPreference("letters").isVisible = true
                findPreference("icon").isVisible = false
                findPreference("drawable").isVisible = false
                findPreference("background").isVisible = true
                findPreference("shape").isVisible = true
                findPreference("size").isVisible = true
                findPreference("indicatorColor").isVisible = false
            }
            "setIcon" -> {
                findPreference("letters").isVisible = false
                findPreference("icon").isVisible = true
                findPreference("drawable").isVisible = false
                findPreference("background").isVisible = true
                findPreference("shape").isVisible = true
                findPreference("size").isVisible = true
                findPreference("indicatorColor").isVisible = false
            }
            "setDrawable" -> {
                findPreference("letters").isVisible = false
                findPreference("icon").isVisible = false
                findPreference("drawable").isVisible = true
                findPreference("background").isVisible = false
                findPreference("shape").isVisible = true
                findPreference("size").isVisible = true
                findPreference("indicatorColor").isVisible = false
            }
            "showIndicator" -> {
                findPreference("letters").isVisible = false
                findPreference("icon").isVisible = false
                findPreference("drawable").isVisible = false
                findPreference("background").isVisible = false
                findPreference("shape").isVisible = false
                findPreference("size").isVisible = false
                findPreference("indicatorColor").isVisible = true
            }
            "hideIndicator" -> {
                findPreference("letters").isVisible = false
                findPreference("icon").isVisible = false
                findPreference("drawable").isVisible = false
                findPreference("background").isVisible = false
                findPreference("shape").isVisible = false
                findPreference("size").isVisible = false
                findPreference("indicatorColor").isVisible = false
            }
        }
    }

    private fun Avatar.callFunction(selectedFunction: String) = when (selectedFunction) {
        "setLetters" -> ds.setLetters(
            letters = (findPreference("letters") as EditTextPreference).text!!,
            background = dropDownValue("background")!!.toInt(),
            shape = dropDownValue("shape")!!.toInt(),
            avatarSize = dropDownValue("size")!!.toInt(),
        )
        "setIcon" -> ds.setIcon(
            iconRes = dropDownValue("icon")!!.toInt(),
            background = dropDownValue("background")!!.toInt(),
            shape = dropDownValue("shape")!!.toInt(),
            avatarSize = dropDownValue("size")!!.toInt(),
        )
        "setDrawable" -> ds.setDrawable(
            drawable = getDrawable(requireContext(), dropDownValue("drawable")!!.toInt())!!,
            shape = dropDownValue("shape")!!.toInt(),
            avatarSize = dropDownValue("size")!!.toInt(),
        )
        "showIndicator" -> ds.showIndicator(color = dropDownValue("indicatorColor")!!.toInt())
        "hideIndicator" -> ds.hideIndicator()
        else -> error("Unknown function name")
    }.exhaustive

    private fun dropDownValue(prefKey: String): String? {
        return (findPreference(prefKey) as DropDownPreference).value
    }
}
