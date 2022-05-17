package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.Flamingo
import com.flamingo.components.Icon

@Composable
@Preview
@Suppress("FunctionNaming")
fun IconPreview() = Icon(icon = Flamingo.icons.Bell)
