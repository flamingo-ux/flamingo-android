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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
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
import com.flamingo.components.listitem.TextWrapper
import com.flamingo.demoapi.DemoPreference
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.demoapi.WhiteModeDemo
import com.flamingo.demoapi.configurePreference
import com.flamingo.demoapi.findPreference
import com.flamingo.demoapi.initPref
import com.flamingo.demoapi.onChange
import com.flamingo.demoapi.parceNull
import com.flamingo.playground.R
import com.flamingo.playground.boast
import com.flamingo.playground.preview.CheckboxPreview
import com.flamingo.playground.preview.RadioButtonPreview
import com.flamingo.playground.preview.SwitchPreview
import com.flamingo.theme.FlamingoIcon

@StatesPlayroomDemo
class ListItemStatesPlayroom : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.states_playroom_listitem, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var startSlot by mutableStateOf(Content.NO)
        var title by mutableStateOf(Text.ONE_LINE)
        var titleIcon: FlamingoIcon? by mutableStateOf(null)
        var titleIconTint by mutableStateOf(IconDemoColors.Null)
        var titleIconPosition by mutableStateOf(TextWrapper.TextIconPosition.START)
        var titlePadding by mutableStateOf(false)
        var subtitle by mutableStateOf(Text.NO)
        var subtitleIcon: FlamingoIcon? by mutableStateOf(null)
        var subtitleIconTint by mutableStateOf(IconDemoColors.Null)
        var subtitleIconPosition by mutableStateOf(TextWrapper.TextIconPosition.START)
        var subtitlePadding by mutableStateOf(false)
        var description by mutableStateOf(Text.NO)
        var descriptionIcon: FlamingoIcon? by mutableStateOf(null)
        var descriptionIconTint by mutableStateOf(IconDemoColors.Null)
        var descriptionIconPosition by mutableStateOf(TextWrapper.TextIconPosition.START)
        var descriptionPadding by mutableStateOf(false)
        var date by mutableStateOf(Date.NO)
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
                    titleWrapper = TextWrapper(
                        text = "",
                        padding = PaddingValues(if (titlePadding) 10.dp else 0.dp),
                        icon = titleIcon,
                        iconTint = getColorByName(titleIconTint),
                        iconPosition = titleIconPosition
                    ),
                    subtitle = subtitle,
                    subtitleWrapper = TextWrapper(
                        text = "",
                        padding = PaddingValues(if (subtitlePadding) 10.dp else 0.dp),
                        icon = subtitleIcon,
                        iconTint = getColorByName(subtitleIconTint),
                        iconPosition = subtitleIconPosition
                    ),
                    description = description,
                    descriptionWrapper = TextWrapper(
                        text = "",
                        padding = PaddingValues(if (descriptionPadding) 10.dp else 0.dp),
                        icon = descriptionIcon,
                        iconTint = getColorByName(descriptionIconTint),
                        iconPosition = descriptionIconPosition
                    ),
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
                findPreference("titleIcon").isVisible = title == Text.TEXT_WRAPPER
                findPreference("titleIconTint").isVisible = title == Text.TEXT_WRAPPER
                findPreference("titleIconPosition").isVisible = title == Text.TEXT_WRAPPER
                findPreference("titlePadding").isVisible = title == Text.TEXT_WRAPPER
                true
            }
            initPref(savedInstanceState, defVal = Text.ONE_LINE.toString())
        }

        configurePreference<DropDownPreference>("titleIcon") {
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
                titleIcon =
                    (it as? String)?.parceNull()?.let { Flamingo.icons.fromName(context, it) }
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        configurePreference<DropDownPreference>("titleIconTint") {
            entries = IconDemoColors.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { newValue ->
                titleIconTint = IconDemoColors.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = IconDemoColors.Null)
        }

        configurePreference<DropDownPreference>("titleIconPosition") {
            entries = TextWrapper.TextIconPosition.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { newValue ->
                titleIconPosition = TextWrapper.TextIconPosition.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = TextWrapper.TextIconPosition.START)
        }

        configurePreference<SwitchPreferenceCompat>("titlePadding") {
            onChange { newValue ->
                titlePadding = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("subtitle") {
            val texts = Text.values().map { it.name }.toTypedArray()
            entries = texts
            entryValues = texts
            onChange { newValue ->
                subtitle = Text.valueOf(newValue)
                findPreference("subtitleIcon").isVisible = subtitle == Text.TEXT_WRAPPER
                findPreference("subtitleIconTint").isVisible = subtitle == Text.TEXT_WRAPPER
                findPreference("subtitleIconPosition").isVisible = subtitle == Text.TEXT_WRAPPER
                findPreference("subtitlePadding").isVisible = subtitle == Text.TEXT_WRAPPER
                true
            }
            initPref(savedInstanceState, defVal = Text.NO.toString())
        }

        configurePreference<DropDownPreference>("subtitleIcon") {
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
                subtitleIcon =
                    (it as? String)?.parceNull()?.let { Flamingo.icons.fromName(context, it) }
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        configurePreference<DropDownPreference>("subtitleIconTint") {
            entries = IconDemoColors.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { newValue ->
                subtitleIconTint = IconDemoColors.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = IconDemoColors.Null)
        }

        configurePreference<DropDownPreference>("subtitleIconPosition") {
            entries = TextWrapper.TextIconPosition.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { newValue ->
                subtitleIconPosition = TextWrapper.TextIconPosition.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = TextWrapper.TextIconPosition.START)
        }

        configurePreference<SwitchPreferenceCompat>("subtitlePadding") {
            onChange { newValue ->
                subtitlePadding = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("description") {
            val texts = Text.values().map { it.name }.toTypedArray()
            entries = texts
            entryValues = texts
            onChange { newValue ->
                description = Text.valueOf(newValue)
                findPreference("descriptionIcon").isVisible = description == Text.TEXT_WRAPPER
                findPreference("descriptionIconTint").isVisible = description == Text.TEXT_WRAPPER
                findPreference("descriptionIconPosition").isVisible =
                    description == Text.TEXT_WRAPPER
                findPreference("descriptionPadding").isVisible = description == Text.TEXT_WRAPPER
                true
            }
            initPref(savedInstanceState, defVal = Text.NO.toString())
        }

        configurePreference<DropDownPreference>("descriptionIcon") {
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
                descriptionIcon =
                    (it as? String)?.parceNull()?.let { Flamingo.icons.fromName(context, it) }
                true
            }
            initPref(savedInstanceState, defVal = "null")
        }

        configurePreference<DropDownPreference>("descriptionIconTint") {
            entries = IconDemoColors.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { newValue ->
                descriptionIconTint = IconDemoColors.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = IconDemoColors.Null)
        }

        configurePreference<DropDownPreference>("descriptionIconPosition") {
            entries = TextWrapper.TextIconPosition.values().map { it.name }.toTypedArray()
            entryValues = entries
            onChange { newValue ->
                descriptionIconPosition = TextWrapper.TextIconPosition.valueOf(newValue)
                true
            }
            initPref(savedInstanceState, defVal = TextWrapper.TextIconPosition.START)
        }

        configurePreference<SwitchPreferenceCompat>("descriptionPadding") {
            onChange { newValue ->
                descriptionPadding = newValue as? Boolean ?: return@onChange false
                true
            }
            initPref(savedInstanceState, defVal = false)
        }

        configurePreference<DropDownPreference>("date") {
            val texts = Date.values().map { it.name }.toTypedArray()
            entries = texts
            entryValues = texts
            onChange { newValue ->
                date = Date.valueOf(newValue)
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

    private enum class Text { NO, ONE_LINE, MULTILINE, TEXT_WRAPPER }
    private enum class Date { NO, ONE_LINE, MULTILINE }
    private enum class Actions { NO, ONE, TWO }

    @Composable
    private fun ListItemPreview(
        start: Content,
        title: Text,
        titleWrapper: TextWrapper,
        subtitle: Text,
        subtitleWrapper: TextWrapper,
        description: Text,
        descriptionWrapper: TextWrapper,
        date: Date,
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
                Text.ONE_LINE -> TextWrapper("ListItem " + "Title ")
                Text.MULTILINE -> TextWrapper("ListItem " + "Title ".repeat(30))
                Text.TEXT_WRAPPER -> TextWrapper(
                    text = "ListItem " + "Title ",
                    padding = titleWrapper.padding,
                    icon = titleWrapper.icon,
                    iconTint = titleWrapper.iconTint,
                    iconPosition = titleWrapper.iconPosition
                )
            },
            subtitle = when (subtitle) {
                Text.NO -> null
                Text.ONE_LINE -> TextWrapper("ListItem " + "Subtitle ")
                Text.MULTILINE -> TextWrapper("ListItem " + "Subtitle ".repeat(7))
                Text.TEXT_WRAPPER -> TextWrapper(
                    text = "ListItem " + "Subtitle ",
                    padding = subtitleWrapper.padding,
                    icon = subtitleWrapper.icon,
                    iconTint = subtitleWrapper.iconTint,
                    iconPosition = subtitleWrapper.iconPosition
                )
            },
            description = when (description) {
                Text.NO -> null
                Text.ONE_LINE -> TextWrapper("ListItem " + "Description ")
                Text.MULTILINE -> TextWrapper("ListItem " + "Description ".repeat(7))
                Text.TEXT_WRAPPER -> TextWrapper(
                    text = "ListItem " + "Description ",
                    padding = descriptionWrapper.padding,
                    icon = descriptionWrapper.icon,
                    iconTint = descriptionWrapper.iconTint,
                    iconPosition = descriptionWrapper.iconPosition
                )
            },
            date = when (date) {
                Date.NO -> null
                Date.ONE_LINE -> AnnotatedString("ListItem " + "Date ")
                Date.MULTILINE -> AnnotatedString("ListItem " + "Date ".repeat(7))
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

    // needed because Flamingo.colors is a @Composable function, that can't be called in configurePreference
    @Composable
    private fun getColorByName(color: IconDemoColors): Color? {
        return when (color) {
            IconDemoColors.Primary -> Flamingo.colors.primary
            IconDemoColors.TextPrimary -> Flamingo.colors.textPrimary
            IconDemoColors.TextSecondary -> Flamingo.colors.textSecondary
            IconDemoColors.TextTertiary -> Flamingo.colors.textTertiary
            IconDemoColors.Error -> Flamingo.colors.error
            IconDemoColors.Warning -> Flamingo.colors.warning
            IconDemoColors.Null -> null
        }
    }

    private enum class IconDemoColors {
        Null, Primary, TextPrimary, TextSecondary, TextTertiary, Error, Warning
    }
}
