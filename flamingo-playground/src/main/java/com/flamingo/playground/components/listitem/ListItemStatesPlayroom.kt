@file:Suppress(
    "ModifierOrdering",
    "CommentSpacing",
    "NoBlankLineBeforeRbrace",
    "ModifierOrdering",
    "NoConsecutiveBlankLines",
    "MagicNumber",
    "FunctionName",
    "MatchingDeclarationName",
    "LongParameterList",
    "LongMethod",
    "ComplexMethod",
    "SpacingAroundParens"
)

package com.flamingo.playground.components.listitem

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.flamingo.Flamingo
import com.flamingo.components.Action
import com.flamingo.components.ActionGroup
import com.flamingo.components.Avatar
import com.flamingo.components.AvatarBackground
import com.flamingo.components.AvatarContent
import com.flamingo.components.AvatarSize
import com.flamingo.components.Icon
import com.flamingo.components.IconButton
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonSize
import com.flamingo.components.listitem.ListItem
import com.flamingo.components.listitem.SideSlotsAlignment
import com.flamingo.playground.R
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.playground.boast
import com.flamingo.demoapi.WhiteModeDemo
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.onChange
import com.flamingo.playground.preview.CheckboxPreview
import com.flamingo.playground.preview.RadioButtonPreview
import com.flamingo.playground.preview.SwitchPreview

