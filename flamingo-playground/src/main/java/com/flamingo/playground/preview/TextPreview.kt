package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.Text
import com.flamingo.loremIpsum

@Composable
@Preview
@Suppress("FunctionNaming", "MagicNumber")
fun TextPreview() = Text(loremIpsum(15), textAlign = TextAlign.Center)
