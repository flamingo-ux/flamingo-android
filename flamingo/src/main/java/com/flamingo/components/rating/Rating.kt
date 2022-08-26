@file:Suppress("LongParameterList")

package com.flamingo.components.rating

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.Say
import com.flamingo.annotations.DelicateFlamingoApi
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.FlamingoComponentBase
import com.flamingo.components.Icon
import com.flamingo.theme.FlamingoIcon
import com.flamingo.utils.times

/**
 * Rating bar with selectable stars. Used typically in "feedback" applications. To use it, it is
 * __required__ to add this to the `AndroidManifest.xml`:
 * ```xml
 * <uses-permission android:name="android.permission.VIBRATE" />
 * ```
 *
 * @param value if [halves] is false, [value] will be in the range of [0..5], else - [0..10],
 * representing number of selected steps (stars). If incorrect value is supplied in [value],
 * exception will be thrown. Thus, be __cautious__ when switching [halves] while having some value
 * saved in a [MutableState].
 *
 * @param onSelected will be called when number of selected steps (stars) is requested to change
 * (either from a tap or a continuous dragging). If null, [Rating] will become non-interactive.
 * [selectedSteps] will never be `0` (meaning that if user already selected a step (star) by tapping
 * or just began a dragging gesture, it will not be possible to select 0 steps (stars)
 * (to deselect)). If [halves] is false, [selectedSteps] will be in the range of [0..5], else -
 * [0..10].
 *
 * @param halves if true, user is allowed to select half of each star, else - only whole star can
 * be selected
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.RatingPreview",
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx?node-id=1026%3A1",
    specification = "https://confluence.companyname.ru/x/WiTQgAE",
    demo = ["com.flamingo.playground.components.RatingStatesPlayroom"],
    supportsWhiteMode = false,
)
@Composable
public fun Rating(
    value: UInt,
    onSelected: ((selectedSteps: UInt) -> Unit)? = null,
    halves: Boolean = false,
    size: RatingSize = RatingSize.MEDIUM,
): Unit = Rating(
    value = value,
    onSelected = onSelected,
    halves = halves,
    dragEnabled = true,
    vibrationEnabled = true,
    wholeSteps = 5u,
    size = size,
)

/**
 * Beta version, has some bugs.
 */
@DelicateFlamingoApi
@Composable
public fun Rating(
    value: UInt,
    onSelected: ((selectedSteps: UInt) -> Unit)? = null,
    halves: Boolean = false,
    dragEnabled: Boolean = true,
    vibrationEnabled: Boolean = true,
    wholeSteps: UInt = 5u,
    size: RatingSize = RatingSize.MEDIUM,
): Unit = FlamingoComponentBase {
    WarnAboutSmallRating(size, onSelected)
    Rating(
        value = value,
        onSelected = onSelected,
        halves = halves,
        dragEnabled = dragEnabled,
        vibrationEnabled = vibrationEnabled,
        wholeSteps = wholeSteps,
        iconSize = size.iconSize,
        spaceBetweenIcons = 8.dp,
    )
}

@Composable
private fun Rating(
    value: UInt,
    onSelected: ((selectedSteps: UInt) -> Unit)?,
    halves: Boolean,
    dragEnabled: Boolean,
    vibrationEnabled: Boolean,
    wholeSteps: UInt,
    iconSize: Dp,
    spaceBetweenIcons: Dp,
): Unit = FlamingoComponentBase {
    val validValueRange = if (halves) 0u..wholeSteps * 2u else 0u..wholeSteps
    require(value in validValueRange) { "value = $value, should be in $validValueRange" }
    require(wholeSteps != 0u) { "wholeSteps = 0, should not" }

    val context = LocalContext.current
    val iconWidth = iconSize
    val totalWidth = iconWidth * wholeSteps + spaceBetweenIcons * wholeSteps
    val stepDragIndex = remember { mutableStateOf(0u) }

    val dragModifier = Modifier.ratingDrag(
        onSelected, stepDragIndex, dragEnabled, spaceBetweenIcons, totalWidth, wholeSteps, halves,
        vibrationEnabled, context
    )
    val tapModifier = Modifier.ratingTap(
        onSelected, spaceBetweenIcons, totalWidth, wholeSteps, halves, vibrationEnabled, context,
    )

    BoxWithConstraints {
        val notEnoughSpace = maxWidth < totalWidth
        Row(
            modifier = Modifier
                // disables drag gestures if scroll is needed
                .run { if (notEnoughSpace) horizontalScroll(rememberScrollState()) else this }
                .run {
                    if (!notEnoughSpace && dragEnabled && onSelected != null) then(dragModifier)
                    else this
                }
                .run { if (onSelected != null) then(tapModifier) else this },
            horizontalArrangement = Arrangement.spacedBy(spaceBetweenIcons)
        ) {
            repeat(wholeSteps.toInt()) {
                val index = it + 1
                val icon = calculateIcon(
                    iconIndex = index.toUInt(),
                    stepIndex = if (stepDragIndex.value == 0u) value else stepDragIndex.value,
                    halves = halves
                )
                Icon(
                    modifier = Modifier.requiredSize(iconSize),
                    icon = icon,
                    tint = Flamingo.colors.rating
                )
            }
        }
    }
}

private fun calculateIcon(
    iconIndex: UInt,
    stepIndex: UInt,
    halves: Boolean,
): FlamingoIcon {
    require(iconIndex >= 1u) { "index = $iconIndex, should be <= 1" }
    val value = if (halves) stepIndex else stepIndex * 2u
    return when {
        value >= iconIndex * 2u -> Flamingo.icons.StarFilled
        value >= iconIndex * 2u - 1u -> Flamingo.icons.StarHalfFilled
        else -> Flamingo.icons.Star
    }
}

@Composable
private fun WarnAboutSmallRating(
    size: RatingSize,
    onSelected: ((UInt) -> Unit)?,
) {
    val warn = Flamingo.isStagingBuild && size != RatingSize.LARGE && onSelected != null
    if (!warn) return
    val sizeName = RatingSize.SMALL.name.lowercase()
    Say(
        key = true,
        textToSay = "Bad usage detected: clickable Rating with $sizeName size. It is too " +
                "small to click.",
        sayAndToast = true,
    )
}

public enum class RatingSize(public val iconSize: Dp) {
    SMALL(iconSize = 16.dp),
    MEDIUM(iconSize = 24.dp),
    LARGE(iconSize = 40.dp),
}
