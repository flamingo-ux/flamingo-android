@file:Suppress(
    "LongParameterList",
    "SpacingAroundOperators",
    "FunctionNaming",
    "MagicNumber",
)

package com.flamingo.components.tabrow

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo

@Composable
internal fun Tab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: TabVariant = TabVariant.Contained,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) = Row(
    modifier = modifier
        .selectable(
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            role = Role.Tab,
            interactionSource = interactionSource,
            indication = rememberRipple()
        )
        .fillMaxWidth()
        .run {
            if (variant == TabVariant.Contained && !selected) {
                this.border(1.dp, Flamingo.colors.outlineDark, CircleShape)
            } else {
                this
            }
        }
        .padding(
            horizontal = if (variant == TabVariant.Contained) 12.dp else 0.dp,
            vertical = if (variant == TabVariant.Contained) 4.dp else 12.dp
        ),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
    content = content
)
