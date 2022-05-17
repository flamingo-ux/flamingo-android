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
    "SpacingAroundParens",
)

package com.flamingo

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.util.fastForEachIndexed
import java.util.Locale

/**
 * If possible, pronounces [textToSay] after entering the composition, else - displays a toast with
 * [toastText].
 *
 * @param key every time it changes, [textToSay] is spoken (or toast is shown) again
 * @param textToSay in english language
 * @param sayAndToast if true, toast will be shown regardless of a success/failure to speak
 */
@Composable
internal fun Say(
    key: Any?,
    textToSay: String,
    toastText: String = textToSay,
    sayAndToast: Boolean = false,
) {
    val ctx = LocalContext.current
    DisposableEffect(key) {
        var tts: TextToSpeech? = null
        var toast: Toast? = null
        tts = TextToSpeech(ctx) { status ->
            if (sayAndToast || status != TextToSpeech.SUCCESS) {
                val length = if (sayAndToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
                toast = Toast.makeText(ctx, toastText, length)?.apply { show() }
            }
            if (status == TextToSpeech.SUCCESS) tts?.run {
                language = Locale.US
                setSpeechRate(1f)
                speak(textToSay, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
        onDispose {
            toast?.cancel()
            tts.stop()
            tts.shutdown()
        }
    }
}

@Composable
internal fun rainbowColorAnim(): State<Color> = with(Flamingo.palette) {
    rememberInfiniteTransition().animateColor(
        initialValue = orhid500,
        targetValue = red500,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = rainbowColors.size * 150
                rainbowColors.fastForEachIndexed { index, color -> color at index * 150 }
            }
        )
    )
}

private val rainbowColors = with(Flamingo.palette) {
    listOf(
        orhid500, electricBlue500, blue500, arctic500, spring500, green500, yellow500, orange500,
        pink500, red500,
    )
}
