package com.flamingo.playground.components

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.Flamingo
import com.flamingo.components.*
import com.flamingo.demoapi.*
import com.flamingo.playground.R
import com.flamingo.playground.boast

@StatesPlayroomDemo
class AvatarComposeStatesPlayroom : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_avatar_compose, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var content: AvatarContent by mutableStateOf(AvatarContent.Letters('A', 'A', AvatarBackground.PRIMARY))
        var hasOnClick by mutableStateOf(false)
        var size by mutableStateOf(AvatarSize.SIZE_40)
        var shape by mutableStateOf(AvatarShape.CIRCLE)
        var indicator: AvatarIndicator? by mutableStateOf(null)
        var disabled by mutableStateOf(false)

        // todo figure out a way to initiate Painter on the spot, not inside setComposeDesignComponent
        var painter: Painter? = null

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            painter = painterResource(com.flamingo.demoapi.R.drawable.example_human)
            Avatar(
                content = content,
                contentDescription = null,
                onClick = if (hasOnClick) boast("Avatar onClick") else null,
                size = size,
                shape = shape,
                indicator = indicator,
                disabled = disabled
            )
        }

        configurePreference<SwitchPreferenceCompat>("disabled") {
            onChange { newValue ->
                disabled = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("hasOnClick") {
            onChange { newValue ->
                hasOnClick = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("content") {
            val list = arrayOf(
                "Image",
                "Icon",
                "Letters",
            )
            entries = list
            entryValues = list
            onChange { newValue ->
                var newContent: AvatarContent? = null
                (newValue as? String)?.parceNull()?.let {
                    newContent = when (it) {
                        "Image" -> AvatarContent.Image(painter ?: return@onChange false)
                        "Icon" -> AvatarContent.Icon(Flamingo.icons.Bell, AvatarBackground.PRIMARY)
                        "Letters" -> AvatarContent.Letters('A', 'A', AvatarBackground.PRIMARY)
                        else -> null
                    }
                }

                //todo change visibilities of corresponding preferences depending on content

                if (newContent == null) {
                    return@onChange false
                }

                content = newContent ?: return@onChange false

                true
            }
            initPref(savedInstanceState, defVal = false)
        }
    }
}