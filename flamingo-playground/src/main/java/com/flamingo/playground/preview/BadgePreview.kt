package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.Badge
import com.flamingo.components.BadgeColor

@Preview
@Composable
@Suppress("FunctionNaming")
fun BadgePreview() = Badge(label = "Badge", color = BadgeColor.Gradient.RED)
