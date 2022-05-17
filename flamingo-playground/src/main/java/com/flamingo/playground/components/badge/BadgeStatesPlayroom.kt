package com.flamingo.playground.components.badge

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.components.Badge
import com.flamingo.components.BadgeColor
import com.flamingo.components.BadgeSize
import com.flamingo.playground.R
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.WhiteModeDemo
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.findPreference

@StatesPlayroomDemo
class BadgeStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_badge, rootKey)
    }

    @Suppress("LongMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var label by mutableStateOf("Badge")
        var size by mutableStateOf(BadgeSize.BIG)
        var color by mutableStateOf<BadgeColor>(BadgeColor.Default)
        var white by mutableStateOf(false)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            WhiteModeDemo(white = white) { Badge(label, color, size) }
        }

        configurePreference<EditTextPreference>("label") {
            onChange { newValue ->
                label = newValue
                summary = label
                true
            }
            initPref(savedInstanceState, defVal = "Badge")
        }

        configurePreference<DropDownPreference>("size") {
            val contents = BadgeSize.values().map { it.name }.toTypedArray()
            entries = contents
            entryValues = contents
            onChange { newValue ->
                size = BadgeSize.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = BadgeSize.BIG.toString())
        }

        configurePreference<DropDownPreference>("color") {
            entries = BadgeColorNames.colorEntries()
            entryValues = BadgeColorNames.colorEntryValues()
            onChange { newValue ->
                val name = newValue.drop(1)
                color = when (newValue[0]) {
                    'K' -> BadgeColorNames.colorClasses
                        .first { it.qualifiedName == name }
                        .objectInstance!!
                    'E' -> BadgeColor.Gradient.valueOf(name)
                    else -> error("Unreachable")
                }
                true
            }
            initPref(savedInstanceState, defVal = "K" + BadgeColor.Default::class.qualifiedName!!)
        }

        configurePreference<SwitchPreferenceCompat>("white") {
            onChange { white = it; true }
            initPref(savedInstanceState, defVal = false)
        }
    }

    private object BadgeColorNames {
        val colorClasses = BadgeColor::class.sealedSubclasses
            .filterNot { it == BadgeColor.Gradient::class }
        private val gradientClassName = BadgeColor.Gradient::class.simpleName!!
        private val gradientValues = BadgeColor.Gradient.values()

        fun colorEntries(): Array<String> {
            val colorClassesEntries = colorClasses.map { it.simpleName!! }
            val gradientEntries = gradientValues.map { "$gradientClassName.${it.name}" }
            return (colorClassesEntries + gradientEntries).toTypedArray()
        }

        fun colorEntryValues(): Array<String> {
            val colorClassesEntryValues = colorClasses.map { "K${it.qualifiedName}" }
            val gradientEntryValues = gradientValues.map { "E${it.name}" }
            return (colorClassesEntryValues + gradientEntryValues).toTypedArray()
        }
    }
}
