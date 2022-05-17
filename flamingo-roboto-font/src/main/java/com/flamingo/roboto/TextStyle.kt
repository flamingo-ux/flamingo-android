@file:Suppress("NOTHING_TO_INLINE", "MagicNumber")

package com.flamingo.roboto

import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.core.widget.TextViewCompat
import com.flamingo.utils.UnitConversions.sp
import com.flamingo.utils.firstBaselineToTopHeightCompat
import com.flamingo.utils.lastBaselineToBottomHeightCompat

/**
 * Sets flamingo approved text style on a [TextView]. An alternative to this xml:
 * ```
 * <TextView
 *     style="@style/TextStyle.Flamingo.Headline1"
 *     android:layout_width="wrap_content"
 *     android:layout_height="wrap_content"
 *     android:text="Lorem isprum" />
 * ```
 *
 * ## WARNING!
 * If you use this function, you are prohibited from changing [TextView.getPaddingTop] and
 * [TextView.getPaddingBottom]. Else, [TextView] will be rendered incorrectly.
 *
 * For more info on why this requirement exists, see [firstBaselineToTopHeightCompat] and
 * [lastBaselineToBottomHeightCompat].
 *
 * @param dsTextStyleResId one of [R.style.TextStyle_Flamingo], for example
 * [R.style.TextStyle_Flamingo_Headline1]
 */
@Suppress("ComplexMethod")
public fun TextView.dsTextStyle(@StyleRes dsTextStyleResId: Int): Unit = when (dsTextStyleResId) {
    R.style.TextStyle_Flamingo_Headline1 -> dsStyleHeadline1()
    R.style.TextStyle_Flamingo_Headline2 -> dsStyleHeadline2()
    R.style.TextStyle_Flamingo_Headline3 -> dsStyleHeadline3()
    R.style.TextStyle_Flamingo_Headline4 -> dsStyleHeadline4()
    R.style.TextStyle_Flamingo_Headline5 -> dsStyleHeadline5()
    R.style.TextStyle_Flamingo_Headline6 -> dsStyleHeadline6()
    R.style.TextStyle_Flamingo_Body1 -> dsStyleBody1()
    R.style.TextStyle_Flamingo_Body2 -> dsStyleBody2()
    R.style.TextStyle_Flamingo_Subtitle1 -> dsStyleSubtitle1()
    R.style.TextStyle_Flamingo_Subtitle2 -> dsStyleSubtitle2()
    R.style.TextStyle_Flamingo_Caption -> dsStyleCaption()
    R.style.TextStyle_Flamingo_Overline -> dsStyleOverline()
    R.style.TextStyle_Flamingo_Button -> dsStyleButton()
    else -> throw IllegalArgumentException("Wrong ds text style res id: $dsTextStyleResId")
}

private inline fun TextView.dsStyleHeadline1(): Unit = with(this) {
    TextViewCompat.setTextAppearance(this, R.style.TextAppearance_Flamingo_Headline1)
    includeFontPadding = false
    firstBaselineToTopHeightCompat = 32.sp
    setLineSpacing(
        0f,
        0.9960855f
    )
}

private inline fun TextView.dsStyleHeadline2(): Unit = with(this) {
    TextViewCompat.setTextAppearance(this, R.style.TextAppearance_Flamingo_Headline2)
    includeFontPadding = false
    setLineSpacing(
        0f,
        0.97f
    )
}

private inline fun TextView.dsStyleHeadline3(): Unit = with(this) {
    TextViewCompat.setTextAppearance(this, R.style.TextAppearance_Flamingo_Headline3)
    includeFontPadding = false
    firstBaselineToTopHeightCompat = 22.sp
    setLineSpacing(
        0f,
        1.08606951f
    )
}

private inline fun TextView.dsStyleHeadline4(): Unit = with(this) {
    TextViewCompat.setTextAppearance(this, R.style.TextAppearance_Flamingo_Headline4)
    includeFontPadding = false
    firstBaselineToTopHeightCompat = 22.sp
    setLineSpacing(
        0f,
        1.08606951f
    )
}

private inline fun TextView.dsStyleHeadline5(): Unit = with(this) {
    TextViewCompat.setTextAppearance(this, R.style.TextAppearance_Flamingo_Headline5)
    includeFontPadding = false
    firstBaselineToTopHeightCompat = 19.sp
    setLineSpacing(
        0f,
        1.02f
    )
}

private inline fun TextView.dsStyleHeadline6(): Unit = with(this) {
    TextViewCompat.setTextAppearance(this, R.style.TextAppearance_Flamingo_Headline6)
    includeFontPadding = false
    lastBaselineToBottomHeightCompat = 5.sp
    setLineSpacing(
        0f,
        1.06f
    )
}

private inline fun TextView.dsStyleBody1(): Unit = with(this) {
    TextViewCompat.setTextAppearance(this, R.style.TextAppearance_Flamingo_Body1)
    includeFontPadding = false
    firstBaselineToTopHeightCompat = 18.sp
    lastBaselineToBottomHeightCompat = 6.sp
    setLineSpacing(
        0f,
        1.22f
    )
}

private inline fun TextView.dsStyleBody2(): Unit = with(this) {
    TextViewCompat.setTextAppearance(this, R.style.TextAppearance_Flamingo_Body2)
    includeFontPadding = false
    firstBaselineToTopHeightCompat = 15.sp
    lastBaselineToBottomHeightCompat = 5.sp
    setLineSpacing(
        0f,
        1.13778711f
    )
}

private inline fun TextView.dsStyleSubtitle1(): Unit = with(this) {
    TextViewCompat.setTextAppearance(this, R.style.TextAppearance_Flamingo_Subtitle1)
    includeFontPadding = false
    firstBaselineToTopHeightCompat = 17.sp
    lastBaselineToBottomHeightCompat = 7.sp
    setLineSpacing(
        0f,
        1.27f
    )
}

private inline fun TextView.dsStyleSubtitle2(): Unit = with(this) {
    TextViewCompat.setTextAppearance(this, R.style.TextAppearance_Flamingo_Subtitle2)
    includeFontPadding = false
    firstBaselineToTopHeightCompat = 15.sp
    lastBaselineToBottomHeightCompat = 5.sp
    setLineSpacing(
        0f,
        1.13778711f
    )
}

private inline fun TextView.dsStyleCaption(): Unit = with(this) {
    TextViewCompat.setTextAppearance(this, R.style.TextAppearance_Flamingo_Caption)
    includeFontPadding = false
    firstBaselineToTopHeightCompat = 12.sp
    lastBaselineToBottomHeightCompat = 4.sp
    setLineSpacing(
        0f,
        1.13778711f
    )
}

private inline fun TextView.dsStyleOverline(): Unit = with(this) {
    TextViewCompat.setTextAppearance(this, R.style.TextAppearance_Flamingo_Overline)
    includeFontPadding = false
    firstBaselineToTopHeightCompat = 12.sp
    lastBaselineToBottomHeightCompat = 4.sp
    setLineSpacing(
        0f,
        1.2412223f
    )
}

private inline fun TextView.dsStyleButton(): Unit = with(this) {
    TextViewCompat.setTextAppearance(this, R.style.TextAppearance_Flamingo_Button)
    includeFontPadding = false
    setLineSpacing(
        0f,
        0.97524609f
    )
}
