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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.Flamingo
import com.flamingo.components.topappbar.CenterItem
import com.flamingo.components.topappbar.EdgeItem
import com.flamingo.components.topappbar.TopAppBar
import com.flamingo.playground.preview.ListItemPreview
import com.flamingo.playground.preview.TopAppBarPreview
import com.flamingo.playground.preview.TopAppBarPreview2

@Preview
@Composable
fun LazyColumnScrollingShadowSample() = Column {
    val listState = rememberLazyListState()
    TopAppBarPreview(listState = listState)
    LazyColumn(state = listState) { items(100) { ListItemPreview() } }
}

@Preview
@Composable
fun ScrollingShadowSample() = Column {
    val state = rememberScrollState()
    TopAppBarPreview2(scrollState = state)
    Column(Modifier.verticalScroll(state)) { repeat(30) { ListItemPreview() } }
}

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
