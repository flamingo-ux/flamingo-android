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
import com.flamingo.components.*
import com.flamingo.demoapi.*
import com.flamingo.playground.R
import com.flamingo.playground.boast
import com.flamingo.playground.utils.Boast
import com.flamingo.playground.utils.exhaustive

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
        var edgeItem: EdgeItem? by mutableStateOf(null)
        var edgeItemHasOnClick: Boolean by mutableStateOf(false)
        var edgeItemPlacement: IconPlacement by mutableStateOf(IconPlacement.END)
        var buttonText: String by mutableStateOf("")
        var hasOnClick: Boolean by mutableStateOf(false)
        var bottomPadding: TextFieldBottomPadding by mutableStateOf(TextFieldBottomPadding.MEDIUM)
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
                        edgeItemAreaAlignment = iconAreaAlignment,
                        edgeItem = edgeItem,
                        onClick = if (hasOnClick) boast("TextField onClick") else null,
                        bottomPadding = bottomPadding
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

        val onIconClick = { Boast.showText(context, "Click") }

        configurePreference<DropDownPreference>("edgeItem") {
            entries = arrayOf(
                "null",
                "Icon",
                "Avatar",
                "Button",
            )
            entryValues = arrayOf(
                "null",
                "Icon",
                "Avatar",
                "Button",
            )
            onChange { newValue ->
                var newEdgeItem: EdgeItem? = null
                (newValue as? String)?.parceNull()?.let {
                    newEdgeItem = when (it) {
                        "Icon" -> EdgeItem.TextFieldIcon(
                            Flamingo.icons.Bell,
                            if (state.edgeItemHasOnClick) onIconClick else null,
                            state.edgeItemPlacement,
                            null
                        )
                        "Avatar" -> EdgeItem.TextFieldAvatar(
                            AvatarContent.Letters('A', 'A', AvatarBackground.PRIMARY),
                            if (state.edgeItemHasOnClick) onIconClick else null,
                            AvatarShape.CIRCLE,
                            state.edgeItemPlacement
                        )
                        "Button" -> EdgeItem.TextFieldButton(
                            state.buttonText,
                            onIconClick,
                            state.loading,
                            state.disabled
                        )
                        else -> null
                    }
                }
                findPreference("edgeItemHasOnClick").isVisible =
                    newEdgeItem != null && newEdgeItem !is EdgeItem.TextFieldButton
                findPreference("edgeItemPlacement").isVisible =
                    newEdgeItem != null && newEdgeItem !is EdgeItem.TextFieldButton
                findPreference("buttonText").isVisible =
                    newEdgeItem != null && newEdgeItem is EdgeItem.TextFieldButton
                if (newEdgeItem == null) {
                    state.edgeItem = null
                    return@onChange true
                }
                state.edgeItem = newEdgeItem

                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        configurePreference<SwitchPreferenceCompat>("edgeItemHasOnClick") {
            onChange { newValue ->
                val click = newValue as? Boolean ?: return@onChange false
                val edgeItem = state.edgeItem ?: return@onChange false
                when (edgeItem) {
                    is EdgeItem.TextFieldIcon -> state.edgeItem =
                        edgeItem.copy(onClick = if (click) onIconClick else null)
                    is EdgeItem.TextFieldAvatar -> state.edgeItem =
                        edgeItem.copy(onClick = if (click) onIconClick else null)
                    else -> return@onChange false
                }.exhaustive

                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("edgeItemPlacement") {
            val values = IconPlacement.values().map { it.name }.toTypedArray()
            entries = values
            entryValues = values
            onChange { newValue ->
                val edgeItem = state.edgeItem ?: return@onChange false
                val placement = IconPlacement.valueOf(newValue)
                when (edgeItem) {
                    is EdgeItem.TextFieldIcon -> state.edgeItem =
                        edgeItem.copy(placement = placement)
                    is EdgeItem.TextFieldAvatar -> state.edgeItem =
                        edgeItem.copy(placement = placement)
                    else -> return@onChange false
                }.exhaustive
                true
            }
            initPref(savedInstanceState, defVal = IconPlacement.END)
        }

        configurePreference<EditTextPreference>("buttonText") {
            onChange { newValue ->
                state.buttonText = newValue
                summary = state.buttonText.wrapWithBraces()
                state.edgeItem =
                    (state.edgeItem as? EdgeItem.TextFieldButton)?.copy(label = state.buttonText)
                true
            }
            initPref(savedInstanceState, defVal = "")
        }

        configurePreference<DropDownPreference>("iconAreaAlignment") {
            val values = TextFieldIconAlignment.values().map { it.name }.toTypedArray()
            entries = values
            entryValues = values
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



        configurePreference<DropDownPreference>("bottomPadding") {
            val values = TextFieldBottomPadding.values().map { it.name }.toTypedArray()
            entries = values
            entryValues = values
            onChange { newValue ->
                state.bottomPadding = TextFieldBottomPadding.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = TextFieldBottomPadding.MEDIUM)
        }
    }
}
