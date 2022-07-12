package com.flamingo.playground.components.topappbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.playground.preview.ListItemPreview
import com.flamingo.playground.preview.TopAppBarPreview2

@Preview
@Composable
fun ScrollingShadowSample() = Column {
    val state = rememberScrollState()
    TopAppBarPreview2(scrollState = state)
    Column(Modifier.verticalScroll(state)) { repeat(30) { ListItemPreview() } }
}
