package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.rating.Rating
import com.flamingo.components.rating.RatingSize

@Preview
@Composable
fun RatingPreview() {
    var state by remember { mutableStateOf(0u) }
    Rating(
        value = state,
        onSelected = { state = it },
        size = RatingSize.LARGE,
    )
}
