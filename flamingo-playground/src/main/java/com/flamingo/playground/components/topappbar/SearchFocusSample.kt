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
    "SpacingAroundParens"
)

package com.flamingo.playground.components.topappbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.Flamingo
import com.flamingo.components.topappbar.CenterItem
import com.flamingo.components.topappbar.EdgeItem
import com.flamingo.components.topappbar.TopAppBar

@Preview
@Composable
fun SearchFocusSample() {
    val focusRequester = remember { FocusRequester() }
    var text by remember { mutableStateOf("") }
    TopAppBar(
        start = EdgeItem.IconButton(Flamingo.icons.ArrowLeft) {},
        center = CenterItem.Search(
            context = LocalContext.current,
            value = text,
            onValueChange = { text = it },
            focusRequester = focusRequester
        ),
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
