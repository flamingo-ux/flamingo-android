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

package com.flamingo.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf

/**
 * @param text The text to be displayed.
 *
 * @param modifier [Modifier] to apply to this layout node.
 *
 * @param color [Color] to apply to the text.
 *
 * @param fontSize The size of glyphs to use when painting the text. See [TextStyle.fontSize].
 *
 * @param fontStyle The typeface variant to use when drawing the letters (e.g., italic).
 * See [TextStyle.fontStyle].
 *
 * @param fontWeight The typeface thickness to use when painting the text (e.g., [FontWeight.Bold]).
 *
 * @param fontFamily The font family to be used when rendering the text. See [TextStyle.fontFamily].
 *
 * @param letterSpacing The amount of space to add between each letter.
 * See [TextStyle.letterSpacing].
 *
 * @param textDecoration The decorations to paint on the text (e.g., an underline).
 * See [TextStyle.textDecoration].
 *
 * @param textAlign The alignment of the text within the lines of the paragraph.
 * See [TextStyle.textAlign].
 *
 * @param lineHeight Line height for the [Paragraph] in [TextUnit] unit, e.g. SP or EM.
 * See [TextStyle.lineHeight].
 *
 * @param overflow How visual overflow should be handled.
 *
 * @param softWrap Whether the text should break at soft line breaks. If false, the glyphs in the
 * text will be positioned as if there was unlimited horizontal space. If [softWrap] is false,
 * [overflow] and TextAlign may have unexpected effects.
 *
 * @param maxLines An optional maximum number of lines for the text to span, wrapping if
 * necessary. If the text exceeds the given number of lines, it will be truncated according to
 * [overflow] and [softWrap]. If it is not null, then it MUST BE greater than zero.
 *
 * @param onTextLayout Callback that is executed when a new text layout is calculated. A
 * [TextLayoutResult] object that callback provides contains paragraph information, size of the
 * text, baselines and other details. The callback can be used to add additional decoration or
 * functionality to the text. For example, to draw selection around the text.
 *
 * @param style Style configuration for the text such as color, font, line height etc.
 *
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.TextPreview",
    figma = "https://f.com/file/siTvBrYyUPigpjkZfOhJ1h?node-id=731%3A295",
    specification = "https://confluence.companyname.ru/x/J4HJPgE",
    demo = ["com.flamingo.playground.view.FontsDemoFragment"],
    supportsWhiteMode = false,
)
@UsedInsteadOf("androidx.compose.material.Text")
@Composable
public fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalFlamingoTextStyle.current,
) {
    Flamingo.checkFlamingoPresence()
    val mergedStyle = style.merge(
        TextStyle(
            color = color,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            lineHeight = lineHeight,
            fontFamily = fontFamily,
            textDecoration = textDecoration,
            fontStyle = fontStyle,
            letterSpacing = letterSpacing
        )
    )
    BasicText(text, modifier, mergedStyle, onTextLayout, overflow, softWrap, maxLines)
}

@Suppress("MaxLineLength")
/**
 * # Implementation details
 *
 * Code duplication is chosen because of [this](https://android-review.googlesource.com/c/platform/frameworks/support/+/1842390#:~:text=String-based%20overloads%20of%20BasicText%20and%20Text%20to%20be%20a%20bit%20more%20efficient%20than%20their%20AnnotatedString%20counterparts.)
 *
 * @see Text
 */
@Composable
public fun Text(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalFlamingoTextStyle.current,
) {
    Flamingo.checkFlamingoPresence()
    val mergedStyle = style.merge(
        TextStyle(
            color = color,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            lineHeight = lineHeight,
            fontFamily = fontFamily,
            textDecoration = textDecoration,
            fontStyle = fontStyle,
            letterSpacing = letterSpacing
        )
    )
    BasicText(
        text, modifier, mergedStyle, onTextLayout, overflow, softWrap, maxLines, inlineContent
    )
}

@Suppress("MaxLineLength")
/**
 * Exists to remove code duplication and to exercise [this performance optimization](https://android-review.googlesource.com/c/platform/frameworks/support/+/1842390#:~:text=String-based%20overloads%20of%20BasicText%20and%20Text%20to%20be%20a%20bit%20more%20efficient%20than%20their%20AnnotatedString%20counterparts.).
 *
 * @param text must be either [String] or [AnnotatedString]
 * @see Text
 */
@Composable
internal fun UniversalText(
    text: CharSequence,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalFlamingoTextStyle.current,
) {
    when (text) {
        is String -> Text(
            text, modifier, color, fontSize, fontStyle, fontWeight, fontFamily, letterSpacing,
            textDecoration, textAlign, lineHeight, overflow, softWrap, maxLines, onTextLayout,
            style,
        )
        is AnnotatedString -> Text(
            text, modifier, color, fontSize, fontStyle, fontWeight, fontFamily, letterSpacing,
            textDecoration, textAlign, lineHeight, overflow, softWrap, maxLines, inlineContent,
            onTextLayout, style,
        )
        else -> error("\"text\" param must be either [String] or [AnnotatedString]")
    }
}

/**
 * CompositionLocal containing the preferred [TextStyle] that will be used by [Text] components by
 * default.
 */
public val LocalFlamingoTextStyle: ProvidableCompositionLocal<TextStyle> =
    compositionLocalOf { TextStyle.Default }
