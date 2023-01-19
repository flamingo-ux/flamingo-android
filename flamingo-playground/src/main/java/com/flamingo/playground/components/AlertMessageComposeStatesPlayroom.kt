package com.flamingo.playground.components

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.components.Action
import com.flamingo.components.ActionGroup
import com.flamingo.components.alertmessage.AlertMessage
import com.flamingo.components.alertmessage.AlertMessageVariant
import com.flamingo.demoapi.*
import com.flamingo.loremIpsum
import com.flamingo.playground.R
import com.flamingo.playground.boast
import com.flamingo.playground.utils.Boast

@StatesPlayroomDemo
class AlertMessageComposeStatesPlayroom : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_alert_message_compose, rootKey)
    }

    private class ActionState(context: Context, onClickText: String) {
        var visible by mutableStateOf(false)
        var label by mutableStateOf("button")
        var loading by mutableStateOf(false)
        var disabled by mutableStateOf(false)

        val onClick = { Boast.showText(context, onClickText) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var text by mutableStateOf(loremIpsum(10))
        var variant by mutableStateOf(AlertMessageVariant.INFO)
        var hasOnCloseClick by mutableStateOf(false)
        var actions: ActionGroup? by mutableStateOf(null)

        val firstAction = ActionState(requireContext(), "first action click")
        val secondAction = ActionState(requireContext(), "second action click")

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            AlertMessage(
                text = text,
                variant = variant,
                onClose = if (hasOnCloseClick) boast("close onClick") else null,
                actions = actions
            )
        }

        configurePreference<EditTextPreference>("message") {
            onChange { newValue ->
                text = newValue
                summary = text.wrapWithBraces()
                true
            }
            initPref(savedInstanceState, defVal = loremIpsum(10))
        }

        configurePreference<SwitchPreferenceCompat>("hasOnCloseClick") {
            onChange { newValue ->
                hasOnCloseClick = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("variant") {
            val list = AlertMessageVariant.values().map { it.name }.toTypedArray()
            entries = list
            entryValues = list
            onChange { newValue ->
                variant = AlertMessageVariant.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = AlertMessageVariant.INFO)
        }

        configurePreference<SwitchPreferenceCompat>("firstAction") {
            onChange { newValue ->
                actions = if (newValue)
                    ActionGroup(
                        firstAction = Action(
                            firstAction.label,
                            firstAction.onClick,
                            firstAction.loading,
                            firstAction.disabled
                        ),
                        secondAction = if (secondAction.visible) Action(
                            secondAction.label,
                            secondAction.onClick,
                            secondAction.loading,
                            secondAction.disabled
                        ) else null
                    )
                else null

                firstAction.visible = newValue

                findPreference("firstActionLabel").isVisible = firstAction.visible
                findPreference("firstActionLoading").isVisible = firstAction.visible
                findPreference("firstActionDisabled").isVisible = firstAction.visible

                findPreference("secondAction").isVisible = firstAction.visible
                findPreference("secondActionLabel").isVisible =
                    firstAction.visible && secondAction.visible
                findPreference("secondActionLoading").isVisible =
                    firstAction.visible && secondAction.visible
                findPreference("secondActionDisabled").isVisible =
                    firstAction.visible && secondAction.visible
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<EditTextPreference>("firstActionLabel") {
            onChange { newValue ->
                firstAction.label = newValue
                summary = firstAction.label.wrapWithBraces()
                val oldActions = actions
                if (oldActions != null) {
                    actions =
                        oldActions.copy(firstAction = oldActions.firstAction.copy(label = firstAction.label))
                } else {
                    return@onChange false
                }
                true
            }
            initPref(savedInstanceState, defVal = "button")
        }

        configurePreference<SwitchPreferenceCompat>("firstActionLoading") {
            onChange { newValue ->
                firstAction.loading = newValue as? Boolean ?: return@onChange false
                val oldActions = actions
                if (oldActions != null) {
                    actions =
                        oldActions.copy(firstAction = oldActions.firstAction.copy(loading = firstAction.loading))
                } else {
                    return@onChange false
                }
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("firstActionDisabled") {
            onChange { newValue ->
                firstAction.disabled = newValue as? Boolean ?: return@onChange false
                val oldActions = actions
                if (oldActions != null) {
                    actions =
                        oldActions.copy(firstAction = oldActions.firstAction.copy(disabled = firstAction.disabled))
                } else {
                    return@onChange false
                }
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("secondAction") {
            onChange { newValue ->
                actions = if (newValue)
                    actions?.copy(
                        secondAction = Action(
                            secondAction.label,
                            secondAction.onClick,
                            secondAction.loading,
                            secondAction.disabled
                        )
                    )
                else
                    actions?.copy(secondAction = null)

                secondAction.visible = newValue

                findPreference("secondActionLabel").isVisible =
                    firstAction.visible && secondAction.visible
                findPreference("secondActionLoading").isVisible =
                    firstAction.visible && secondAction.visible
                findPreference("secondActionDisabled").isVisible =
                    firstAction.visible && secondAction.visible
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<EditTextPreference>("secondActionLabel") {
            onChange { newValue ->
                secondAction.label = newValue
                summary = secondAction.label.wrapWithBraces()
                val oldActions = actions
                if (oldActions != null) {
                    actions =
                        oldActions.copy(secondAction = oldActions.secondAction?.copy(label = secondAction.label))
                } else {
                    return@onChange false
                }
                true
            }
            initPref(savedInstanceState, defVal = "button")
        }

        configurePreference<SwitchPreferenceCompat>("secondActionLoading") {
            onChange { newValue ->
                secondAction.loading = newValue as? Boolean ?: return@onChange false
                val oldActions = actions
                if (oldActions != null) {
                    actions =
                        oldActions.copy(secondAction = oldActions.secondAction?.copy(loading = secondAction.loading))
                } else {
                    return@onChange false
                }
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("secondActionDisabled") {
            onChange { newValue ->
                secondAction.disabled = newValue as? Boolean ?: return@onChange false
                val oldActions = actions
                if (oldActions != null) {
                    actions =
                        oldActions.copy(secondAction = oldActions.secondAction?.copy(disabled = secondAction.disabled))
                } else {
                    return@onChange false
                }
                true
            }
            initPref(savedInstanceState, defVal = false)
        }
    }
}
