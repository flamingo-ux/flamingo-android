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

package com.flamingo.playground.components.radiobutton

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.flamingo.components.RadioButton
import com.flamingo.components.Text

@Composable
fun Sample1() {
    Text(text = "We have two radio buttons and only one can be selected")

    var state by remember { mutableStateOf(true) }
    // Note that selectableGroup() is essential to ensure correct accessibility behavior
    Row(Modifier.selectableGroup()) {
        RadioButton(
            selected = state,
            onClick = { state = true }
        )
        RadioButton(
            selected = !state,
            onClick = { state = false }
        )
    }
}
