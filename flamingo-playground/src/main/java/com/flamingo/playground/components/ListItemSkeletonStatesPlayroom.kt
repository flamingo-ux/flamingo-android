package com.flamingo.playground.components

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.components.AvatarShape
import com.flamingo.components.AvatarSize
import com.flamingo.components.listitem.AvatarSkeleton
import com.flamingo.components.listitem.ListItemSkeleton
import com.flamingo.components.listitem.TextBlocksSkeleton
import com.flamingo.playground.R
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.WhiteModeDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.onChange

@StatesPlayroomDemo
class ListItemSkeletonStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_listitem_skeleton, rootKey)
    }

    @Suppress("LongMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var textBlocks by mutableStateOf(TextBlocksSkeleton.ONE)
        var avatarSkeleton by mutableStateOf(false)
        var avatarSize by mutableStateOf(AvatarSize.SIZE_40)
        var avatarShape by mutableStateOf(AvatarShape.CIRCLE)
        var white by mutableStateOf(false)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            WhiteModeDemo(white = white) {
                ListItemSkeleton(
                    avatar = if (avatarSkeleton) AvatarSkeleton(avatarSize, avatarShape) else null,
                    textBlocks = textBlocks,
                )
            }
        }

        val avatarSizePref = findPreference("avatarSize")
        val avatarShapePref = findPreference("avatarShape")

        configurePreference<DropDownPreference>("textBlocks") {
            entries = TextBlocksSkeleton.values().map { it.toString() }.toTypedArray()
            entryValues = entries
            onChange { newValue ->
                textBlocks = TextBlocksSkeleton.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = TextBlocksSkeleton.ONE)
        }

        configurePreference<SwitchPreferenceCompat>("avatarSkeleton") {
            onChange { newValue ->
                avatarSkeleton = newValue as? Boolean ?: return@onChange false
                avatarSizePref.isVisible = avatarSkeleton
                avatarShapePref.isVisible = avatarSkeleton
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("avatarSize") {
            entries = AvatarSize.values().map { it.toString() }.toTypedArray()
            entryValues = entries
            onChange { newValue ->
                avatarSize = AvatarSize.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = AvatarSize.SIZE_40)
        }

        configurePreference<DropDownPreference>("avatarShape") {
            entries = AvatarShape.values().map { it.toString() }.toTypedArray()
            entryValues = entries
            onChange { newValue ->
                avatarShape = AvatarShape.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = AvatarShape.CIRCLE)
        }

        configurePreference<SwitchPreferenceCompat>("white") {
            onChange { white = it; true }
            initPref(savedInstanceState, defVal = false)
        }
    }
}
