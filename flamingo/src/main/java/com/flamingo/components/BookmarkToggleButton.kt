@file:Suppress(
    "ModifierOrdering",
    "CommentSpacing",
    "NoBlankLineBeforeRbrace",
    "ModifierOrdering",
    "NoConsecutiveBlankLines",
    "MagicNumber",
    "FunctionName",
    "MatchingDeclarationName",
    "LongParameterList",
    "SpacingAroundParens"
)

package com.flamingo.components

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.internalComponents

@FlamingoComponent(
    displayName = "Bookmark Toggle Button",
    preview = "com.flamingo.playground.preview.BookmarkToggleButtonPreview",
    figma = "https://todo.com/file/qVO8jDuABDK9vsuLqRXeMx?node-id=17133%3A133327",
    specification = "https://todo.com/x/nRAjfwE",
    demo = ["com.flamingo.playground.components.bookmarktogglebutton.TypicalUsage"],
    supportsWhiteMode = false,
)
@Composable
public fun BookmarkToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    disabled: Boolean = false,
): Unit = FlamingoComponentBase {
    var isFirst by remember { mutableStateOf(true) }
    val scale = remember { Animatable(1f) }
    LaunchedEffect(checked) {
        if (isFirst) {
            isFirst = false
            return@LaunchedEffect
        }
        scale.animateTo(1.2f)
        scale.animateTo(1f)
    }
    internalComponents.IconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        disabled = disabled,
        checkedIcon = Flamingo.icons.BookmarkFilled,
        uncheckedIcon = Flamingo.icons.Bookmark,
        checkedTint = Flamingo.colors.rating,
        uncheckedTint = Flamingo.colors.textSecondary,
        contentModifier = Modifier.graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        },
    )
}
