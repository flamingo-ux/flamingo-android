package com.flamingo.components

import com.flamingo.components.button.ButtonWidthPolicy

public data class Action(
    val label: String,
    val onClick: () -> Unit,
    val loading: Boolean = false,
    val disabled: Boolean = false,
    val widthPolicy: ButtonWidthPolicy = ButtonWidthPolicy.MULTILINE,
)

public data class ActionGroup(
    val firstAction: Action,
    val secondAction: Action? = null,
)
