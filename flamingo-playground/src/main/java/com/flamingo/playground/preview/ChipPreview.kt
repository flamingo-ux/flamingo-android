package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.Chip
import com.flamingo.playground.boast

@Preview
@Composable
@Suppress("FunctionNaming")
fun ChipPreview() = Chip(
    label = "Chip",
    onClick = boast(msg = "onClick"),
    onDelete = boast(msg = "onDelete"),
)
