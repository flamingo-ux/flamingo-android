package com.flamingo.playground.components.menulist

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.Flamingo
import com.flamingo.components.menulist.MenuList
import com.flamingo.components.menulist.MenuListItem
import com.flamingo.components.menulist.MenuListItemParams
import com.flamingo.components.menulist.SelectedMenuListItem
import com.flamingo.components.menulist.TextButtonParams
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.parceNull
import com.flamingo.demoapi.wrapWithBraces
import com.flamingo.playground.R
import com.flamingo.playground.boast
import com.flamingo.theme.FlamingoIcon

@StatesPlayroomDemo
class MenuListStatesPlayroom : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_menulist, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = listOf(
            MenuListItem(params = MenuListItemParams(label = "first")),
            MenuListItem(
                params = MenuListItemParams(label = "second", icon = Flamingo.icons.Airplay),
                list = listOf(
                    MenuListItemParams("sub item 1"),
                    MenuListItemParams(
                        "long long long long long long long long long long long sub item 2",
                        disabled = true
                    ),
                    MenuListItemParams("sub item 3")
                )
            ),
            MenuListItem(params = MenuListItemParams(label = "third", icon = Flamingo.icons.Menu)),
            MenuListItem(
                params = MenuListItemParams(label = "fourth"), list = listOf(
                    MenuListItemParams("sub item 1"),
                    MenuListItemParams("long long long long long long long long long long long long long long long long long long long long long sub item 2"),
                    MenuListItemParams("sub item 3")
                )
            ),
            MenuListItem(params = MenuListItemParams(label = "long long long long long fifth item")),
        )

        var dialogTitle by mutableStateOf("title")
        var selectedItem by mutableStateOf(
            SelectedMenuListItem(
                MenuListItemParams(label = "first"),
                null
            )
        )
        var disabled by mutableStateOf(false)
        var hasTextButton by mutableStateOf(false)
        var textButtonLabel by mutableStateOf("label")
        var textButtonIcon: FlamingoIcon? by mutableStateOf(null)
        var textButtonIconAtStart by mutableStateOf(false)
        var textButtonDisabled by mutableStateOf(false)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            MenuList(
                dialogTitle = dialogTitle,
                items = items,
                selectedItem = selectedItem,
                disabled = disabled,
                textButtonParams = if (hasTextButton) TextButtonParams(
                    label = textButtonLabel,
                    icon = textButtonIcon,
                    iconAtStart = textButtonIconAtStart,
                    disabled = textButtonDisabled,
                    onClick = boast("buttonClick")
                ) else null,
                onItemSelected = {
                    selectedItem = it
                }
            )
        }

        configurePreference<EditTextPreference>("dialogTitle") {
            onChange { newValue ->
                dialogTitle = newValue
                summary = dialogTitle.wrapWithBraces()
                true
            }
            initPref(savedInstanceState, defVal = "title")
        }

        configurePreference<SwitchPreferenceCompat>("disabled") {
            onChange { newValue ->
                disabled = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<Preference>("manualSelection") {
            setOnPreferenceClickListener {
                selectedItem = SelectedMenuListItem(MenuListItemParams(label = "first"), null)
                true
            }
        }

        configurePreference<SwitchPreferenceCompat>("hasTextButton") {
            onChange { newValue ->
                hasTextButton = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<EditTextPreference>("textButtonLabel") {
            onChange { newValue ->
                textButtonLabel = newValue
                summary = textButtonLabel.wrapWithBraces()
                true
            }
            initPref(savedInstanceState, defVal = "Label")
        }

        configurePreference<DropDownPreference>("textButtonIcon") {
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
            onChange {
                textButtonIcon =
                    (it as? String)?.parceNull()?.let { Flamingo.icons.fromName(context, it) }
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        configurePreference<SwitchPreferenceCompat>("textButtonIconAtStart") {
            onChange { newValue ->
                textButtonIconAtStart = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("textButtonDisabled") {
            onChange { newValue ->
                textButtonDisabled = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }
    }
}