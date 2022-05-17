package com.flamingo.playground.components

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.Flamingo
import com.flamingo.components.TextField
import com.flamingo.components.TextFieldIcon
import com.flamingo.components.TextFieldIconAlignment
import com.flamingo.components.TextFieldSize
import com.flamingo.playground.R
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.playground.boast
import com.flamingo.playground.utils.Boast
import com.flamingo.demoapi.parceNull
import com.flamingo.demoapi.wrapWithBraces
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.WhiteModeDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange

@StatesPlayroomDemo
class TextFieldStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_text_field, rootKey)
    }

    private class TextFieldState {
        var value: String by mutableStateOf("")
        var label: String? by mutableStateOf(null)
        var placeholder: String? by mutableStateOf(null)
        var required: Boolean by mutableStateOf(false)
        var size: TextFieldSize by mutableStateOf(TextFieldSize.MEDIUM)
        var error: Boolean by mutableStateOf(false)
        var errorText: String? by mutableStateOf(null)
        var helperText: String? by mutableStateOf(null)
        var maxCharNumber: Int? by mutableStateOf(null)
        var loading: Boolean by mutableStateOf(false)
        var disabled: Boolean by mutableStateOf(false)
        var multiline: Boolean by mutableStateOf(false)
        var maxVisibleLines: Int by mutableStateOf(@Suppress("MagicNumber") 4)
        var iconAreaAlignment: TextFieldIconAlignment by mutableStateOf(TextFieldIconAlignment.TOP)
        var icon: TextFieldIcon? by mutableStateOf(null)
        var hasOnClick: Boolean by mutableStateOf(false)
        var white: Boolean by mutableStateOf(false)
    }

    @Suppress("LongMethod", "ComplexMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val state = TextFieldState()

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            with(state) {
                WhiteModeDemo(white = white) {
                    TextField(
                        value = value,
                        onValueChange = { value = it },
                        label = label,
                        placeholder = placeholder,
                        required = required,
                        size = size,
                        error = error,
                        errorText = errorText,
                        helperText = helperText,
                        maxCharNumber = maxCharNumber,
                        loading = loading,
                        disabled = disabled,
                        multiline = multiline,
                        maxVisibleLines = maxVisibleLines,
                        iconAreaAlignment = iconAreaAlignment,
                        icon = icon,
                        onClick =
                        if (hasOnClick) boast("TextField onClick") else null,
                    )
                }
            }
        }

        configurePreference<EditTextPreference>("label") {
            onChange { newValue ->
                state.label = (newValue as? String)?.parceNull()
                summary = state.label?.wrapWithBraces() ?: "null"
                true
            }
            initPref(savedInstanceState, defVal = "Label")
        }

        configurePreference<EditTextPreference>("placeholder") {
            onChange { newValue ->
                state.placeholder = (newValue as? String)?.parceNull()
                summary = state.placeholder?.wrapWithBraces() ?: "null"
                true
            }
            initPref(savedInstanceState, defVal = "Placeholder")
        }

        configurePreference<SwitchPreferenceCompat>("required") {
            onChange { newValue ->
                state.required = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = true)
        }

        configurePreference<DropDownPreference>("size") {
            val sizes = TextFieldSize.values().map { it.name }.toTypedArray()
            entries = sizes
            entryValues = sizes
            onChange { newValue ->
                state.size = TextFieldSize.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = TextFieldSize.MEDIUM)
        }

        configurePreference<SwitchPreferenceCompat>("error") {
            onChange { newValue ->
                state.error = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<EditTextPreference>("errorText") {
            onChange { newValue ->
                state.errorText = (newValue as? String)?.parceNull()
                summary = state.errorText?.wrapWithBraces() ?: "null"
                true
            }
            initPref(savedInstanceState, defVal = "Error text")
        }

        configurePreference<EditTextPreference>("helperText") {
            onChange { newValue ->
                state.helperText = (newValue as? String)?.parceNull()
                summary = state.helperText?.wrapWithBraces() ?: "null"
                true
            }
            initPref(savedInstanceState, defVal = "Helper text")
        }

        configurePreference<EditTextPreference>("maxCharNumber") {
            onChange { newValue ->
                state.maxCharNumber = (newValue as? String)?.toIntOrNull()
                summary = state.maxCharNumber.toString()
                true
            }
            initPref(savedInstanceState, defVal = "12")
        }

        configurePreference<SwitchPreferenceCompat>("loading") {
            onChange { newValue ->
                state.loading = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("disabled") {
            onChange { newValue ->
                state.disabled = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("multiline") {
            onChange { newValue ->
                state.multiline = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<EditTextPreference>("maxVisibleLines") {
            onChange { newValue ->
                state.maxVisibleLines =
                    (newValue as? String)?.toIntOrNull() ?: return@onChange false
                summary = state.maxVisibleLines.toString()
                true
            }
            initPref(savedInstanceState, defVal = "4")
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
            onChange { newValue ->
                val icon = state.icon
                val dsIcon = (newValue as? String)?.parceNull()?.let {
                    Flamingo.icons.fromName(context, it)
                }
                findPreference("iconHasOnClick").isVisible = dsIcon != null
                if (dsIcon == null) {
                    state.icon = null
                    return@onChange true
                }
                state.icon =
                    icon?.copy(icon = dsIcon) ?: TextFieldIcon(dsIcon, contentDescription = null)

                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        val onIconClick = { Boast.showText(context, "Click") }

        configurePreference<SwitchPreferenceCompat>("iconHasOnClick") {
            onChange { newValue ->
                val newValue = newValue as? Boolean ?: return@onChange false
                val icon = state.icon ?: return@onChange false
                state.icon = icon.copy(onClick = if (newValue) onIconClick else null)
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("iconAreaAlignment") {
            val sizes = TextFieldIconAlignment.values().map { it.name }.toTypedArray()
            entries = sizes
            entryValues = sizes
            onChange { newValue ->
                state.iconAreaAlignment = TextFieldIconAlignment.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = TextFieldIconAlignment.TOP)
        }

        configurePreference<SwitchPreferenceCompat>("white") {
            onChange { state.white = it; true }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("hasOnClick") {
            onChange { newValue ->
                state.hasOnClick = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }
    }
}
