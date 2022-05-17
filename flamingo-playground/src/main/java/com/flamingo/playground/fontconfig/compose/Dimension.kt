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

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal data class Dimension(
    val value: Float = 0f,
    /** true - [Dp], false - [TextUnitType.Sp] */
    val isDp: Boolean = true,
) {
    constructor(string: String) : this(
        value = string.dropLast(3).toFloat(),
        isDp = string.endsWith(".dp"),
    )

    @Composable
    fun toDp(): Dp = with(LocalDensity.current) { if (isDp) value.dp else value.sp.toDp() }

    override fun toString(): String = "$value${if (isDp) ".dp" else ".sp"}"
}
