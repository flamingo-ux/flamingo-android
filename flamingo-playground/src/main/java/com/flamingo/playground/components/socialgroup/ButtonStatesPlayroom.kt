package com.flamingo.playground.components.socialgroup

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.components.socialgroup.SocialGroup
import com.flamingo.components.socialgroup.SocialGroupColor
import com.flamingo.components.socialgroup.SocialGroupSize
import com.flamingo.components.socialgroup.button.Message
import com.flamingo.components.socialgroup.button.Share
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.playground.R
import com.flamingo.playground.boast

@StatesPlayroomDemo
class ShareStatesPlayroom : ButtonStatesPlayroom(isShare = true)

@StatesPlayroomDemo
class MessageStatesPlayroom : ButtonStatesPlayroom(isShare = false)

abstract class ButtonStatesPlayroom(private val isShare: Boolean) : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_socialbutton, rootKey)
    }

    @Suppress("LongMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var counter by mutableStateOf("Text")
        var disabled by mutableStateOf(false)
        var size by mutableStateOf(SocialGroupSize.BIG)
        var color by mutableStateOf(SocialGroupColor.DEFAULT)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            if (isShare) SocialGroup.Share(
                onClick = boast(),
                counter = counter,
                disabled = disabled,
                size = size,
            ) else SocialGroup.Message(
                onClick = boast(),
                counter = counter,
                disabled = disabled,
                size = size,
            )
        }

        configurePreference<EditTextPreference>("counter") {
            onChange { newValue ->
                counter = newValue
                summary = counter
                true
            }
            initPref(savedInstanceState, defVal = counter)
        }

        configurePreference<DropDownPreference>("size") {
            val contents = SocialGroupSize.values().map { it.name }.toTypedArray()
            entries = contents
            entryValues = contents
            onChange { newValue ->
                size = SocialGroupSize.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = size)
        }
        configurePreference<DropDownPreference>("color") {
            val contents = SocialGroupColor.values().map { it.name }.toTypedArray()
            entries = contents
            entryValues = contents
            onChange { newValue ->
                color = SocialGroupColor.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = color)
        }

        configurePreference<SwitchPreferenceCompat>("disabled") {
            onChange { newValue ->
                disabled = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = disabled)
        }
    }
}
