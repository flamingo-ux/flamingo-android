package com.flamingo.playground.components.topappbar

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.Flamingo
import com.flamingo.components.AvatarBackground
import com.flamingo.components.AvatarContent
import com.flamingo.components.AvatarIndicator
import com.flamingo.components.AvatarShape
import com.flamingo.components.IconButtonIndicator
import com.flamingo.components.IndicatorColor
import com.flamingo.components.topappbar.ActionItem
import com.flamingo.components.topappbar.CenterItem
import com.flamingo.components.topappbar.EdgeItem
import com.flamingo.components.topappbar.TopAppBar
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.WhiteModeDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.parceNull
import com.flamingo.demoapi.wrapWithBraces
import com.flamingo.playground.R
import com.flamingo.playground.boast
import kotlin.reflect.KClass

@StatesPlayroomDemo
class TopAppBarStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_top_app_bar, rootKey)
    }

    @Suppress("LongMethod", "ComplexMethod", "ComplexCondition")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var start by mutableStateOf<EdgeItem?>(null)
        var centerItemType by mutableStateOf<KClass<out CenterItem>?>(null)
        var searchHasOnClick by mutableStateOf(false)
        var avatarInCenter by mutableStateOf(false)
        var title by mutableStateOf<String?>(null)
        var subtitle by mutableStateOf<String?>(null)
        var action1 by mutableStateOf<ActionItem?>(null)
        var action2 by mutableStateOf<ActionItem?>(null)
        var end by mutableStateOf<EdgeItem?>(null)
        var showBackground by mutableStateOf(false)
        var shadow by mutableStateOf(false)
        var white by mutableStateOf(false)
        var debugMode by mutableStateOf(false)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            WhiteModeDemo(white = white) {
                CompositionLocalProvider(Flamingo.LocalDebugMode provides debugMode) {
                    TopAppBar(
                        start = start,
                        center = when (centerItemType) {
                            CenterItem.Center::class -> CenterItem.Center(
                                title = title,
                                subtitle = subtitle,
                                avatar = if (avatarInCenter) {
                                    CenterItem.Avatar(
                                        content = AvatarContent.Image(
                                            painterResource(
                                                com.flamingo.demoapi.R.drawable.example_dog
                                            )
                                        ),
                                        shape = AvatarShape.ROUNDED_CORNERS_SMALL,
                                        indicator = AvatarIndicator(IndicatorColor.ERROR)
                                    )
                                } else null
                            )
                            CenterItem.Search::class -> {
                                var value by remember { mutableStateOf("") }
                                CenterItem.Search(
                                    context = requireContext(),
                                    value = value,
                                    onValueChange = { value = it },
                                    onClick = if (searchHasOnClick) {
                                        boast("Search click")
                                    } else null
                                )
                            }
                            null -> null
                            else -> error("Unknown CenterItem.Search subclass: $centerItemType")
                        },
                        action1 = action1,
                        action2 = action2,
                        end = end,
                        showShadow = shadow,
                        showBackground = showBackground,
                    )
                }
            }
        }

        val searchHasOnClickPref = findPreference("searchHasOnClick")
        val avatarInCenterPref = findPreference("avatarInCenter")
        val titlePref = findPreference("title")
        val subtitlePref = findPreference("subtitle")

        configurePreference<DropDownPreference>("start") {
            entries = arrayOf("Icon Button", "Button", "Avatar", "null")
            entryValues = entries
            onChange { newValue ->
                start = when (newValue) {
                    "Icon Button" -> EdgeItem.IconButton(Flamingo.icons.ArrowLeft) {}
                    "Button" -> EdgeItem.Button("Button") {}
                    "Avatar" -> EdgeItem.Avatar(
                        content = AvatarContent.Letters('A', 'B', AvatarBackground.RED),
                    )
                    "null" -> null
                    else -> error("Unreachable")
                }
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        configurePreference<DropDownPreference>("center") {
            entries = arrayOf("Center", "Search", "null")
            entryValues = entries
            onChange { newValue ->
                avatarInCenterPref.isVisible = false
                titlePref.isVisible = false
                subtitlePref.isVisible = false
                searchHasOnClickPref.isVisible = false

                centerItemType = when (newValue) {
                    "Center" -> {
                        avatarInCenterPref.isVisible = true
                        titlePref.isVisible = true
                        subtitlePref.isVisible = true
                        searchHasOnClickPref.isVisible = false

                        CenterItem.Center::class
                    }
                    "Search" -> {
                        searchHasOnClickPref.isVisible = true
                        CenterItem.Search::class
                    }
                    "null" -> null
                    else -> error("Unreachable")
                }
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        configurePreference<SwitchPreferenceCompat>("searchHasOnClick") {
            onChange { newValue ->
                searchHasOnClick = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("avatarInCenter") {
            onChange { newValue ->
                avatarInCenter = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<EditTextPreference>("title") {
            onChange { newValue ->
                title = (newValue as? String)?.parceNull()
                summary = title?.wrapWithBraces() ?: "null"
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        configurePreference<EditTextPreference>("subtitle") {
            onChange { newValue ->
                subtitle = (newValue as? String)?.parceNull()
                summary = subtitle?.wrapWithBraces() ?: "null"
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        configurePreference<SwitchPreferenceCompat>("action1") {
            onChange { newValue ->
                val newValue = newValue as? Boolean ?: return@onChange false
                val indicator = IconButtonIndicator(IndicatorColor.PRIMARY)
                action1 = if (newValue) ActionItem(Flamingo.icons.Coffee, indicator) {} else null
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("action2") {
            onChange { newValue ->
                val newValue = newValue as? Boolean ?: return@onChange false
                action2 = if (newValue) ActionItem(Flamingo.icons.Feather) {} else null
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("end") {
            entries = arrayOf("Icon Button", "Avatar", "Button", "null")
            entryValues = entries
            onChange { newValue ->
                end = when (newValue) {
                    "Icon Button" -> EdgeItem.IconButton(Flamingo.icons.ArrowLeft) {}
                    "Button" -> EdgeItem.Button("Button") {}
                    "Avatar" -> EdgeItem.Avatar(
                        content = AvatarContent.Letters('A', 'B', AvatarBackground.RED),
                    )
                    "null" -> null
                    else -> error("Unreachable")
                }
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        configurePreference<SwitchPreferenceCompat>("showBackground") {
            onChange { newValue ->
                showBackground = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = true)
        }

        configurePreference<SwitchPreferenceCompat>("shadow") {
            onChange { newValue ->
                shadow = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("white") {
            onChange { white = it; true }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("debugMode") {
            onChange { newValue ->
                debugMode = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }
    }
}
