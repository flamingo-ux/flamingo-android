package com.flamingo.utils

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp

@Stable
public inline operator fun Dp.div(other: UInt): Dp = Dp(value = value / other.toInt())

@Stable
public inline operator fun Dp.times(other: UInt): Dp = Dp(value = value * other.toInt())
