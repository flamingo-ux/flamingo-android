package com.flamingo.playground.components.topappbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.playground.preview.ListItemPreview
import com.flamingo.playground.preview.TopAppBarPreview

@Preview
@Composable
fun LazyColumnScrollingShadowSample() = Column {
    val listState = rememberLazyListState()
    TopAppBarPreview(listState = listState)
    LazyColumn(state = listState) { items(100) { ListItemPreview() } }
}