@StatesPlayroomDemo
class ListItemStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_listitem, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var startSlot by mutableStateOf(Content.NO)
        var title by mutableStateOf(Text.ONE_LINE)
        var subtitle by mutableStateOf(Text.NO)
        var description by mutableStateOf(Text.NO)
        var date by mutableStateOf(Text.NO)
        var endSlot by mutableStateOf(Content.NO)
        var isEndSlotClickableWhenDisabled by mutableStateOf(false)
        var titleMaxLines by mutableStateOf(Int.MAX_VALUE)
        var sideSlotsAlignment by mutableStateOf(SideSlotsAlignment.CENTER)
        var actions by mutableStateOf(Actions.NO)
        var onClick by mutableStateOf(false)
        var showDivider by mutableStateOf(true)
        var invertTitleAndSubtitle by mutableStateOf(false)
        var disabled by mutableStateOf(false)
        var white by mutableStateOf(false)

        findPreference<DemoPreference>("component")?.setComposeDesignComponent {
            WhiteModeDemo(white = white) {
                ListItemPreview(
                    start = startSlot,
                    title = title,
                    subtitle = subtitle,
                    description = description,
                    date = date,
                    end = endSlot,
                    isEndSlotClickableWhenDisabled = isEndSlotClickableWhenDisabled,
                    titleMaxLines = titleMaxLines,
                    sideSlotsAlignment = sideSlotsAlignment,
                    actions = actions,
                    onClick = onClick,
                    showDivider = showDivider,
                    invertTitleAndSubtitle = invertTitleAndSubtitle,
                    disabled = disabled
                )
            }
        }

        configurePreference<DropDownPreference>("startSlot") {
            val contents = Content.values().map { it.name }.toTypedArray()
            entries = contents
            entryValues = contents
            onChange { newValue ->
                startSlot = Content.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = Content.NO.toString())
        }

        configurePreference<DropDownPreference>("title") {
            val contents = Text.values().map { it.name }.toTypedArray()
            entries = contents
            entryValues = contents
            onChange { newValue ->
                title = Text.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = Text.ONE_LINE.toString())
        }

        configurePreference<DropDownPreference>("subtitle") {
            val texts = Text.values().map { it.name }.toTypedArray()
            entries = texts
            entryValues = texts
            onChange { newValue ->
                subtitle = Text.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = Text.NO.toString())
        }

        configurePreference<DropDownPreference>("description") {
            val texts = Text.values().map { it.name }.toTypedArray()
            entries = texts
            entryValues = texts
            onChange { newValue ->
                description = Text.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = Text.NO.toString())
        }

        configurePreference<DropDownPreference>("date") {
            val texts = Text.values().map { it.name }.toTypedArray()
            entries = texts
            entryValues = texts
            onChange { newValue ->
                date = Text.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = Text.NO.toString())
        }

        configurePreference<DropDownPreference>("endSlot") {
            val contents = Content.values().map { it.name }.toTypedArray()
            entries = contents
            entryValues = contents
            onChange { newValue ->
                endSlot = Content.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = Content.NO.toString())
        }

        configurePreference<SwitchPreferenceCompat>("isEndSlotClickableWhenDisabled") {
            onChange { newValue ->
                isEndSlotClickableWhenDisabled = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("titleMaxLines") {
            entries = arrayOf("1", "2", "Int.MAX_VALUE")
            entryValues = arrayOf(1, 2, Int.MAX_VALUE).map(Int::toString).toTypedArray()
            onChange { newValue ->
                titleMaxLines = newValue.toInt()
                true
            }
            initPref(savedInstanceState, defVal = Int.MAX_VALUE)
        }

        configurePreference<DropDownPreference>("sideSlotsAlignment") {
            val alignments = SideSlotsAlignment.values().map { it.name }.toTypedArray()
            entries = alignments
            entryValues = alignments
            onChange { newValue ->
                sideSlotsAlignment = SideSlotsAlignment.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = SideSlotsAlignment.CENTER.toString())
        }

        configurePreference<DropDownPreference>("actions") {
            val actionTypes = Actions.values().map { it.name }.toTypedArray()
            entries = actionTypes
            entryValues = actionTypes
            onChange { newValue ->
                actions = Actions.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = Actions.NO.toString())
        }

        configurePreference<SwitchPreferenceCompat>("onClick") {
            onChange { newValue ->
                onClick = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("showDivider") {
            onChange { newValue ->
                showDivider = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = true)
        }

        configurePreference<SwitchPreferenceCompat>("invertTitleAndSubtitle") {
            onChange { newValue ->
                invertTitleAndSubtitle = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("disabled") {
            onChange { newValue ->
                disabled = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<SwitchPreferenceCompat>("white") {
            onChange { white = it; true }
            initPref(savedInstanceState, defVal = false)
        }
    }

    private enum class Content {
        NO, AVATAR, ICON_BUTTON, SWITCH, RADIO_BUTTON, CHECKBOX, ICON, BUTTON,
    }

    private enum class Text { NO, ONE_LINE, MULTILINE }
    private enum class Actions { NO, ONE, TWO }

    @Composable
    private fun ListItemPreview(
        start: Content,
        title: Text,
        subtitle: Text,
        description: Text,
        date: Text,
        end: Content,
        isEndSlotClickableWhenDisabled: Boolean,
        titleMaxLines: Int,
        sideSlotsAlignment: SideSlotsAlignment,
        actions: Actions,
        onClick: Boolean,
        showDivider: Boolean,
        invertTitleAndSubtitle: Boolean,
        disabled: Boolean,
    ) {
        ListItem(
            start = previewListItemContent(start),
            title = when (title) {
                Text.NO -> null
                Text.ONE_LINE -> "ListItem " + "Title "
                Text.MULTILINE -> "ListItem " + "Title ".repeat(30)
            },
            subtitle = when (subtitle) {
                Text.NO -> null
                Text.ONE_LINE -> "ListItem " + "Subtitle "
                Text.MULTILINE -> "ListItem " + "Subtitle ".repeat(7)
            },
            description = when (description) {
                Text.NO -> null
                Text.ONE_LINE -> "ListItem " + "Description "
                Text.MULTILINE -> "ListItem " + "Description ".repeat(7)
            },
            date = when (date) {
                Text.NO -> null
                Text.ONE_LINE -> "ListItem " + "Date "
                Text.MULTILINE -> "ListItem " + "Date ".repeat(7)
            },
            end = previewListItemContent(end),
            isEndSlotClickableWhenDisabled = isEndSlotClickableWhenDisabled,
            titleMaxLines = titleMaxLines,
            sideSlotsAlignment = sideSlotsAlignment,
            actions = when (actions) {
                Actions.NO -> null
                Actions.ONE -> ActionGroup(
                    Action("Button 1", onClick = boast("Button 1"))
                )
                Actions.TWO -> ActionGroup(
                    firstAction = Action("Button 1", onClick = boast("Button 1")),
                    secondAction = Action("Button 2", onClick = boast("Button 2"))
                )
            },
            disabled = disabled,
            onClick = if (onClick) boast("ListItem") else null,
            invertTitleAndSubtitle = invertTitleAndSubtitle,
            showDivider = showDivider,
        )
    }

    private fun previewListItemContent(start: Content): (@Composable () -> Unit)? {
        return if (start == Content.NO) null else {
            {
                when (start) {
                    Content.NO -> Unit
                    Content.AVATAR -> Avatar(
                        content = AvatarContent.Letters('A', 'A', AvatarBackground.GREEN),
                        size = AvatarSize.SIZE_40,
                        contentDescription = null
                    )
                    Content.ICON_BUTTON -> IconButton(
                        onClick = boast("IconButton"),
                        icon = Flamingo.icons.Watch,
                        contentDescription = null
                    )
                    Content.SWITCH -> SwitchPreview()
                    Content.RADIO_BUTTON -> RadioButtonPreview()
                    Content.CHECKBOX -> CheckboxPreview()
                    Content.ICON -> Icon(
                        modifier = Modifier.requiredSize(24.dp),
                        icon = Flamingo.icons.Watch,
                        tint = if (Flamingo.isWhiteMode) Flamingo.palette.white
                        else Flamingo.colors.textSecondary,
                        contentDescription = null
                    )
                    Content.BUTTON -> {
                        Button(
                            onClick = boast("Button click"),
                            label = "Button",
                            size = ButtonSize.MEDIUM,
                        )
                    }
                }
            }
        }
    }
}
