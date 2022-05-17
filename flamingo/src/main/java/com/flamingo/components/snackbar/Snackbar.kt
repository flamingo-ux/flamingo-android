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
    "SpacingAroundParens"
)

package com.flamingo.components.snackbar

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Snackbar
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.flamingo.Flamingo
import com.flamingo.InternalComponents
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.components.FlamingoComponentBase
import com.flamingo.components.Text
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.button.ButtonVariant
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.internalComponents
import kotlin.coroutines.resume
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * See [SnackbarHost].
 */
@OptIn(ExperimentalMaterialApi::class)
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.SnackbarPreview",
    figma = "https://todo.com/file/qVO8jDuABDK9vsuLqRXeMx?node-id=628%3A19",
    specification = "https://todo.com/x/1JXNmgE",
    demo = [
        "com.flamingo.playground.components.snackbar.AllStates",
        "com.flamingo.playground.components.snackbar.TypicalUsage"
    ],
    supportsWhiteMode = false,
)
@UsedInsteadOf("androidx.compose.material.Snackbar")
@Composable
public fun InternalComponents.Snackbar(
    modifier: Modifier = Modifier,
    snackbarData: SnackbarData,
): Unit = FlamingoComponentBase {
    with(snackbarData) {
        val backgroundColor =
            if (isError) Flamingo.colors.error
            else Flamingo.colors.inverse.backgroundSecondary
        val textColor =
            if (isError) Flamingo.colors.global.light.textPrimary
            else Flamingo.colors.inverse.textPrimary
        val buttonColor =
            if (isError) ButtonColor.White
            else ButtonColor.Primary

        BoxWithConstraints(modifier) {
            val swipeableState = rememberSwipeableState(0)
            val sizePx = with(LocalDensity.current) { maxWidth.toPx() }
            val anchors = mapOf(-sizePx to -1, 0f to 0, sizePx to 1)

            val containerModifier = Modifier
                .offset {
                    if (abs(swipeableState.currentValue) == 1) dismiss()
                    IntOffset(swipeableState.offset.value.roundToInt(), 0)
                }
                .padding(16.dp)
                .background(backgroundColor, SnackbarShape)
                .clip(SnackbarShape)

            Box(Modifier.swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )) {
                if (actionOnNewLine) SameLineAction(containerModifier, textColor, buttonColor)
                else NewLineAction(containerModifier, textColor, buttonColor)
            }
        }
    }
}

@Composable
private fun SnackbarData.SameLineAction(
    modifier: Modifier,
    textColor: Color,
    buttonColor: ButtonColor,
) = Column(modifier = modifier) {
    Label(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 12.dp,
                bottom = if (actionLabel != null) 0.dp else 12.dp,
            ),
        snackbarData = this@SameLineAction,
        textColor = textColor,
    )
    val actionLabel = actionLabel
    if (actionLabel != null) Box(modifier = Modifier
        .align(Alignment.End)
        .padding(
            start = 20.dp,
            end = 20.dp,
            bottom = 4.dp,
        )
    ) { Action(this@SameLineAction, actionLabel, buttonColor) }
}

@Composable
private fun SnackbarData.NewLineAction(
    modifier: Modifier,
    textColor: Color,
    buttonColor: ButtonColor,
) = Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
) {
    Label(
        modifier = Modifier
            .weight(1f)
            .padding(
                start = 20.dp,
                top = 12.dp,
                bottom = 12.dp,
                end = if (actionLabel != null) 8.dp else 20.dp,
            ),
        snackbarData = this@NewLineAction,
        textColor = textColor,
    )
    val actionLabel = actionLabel
    if (actionLabel != null) Box(modifier = Modifier.padding(
        top = 2.dp,
        bottom = 2.dp,
        end = 12.dp
    )) { Action(this@NewLineAction, actionLabel, buttonColor) }
}

@Composable
private fun Action(
    snackbarData: SnackbarData,
    actionLabel: String,
    buttonColor: ButtonColor,
) {
    Button(
        onClick = { snackbarData.performAction() },
        label = actionLabel,
        color = buttonColor,
        variant = ButtonVariant.TEXT,
        widthPolicy = ButtonWidthPolicy.STRICT
    )
}

@Composable
private fun Label(
    modifier: Modifier,
    snackbarData: SnackbarData,
    textColor: Color,
) = Text(
    modifier = modifier,
    maxLines = 2,
    overflow = TextOverflow.Ellipsis,
    text = snackbarData.message,
    style = Flamingo.typography.body2,
    color = textColor
)

private val SnackbarShape = RoundedCornerShape(24.dp)

/**
 * Host for [Snackbar]s to be used in the root of the screen to properly show, hide and dismiss
 * [Snackbar]s.
 *
 * In [content], [LocalSnackbarHostState] can be used to [SnackbarHostState.showSnackbar].
 *
 * Only __ONE__ [SnackbarHost] must be present at any time on one screen.
 */
