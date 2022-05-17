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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.annotations.UsedInsteadOf
import com.flamingo.components.listitem.ListItem

/**
 * Used for separating [ListItem]s and visually distinct blocks of content. Color MUST NOT be
 * modified! [modifier] MUST BE used only for modifying width ([vertical] == false) or height
 * ([vertical] == true)
 *
 * @param vertical if true divider will be vertical, else - horizontal (default)
 */
@FlamingoComponent(
    preview = "com.flamingo.playground.preview.DividerPreview",
    figma = "https://todo.com/file/qVO8jDuABDK9vsuLqRXeMx/Friend-UI-kit?node-id=11148%3A115043",
    specification = "https://todo.com/x/6QchMQE",
    demo = ["com.flamingo.playground.components.DividerStatesPlayroom"],
    supportsWhiteMode = true,
)
@UsedInsteadOf("androidx.compose.material.Divider")
@Preview(widthDp = 100, heightDp = 20)
@Composable
public fun Divider(
    modifier: Modifier = Modifier,
    vertical: Boolean = false,
): Unit = FlamingoComponentBase {
    val color = if (Flamingo.isWhiteMode) Flamingo.palette.white else Flamingo.colors.separator
    Box(
        modifier
            .run {
                if (vertical) {
                    fillMaxHeight().requiredWidth(1.dp)
                } else {
                    fillMaxWidth().requiredHeight(1.dp)
                }
            }
            .background(color)
    )
}
