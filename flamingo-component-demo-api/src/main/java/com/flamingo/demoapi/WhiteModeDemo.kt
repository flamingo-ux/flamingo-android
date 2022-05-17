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

package com.flamingo.demoapi

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.R as FlamingoR

/**
 * Used to demonstrate how [FlamingoComponent] looks in [Flamingo.LocalWhiteMode]. If [white] is
 * true:
 * 1. sets [Flamingo.LocalWhiteMode] = true
 * 2. sets gradient background under [content]
 * 3. applies padding between gradient and [content]
 */
@Composable
public fun WhiteModeDemo(
    modifier: Modifier = Modifier,
    white: Boolean,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(Flamingo.LocalWhiteMode provides white) {
        Box(modifier, contentAlignment = Alignment.Center) {
            if (white) Image(
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = FlamingoR.drawable.ds_gradient_purple),
                contentDescription = null
            )
            val padding by animateDpAsState(targetValue = if (white) 8.dp else 0.dp)
            Box(
                modifier = Modifier.padding(padding),
                contentAlignment = Alignment.Center
            ) { content() }
        }
    }
}
