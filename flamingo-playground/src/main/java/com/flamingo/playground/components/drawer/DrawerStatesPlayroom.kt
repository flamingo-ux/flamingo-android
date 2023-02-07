package com.flamingo.playground.components.drawer

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.components.Text
import com.flamingo.components.button.Button
import com.flamingo.components.drawer.Drawer
import com.flamingo.components.modal.ButtonParams
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.parceNull
import com.flamingo.demoapi.wrapWithBraces
import com.flamingo.loremIpsum
import com.flamingo.playground.R
import com.flamingo.playground.boast


@StatesPlayroomDemo
class DrawerStatesPlayroom : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_drawer, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var title: String? by mutableStateOf("")
        var hasCloseButton by mutableStateOf(true)
        var dismissOnBackPress by mutableStateOf(true)
        var dismissOnClickOutside by mutableStateOf(true)
        var hasPrimaryButton by mutableStateOf(true)
        var primaryButtonLabel by mutableStateOf("")
        var primaryButtonDisabled by mutableStateOf(false)
        var primaryButtonLoading by mutableStateOf(false)
        var secondaryButtonLabel by mutableStateOf("")
        var secondaryButtonDisabled by mutableStateOf(false)
        var secondaryButtonLoading by mutableStateOf(false)
        var isLTRAnimation by mutableStateOf(true)
        var widthFraction by mutableStateOf(1f)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            var isVisible by remember { mutableStateOf(false) }

            Button(onClick = { isVisible = true }, label = "Click to show Drawer")

            Drawer(
                isVisible = isVisible,
                title = title,
                hasCloseButton = hasCloseButton,
                properties = DialogProperties(
                    dismissOnBackPress = dismissOnBackPress,
                    dismissOnClickOutside = dismissOnClickOutside
                ),
                onDismissRequest = { isVisible = false },
                isLTRAnimation = isLTRAnimation,
                widthFraction = widthFraction,
                primaryButtonParams = if (hasPrimaryButton) ButtonParams(
                    label = primaryButtonLabel,
                    disabled = primaryButtonDisabled,
                    loading = primaryButtonLoading,
                    onClick = boast("primary click")
                ) else null,
                secondaryButtonParams = ButtonParams(
                    label = secondaryButtonLabel,
                    disabled = secondaryButtonDisabled,
                    loading = secondaryButtonLoading,
                    onClick = boast("secondary click")
                )
            ) {
                Text(modifier = Modifier.padding(horizontal = 16.dp), text = loremIpsum(200))
            }
        }

        configurePreference<EditTextPreference>("title") {
            onChange { newValue ->
                title = newValue.parceNull()
                summary = title?.wrapWithBraces() ?: "null"
                true
            }
            initPref(savedInstanceState, defVal = "Drawer")
        }

        configurePreference<SwitchPreferenceCompat>("hasCloseButton") {
            onChange { newValue ->
                hasCloseButton = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = true)
        }

        configurePreference<SwitchPreferenceCompat>("dismissOnBackPress") {
            onChange { newValue ->
                dismissOnBackPress = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = true)
        }

        configurePreference<SwitchPreferenceCompat>("dismissOnClickOutside") {
            onChange { newValue ->
                dismissOnClickOutside = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = true)
        }

        configurePreference<SwitchPreferenceCompat>("hasPrimaryButton") {
            onChange { newValue ->
                hasPrimaryButton = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = true)
        }

        configurePreference<SwitchPreferenceCompat>("isLTRAnimation") {
            onChange { newValue ->
                isLTRAnimation = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = true)
        }

        configurePreference<SeekBarPreference>("widthFraction") {
            onChange { newValue ->
                widthFraction = newValue / 10f
                summary = widthFraction.toString()
                true
            }
            initPref(savedInstanceState, defVal = 10)
            max = 10
            min = 5
        }

        configurePreference<EditTextPreference>("primaryButtonLabel") {
            onChange { newValue ->
                primaryButtonLabel = newValue
                summary = primaryButtonLabel.wrapWithBraces()
                true
            }
            initPref(savedInstanceState, defVal = "Primary")
        }

        configurePreference<SwitchPreferenceCompat>("primaryButtonDisabled") {
            onChange { newValue ->
                primaryButtonDisabled = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("primaryButtonLoading") {
            onChange { newValue ->
                primaryButtonLoading = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<EditTextPreference>("secondaryButtonLabel") {
            onChange { newValue ->
                secondaryButtonLabel = newValue
                summary = secondaryButtonLabel.wrapWithBraces()
                true
            }
            initPref(savedInstanceState, defVal = "Secondary")
        }

        configurePreference<SwitchPreferenceCompat>("secondaryButtonDisabled") {
            onChange { newValue ->
                secondaryButtonDisabled = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("secondaryButtonLoading") {
            onChange { newValue ->
                secondaryButtonLoading = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }
    }
}
