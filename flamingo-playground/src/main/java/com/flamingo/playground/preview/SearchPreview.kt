@file:Suppress("MagicNumber")

package com.flamingo.playground.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.Search
import com.flamingo.playground.internalComponents

@Preview
@Composable
@Suppress("FunctionNaming")
fun SearchPreview() {
    var value by rememberSaveable { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxWidth()) {
        internalComponents.Search(
            value = value,
            onValueChange = { value = it },
        )
    }
}
