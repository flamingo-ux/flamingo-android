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
import kotlinx.coroutines.launch
import com.flamingo.Flamingo
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.internalComponents

/**
 * @param sameColor if true, an icon will have the same tint in checked and unchecked states, else –
 * different ones
 */
@FlamingoComponent(
    displayName = "Rating Toggle Button",
    preview = "com.flamingo.playground.preview.RatingToggleButtonPreview",
    figma = "https://f.com/file/qVO8jDuABDK9vsuLqRXeMx?node-id=17133%3A133328",
    specification = "https://confluence.companyname.ru/x/nRAjfwE",
    demo = [
        "com.flamingo.playground.components.ratingtogglebutton.StatesPlayroom",
        "com.flamingo.playground.components.ratingtogglebutton.TypicalUsage",
    ],
    supportsWhiteMode = false,
)
@Composable
public fun RatingToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    sameColor: Boolean = false,
    disabled: Boolean = false,
): Unit = FlamingoComponentBase {
    val starSymmetries = 5f
    var isFirst by remember { mutableStateOf(true) }
    val rotation = remember { Animatable(1f) }
    val scale = remember { Animatable(1f) }
    LaunchedEffect(checked) {
        if (isFirst) {
            isFirst = false
            return@LaunchedEffect
        }
        if (checked) launch { rotation.animateTo(rotation.targetValue + 360f / starSymmetries) }
        scale.animateTo(1.2f)
        scale.animateTo(1f)
    }
    internalComponents.IconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        disabled = disabled,
        checkedIcon = Flamingo.icons.StarFilled,
        uncheckedIcon = Flamingo.icons.Star,
        checkedTint = Flamingo.colors.rating,
        uncheckedTint = Flamingo.colors.run { if (sameColor) rating else textSecondary },
        contentModifier = Modifier.graphicsLayer {
            rotationZ = rotation.value
            scaleX = scale.value
            scaleY = scale.value
        },
    )
}
