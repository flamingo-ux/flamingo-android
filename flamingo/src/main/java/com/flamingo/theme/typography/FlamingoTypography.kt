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
    "NoMultipleSpaces",
    "SpacingAroundParens"
)

package com.flamingo.theme.typography

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import com.flamingo.Flamingo
import com.flamingo.annotations.DelicateFlamingoApi
import com.flamingo.theme.colors.FlamingoColors

/**
 * @constructor you are not supposed to call it, this class is created for you by the library and
 * can be accessed in [Flamingo.typography]
 */
@Immutable
public data class FlamingoTypography @DelicateFlamingoApi constructor(
    private val colors: FlamingoColors,
    public val display1: TextStyle,
    public val display2: TextStyle,
    public val display3: TextStyle,
    public val h1: TextStyle,
    public val h2: TextStyle,
    public val h3: TextStyle,
    public val h4: TextStyle,
    public val h5: TextStyle,
    public val h6: TextStyle,
    public val body1: TextStyle,
    public val body2: TextStyle,
    public val subtitle1: TextStyle,
    public val subtitle2: TextStyle,
    public val caption: TextStyle,
    public val overline: TextStyle,
    public val button: TextStyle,
    public val button2: TextStyle,
)
