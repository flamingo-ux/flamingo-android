package com.flamingo.playground.components.dropdown

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import com.flamingo.Flamingo
import com.flamingo.components.button.ButtonSize
import com.flamingo.components.dropdown.BaseDropdownComponent
import com.flamingo.components.dropdown.Dropdown
import com.flamingo.components.dropdown.DropdownItem
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.playground.R
import com.flamingo.playground.components.tabrow.TabsWithDropdown
import com.flamingo.playground.utils.Boast

@StatesPlayroomDemo
class DropdownStatesPlayroom : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_dropdown, rootKey)
    }

    @Suppress("LongMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var baseDropdownComponent: BaseDropdownComponent by mutableStateOf(BaseDropdownComponent.Button("button"))
        val items = listOf(
            DropdownItem("item 1", Flamingo.icons.Bell),
            DropdownItem("long long long long long long long long item", Flamingo.icons.Aperture),
            DropdownItem("item 3"),
            DropdownItem("item 4 long long long long long long long long"),
            DropdownItem("item 5", Flamingo.icons.Bell),
            DropdownItem("item 6", disabled = true),
            DropdownItem("item 7", Flamingo.icons.Share),
            DropdownItem("item 8", Flamingo.icons.Bell),
            DropdownItem("item 9", Flamingo.icons.Bell),
            DropdownItem("item 10", Flamingo.icons.Bell, true),
            DropdownItem("item 11", Flamingo.icons.Bell),
            DropdownItem("item 12", Flamingo.icons.Bell),
            DropdownItem("item 13", Flamingo.icons.Bell),
        )
        var baseComponentDemo: BaseComponentDemo by mutableStateOf(BaseComponentDemo.Button)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            val context = LocalContext.current
            if (baseComponentDemo == BaseComponentDemo.TabRow) {
                TabsWithDropdown()
            } else {
                Dropdown(
                    baseDropdownComponent = baseDropdownComponent,
                    items = items,
                    onDropdownItemSelected = {
                        Boast.makeText(context, "clicked on ${it.label}")
                    }
                )
            }
        }

        configurePreference<DropDownPreference>("baseComponent") {
            entries = BaseComponentDemo.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { newValue ->
                val demo = BaseComponentDemo.valueOf(newValue)
                baseComponentDemo = demo
                baseDropdownComponent = when (demo) {
                    BaseComponentDemo.Button -> BaseDropdownComponent.Button("button")
                    BaseComponentDemo.ButtonWithIcon -> BaseDropdownComponent.Button(
                        "button 2",
                        Flamingo.icons.Bell,
                        ButtonSize.SMALL
                    )
                    BaseComponentDemo.Chip -> BaseDropdownComponent.Chip("chip")
                    BaseComponentDemo.ChipWithIcon -> BaseDropdownComponent.Chip(
                        "chip",
                        Flamingo.icons.Share
                    )
                    BaseComponentDemo.IconButton -> BaseDropdownComponent.IconButton(Flamingo.icons.Edit)
                    BaseComponentDemo.TabRow -> BaseDropdownComponent.Button("null") // placeholder
                }
                true
            }
            initPref(savedInstanceState, defVal = BaseComponentDemo.Button.name)
        }
    }

    private enum class BaseComponentDemo {
        Button,
        ButtonWithIcon,
        Chip,
        ChipWithIcon,
        IconButton,
        TabRow
    }
}