@Composable
public fun SnackbarHost(content: @Composable () -> Unit) {
    val hostState = remember { SnackbarHostState() }
    val currentSnackbarData = hostState.currentSnackbarData
    val accessibilityManager = LocalAccessibilityManager.current
    LaunchedEffect(currentSnackbarData) {
        if (currentSnackbarData != null) {
            val duration = currentSnackbarData.duration.toMillis(
                hasAction = currentSnackbarData.actionLabel != null,
                accessibilityManager = accessibilityManager
            )
            delay(duration)
            currentSnackbarData.dismiss()
        }
    }

    Box {
        CompositionLocalProvider(LocalSnackbarHostState provides hostState) {
            content()
        }
        Crossfade(
            targetState = currentSnackbarData,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) { snackbarData ->
            if (snackbarData != null) internalComponents.Snackbar(
                modifier = Modifier.semantics {
                    liveRegion = LiveRegionMode.Polite
                    dismiss { snackbarData.dismiss(); true }
                },
                snackbarData = snackbarData
            )
        }
    }
}

public val LocalSnackbarHostState: ProvidableCompositionLocal<SnackbarHostState> =
    compositionLocalOf { error("Nothing is provided") }

/**
 * State of the [SnackbarHost], controls the queue and the current [Snackbar] being shown inside
 * the [SnackbarHost].
 *
 * This state lives as a part of a [LocalSnackbarHostState] and provided to the [SnackbarHost]
 * automatically.
 */
@Stable
public class SnackbarHostState internal constructor() {

    /**
     * Only one [Snackbar] can be shown at a time.
     * Since a suspending Mutex is a fair queue, this manages our message queue
     * and we don't have to maintain one.
     */
    private val mutex = Mutex()

    /**
     * The current [SnackbarData] being shown by the [SnackbarHost], of `null` if none.
     */
    public var currentSnackbarData: SnackbarData? by mutableStateOf(null)
        private set

    /**
     * Shows or queues to be shown a [Snackbar] at the bottom of the [SnackbarHost] at
     * which this state is attached and suspends until snackbar is disappeared.
     *
     * [SnackbarHostState] guarantees to show at most one snackbar at a time. If this function is
     * called while another snackbar is already visible, it will be suspended until this snack
     * bar is shown and subsequently addressed. If the caller is cancelled, the snackbar will be
     * removed from display and/or the queue to be displayed.
     *
     * @param message text to be shown in the [Snackbar]
     * @param actionLabel optional action label to show as button in the [Snackbar]
     * @param actionOnNewLine if true, action will bw placed at the bottom, else — to the side
     * @param isError if true, [Snackbar] will be red
     * @param duration duration to control how long snackbar will be shown in [SnackbarHost]
     *
     * @return [SnackbarResult.ActionPerformed] if option action has been clicked or
     * [SnackbarResult.Dismissed] if snackbar has been dismissed via timeout or by the user
     */
    public suspend fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        actionOnNewLine: Boolean = false,
        isError: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short,
    ): SnackbarResult = mutex.withLock {
        try {
            return suspendCancellableCoroutine { continuation ->
                currentSnackbarData = SnackbarDataImpl(
                    message = message,
                    actionLabel = actionLabel,
                    duration = duration,
                    actionOnNewLine = actionOnNewLine,
                    isError = isError,
                    continuation = continuation
                )
            }
        } finally {
            currentSnackbarData = null
        }
    }

    @Stable
    private class SnackbarDataImpl(
        override val message: String,
        override val actionLabel: String?,
        override val duration: SnackbarDuration,
        override val actionOnNewLine: Boolean,
        override val isError: Boolean,
        private val continuation: CancellableContinuation<SnackbarResult>,
    ) : SnackbarData {

        override fun performAction() {
            if (continuation.isActive) continuation.resume(SnackbarResult.ActionPerformed)
        }

        override fun dismiss() {
            if (continuation.isActive) continuation.resume(SnackbarResult.Dismissed)
        }
    }
}

/**
 * Interface to represent one particular [Snackbar] as a piece of the [SnackbarHostState]
 */
public interface SnackbarData {
    public val message: String
    public val actionLabel: String?
    public val duration: SnackbarDuration
    public val actionOnNewLine: Boolean
    public val isError: Boolean

    /**
     * Function to be called when Snackbar action has been performed to notify the listeners
     */
    public fun performAction()

    /**
     * Function to be called when Snackbar is dismissed either by timeout or by the user
     */
    public fun dismiss()
}

/**
 * Possible results of the [SnackbarHostState.showSnackbar] call
 */
public enum class SnackbarResult {
    /**
     * [Snackbar] that is shown has been dismissed either by timeout of by user
     */
    Dismissed,

    /**
     * Action on the [Snackbar] has been clicked before the time out passed
     */
    ActionPerformed,
}

/**
 * Possible durations of the [Snackbar] in [SnackbarHost]
 */
public enum class SnackbarDuration {
    /**
     * Show the Snackbar for a short period of time
     */
    Short,

    /**
     * Show the Snackbar for a long period of time
     */
    Long,

    /**
     * Show the Snackbar indefinitely until explicitly dismissed or action is clicked
     */
    Indefinite
}

internal fun SnackbarDuration.toMillis(
    hasAction: Boolean,
    accessibilityManager: AccessibilityManager?,
): Long {
    val original = when (this) {
        SnackbarDuration.Indefinite -> Long.MAX_VALUE
        SnackbarDuration.Long -> 10000L
        SnackbarDuration.Short -> 4000L
    }
    if (accessibilityManager == null) {
        return original
    }
    return accessibilityManager.calculateRecommendedTimeoutMillis(
        original,
        containsIcons = true,
        containsText = true,
        containsControls = hasAction
    )
}
