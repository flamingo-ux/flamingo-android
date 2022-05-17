@file:Suppress("ModifierOrdering",
    "CommentSpacing",
    "NoBlankLineBeforeRbrace",
    "ModifierOrdering",
    "NoConsecutiveBlankLines",
    "MagicNumber",
    "FunctionName",
    "MatchingDeclarationName",
    "LongParameterList",
    "SpacingAroundParens")

package com.flamingo.playground.fontconfig.compose

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

internal class Config {
    var text: String by mutableStateOf("Lorem isprum")
    var fontSize: TextUnit by mutableStateOf(14.sp)
    var fontWeight: FontWeight by mutableStateOf(FontWeight.Normal)
    var fontStyle: FontStyle by mutableStateOf(FontStyle.Normal)
    var letterSpacing: TextUnit by mutableStateOf(0.em)
    var lineHeight: TextUnit by mutableStateOf(16.sp)

    var paddingTop: MutableState<Dimension> = mutableStateOf(Dimension())
    var paddingBottom: MutableState<Dimension> = mutableStateOf(Dimension())
    var paddingFromBaselineTop: MutableState<Dimension> = mutableStateOf(Dimension())
    var paddingFromBaselineBottom: MutableState<Dimension> = mutableStateOf(Dimension())

    override fun toString(): String = """
        text = $text
        fontSize = $fontSize
        fontWeight = ${fontWeight.weight}
        fontStyle = $fontStyle
        letterSpacing = $letterSpacing
        lineHeight = $lineHeight
        paddingTop = ${paddingTop.value}
        paddingBottom = ${paddingBottom.value}
        paddingFromBaselineTop = ${paddingFromBaselineTop.value}
        paddingFromBaselineBottom = ${paddingFromBaselineBottom.value}
    """.trimIndent()

    companion object {
        val stateSaver: Saver<Config, Any> = mapSaver(
            save = {
                mapOf(
                    "text" to it.text,
                    "fontSize" to it.fontSize.toString(),
                    "fontWeight" to it.fontWeight.weight,
                    "fontStyle" to it.fontStyle.value,
                    "letterSpacing" to it.letterSpacing.toString(),
                    "lineHeight" to it.lineHeight.toString(),
                    "paddingTop" to it.paddingTop.value.toString(),
                    "paddingBottom" to it.paddingBottom.value.toString(),
                    "paddingFromBaselineTop" to it.paddingFromBaselineTop.value.toString(),
                    "paddingFromBaselineBottom" to it.paddingFromBaselineBottom.value.toString(),
                )
            },
            restore = {
                Config().apply {
                    text = it["text"] as String
                    fontSize = TextUnit(it["fontSize"] as String)
                    fontWeight = FontWeight(it["fontWeight"] as Int)
                    fontStyle = FontStyle(it["fontStyle"] as Int)
                    letterSpacing = TextUnit(it["letterSpacing"] as String)
                    lineHeight = TextUnit(it["lineHeight"] as String)
                    paddingTop = mutableStateOf(Dimension(it["paddingTop"] as String))
                    paddingBottom = mutableStateOf(Dimension(it["paddingBottom"] as String))
                    paddingFromBaselineTop =
                        mutableStateOf(Dimension(it["paddingFromBaselineTop"] as String))
                    paddingFromBaselineBottom =
                        mutableStateOf(Dimension(it["paddingFromBaselineBottom"] as String))
                }
            }
        )

        private fun TextUnit(string: String): TextUnit = when {
            string.endsWith(".sp") -> string.dropLast(3).toFloat().sp
            string.endsWith(".em") -> string.dropLast(3).toFloat().em
            string.endsWith("Unspecified") -> TextUnit.Unspecified
            else -> error("Unknown TextUnitType")
        }
    }
}
