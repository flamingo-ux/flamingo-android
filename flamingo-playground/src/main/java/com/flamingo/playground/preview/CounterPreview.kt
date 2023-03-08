package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.counter.Counter

@Preview
@Composable
fun CounterPreview() {
    Counter(maxCount = 10, onCountChange = {

    })
}