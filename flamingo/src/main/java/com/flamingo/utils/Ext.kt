package com.flamingo

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.Dimension
import androidx.annotation.Dimension.DP
import androidx.annotation.Px
import androidx.cardview.widget.CardView
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.flamingo.components.Elevation
import com.flamingo.components.listitem.ListItem
import com.flamingo.utils.UnitConversions
import com.flamingo.utils.UnitConversions.dp
import com.flamingo.utils.isNightMode

/**
 * Convert decimal percentage (0-100) to Hex in decimal representation (0-255).
 * Use for alpha-values conversions, i.e 50% alpha => 127 (0x7F)
 */
@Suppress("MagicNumber")
internal inline val Int.hexp: Int
    get() = this * 255 / 100

/**
 * Sets [CardView] elevation, but in the dark theme also sets [CardView] background to the
 * appropriate color. More info:
 * [https://www.todo.com/file/siTvBrYyUPigpjkZfOhJ1h/2.Core-Styles-%26-Icons?node-id=337%3A1261]
 *
 * For behavior demo and more explanation, open
 * [com.flamingo.playground.CardElevationDemoFragment].
 *
 * @sample com.flamingo.playground.CardElevationDemoFragment
 */
@Suppress("DEPRECATION")
public fun CardView.setCardElevationWithBackground(elevation: Elevation) {
    setCardElevationWithBackground(elevation.dp.value.toInt())
}

/**
 * Sets [CardView] elevation, but in the dark theme also sets [CardView] background to the
 * appropriate color. More info:
 * [https://www.todo.com/file/siTvBrYyUPigpjkZfOhJ1h/2.Core-Styles-%26-Icons?node-id=337%3A1261]
 *
 * For behavior demo and more explanation, open
 * [com.flamingo.playground.CardElevationDemoFragment].
 *
 * @param elevationDp is in dp. If not one of: 0, 1, 2, 3, 4, 6, 8, 12, 24, exception is thrown.
 * @sample com.flamingo.playground.CardElevationDemoFragment
 */
@Deprecated("Use other overload with enum parameter")
@Suppress("MagicNumber", "NaiveCardElevationInCode")
public fun CardView.setCardElevationWithBackground(@Dimension(unit = DP) elevationDp: Int) {
    val nonSupportedElevationMsg = "Elevation value is not supported, use only supported ones"
    require(cardElevationDarkBackgroundColors.containsKey(elevationDp)) { nonSupportedElevationMsg }

    @Px val elevationPx = elevationDp.toFloat().dp
    maxCardElevation = elevationPx
    cardElevation = elevationPx

    if (!isNightMode()) return

    setCardBackgroundColor(
        cardElevationDarkBackgroundColors.getOrElse(elevationDp) { error(nonSupportedElevationMsg) }
    )
}

/**
 * Keys represent all allowed card elevation values in dp.
 * Values are dark background colors which correspond to the [elevation] keys (in dp). Used in
 * [CardView.setCardElevationWithBackground]. More info:
 * [https://www.todo.com/file/siTvBrYyUPigpjkZfOhJ1h/2.Core-Styles-%26-Icons?node-id=337%3A1261]
 */
@Suppress("MagicNumber")
public val cardElevationDarkBackgroundColors: Map<Int, Int> = HashMap<Int, Int>(9, 1f).apply {
    val small = Flamingo.palette.grey950.toArgb()
    val medium = Flamingo.palette.grey900.toArgb()
    val large = Flamingo.palette.grey850.toArgb()
    put(0, Flamingo.palette.black.toArgb())
    put(1, small)
    put(2, small)
    put(3, small) // target
    put(4, medium)
    put(6, medium) // target
    put(8, medium)
    put(12, large) // target
    put(24, large)
}

@SuppressLint("AlphaDisabledRedeclaration") // this is the original declaration
public const val ALPHA_DISABLED: Float = 0.38f

/**
 * Must be called in custom views, if any of [UnitConversions] extension properties are used
 * (for example, [UnitConversions.dp] or [UnitConversions.sp]).
 *
 * Must be called before a first call
 * to the aforementioned properties, or custom view will not be rendered in a layout editor because
 * of an uninitialized property exception ([UnitConversions.context]).
 */
internal fun View.initUnitConversionsInCustomView() {
    runCatching { if (isInEditMode) UnitConversions.init(context) }
}

/**
 * If length of [this] is bigger than [maxLength], truncates it with [ellipsisText], such that
 * length of the resulting string is [maxLength]. Else - returns [this]
 */
internal fun String.ellipsize(maxLength: Int, ellipsisText: String): String {
    return if (length <= maxLength) {
        this
    } else {
        take(maxLength - ellipsisText.length) + ellipsisText
    }
}

/**
 * @param words number of words from "Lorem Ipsum" to use
 */
public fun loremIpsum(words: Int): String = LoremIpsum(words).values.first().replace("\n", " ")

/**
 * Applies [ParagraphStyle] and [SpanStyle] derived from the [style].
 *
 * Note, that once a portion of the text is marked with a [ParagraphStyle], that portion will be
 * separated from the remaining as if a line feed character was added.
 *
 * Applies both [ParagraphStyle] and [SpanStyle] because, if applied separately, they can cause
 * inconsistencies in the style of th text.
 *
 * Primarily used in [ListItem]'s [AnnotatedString] overload.
 *
 * @param style [TextStyle] to be applied
 * @param block function to be executed
 *
 * @return result of the [block]
 */
public inline fun <R : Any> AnnotatedString.Builder.withStyle(
    style: TextStyle,
    crossinline block: AnnotatedString.Builder.() -> R,
): R = withStyle(style.toParagraphStyle()) {
    withStyle(style.toSpanStyle()) {
        block(this)
    }
}
