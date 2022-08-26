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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.InternalComponents
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.widgetcard.WidgetCardGroup
import com.flamingo.components.widgetcard.WidgetCardGroupScope
import com.flamingo.components.widgetcard.WidgetCardLayoutId
import com.flamingo.components.widgetcard.WidgetCardShape
import com.flamingo.components.widgetcard.WidgetCardSize
import com.flamingo.components.widgetcard.cardSize
import com.flamingo.internalComponents
import com.flamingo.theme.FlamingoIcon

/**
 * Can only be displayed using [WidgetCardGroup].
 * @see WidgetCardGroup
 */
@FlamingoComponent(
    displayName = "Link Card",
    preview = "com.flamingo.playground.preview.LinkCardPreview",
    figma = "https://f.com/file/6qbNsEofr4vu0p8bAGCM65?node-id=24525%3A144708",
    specification = "https://confluence.companyname.ru/x/k6w23wE",
    demo = ["com.flamingo.playground.components.listitem.ListLoadingTypicalUsage"],
    supportsWhiteMode = false,
)
@Composable
public fun WidgetCardGroupScope.LinkCard(
    text: String,
    icon: FlamingoIcon? = null,
    onClick: (() -> Unit)? = null,
): Unit = internalComponents.LinkCard(text, size = null, icon, onClick)

@Composable
public fun InternalComponents.LinkCard(
    text: String,
    size: WidgetCardSize?,
    icon: FlamingoIcon? = null,
    onClick: (() -> Unit)? = null,
): Unit = FlamingoComponentBase {
    Column(
        modifier = Modifier
            .layoutId(WidgetCardLayoutId)
            .run { if (size != null) cardSize(size) else fillMaxSize() }
            .background(Flamingo.colors.backgroundSecondary, WidgetCardShape)
            .clip(WidgetCardShape)
            .run { if (onClick != null) clickable(onClick = onClick) else this },
        verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (icon != null) Icon(icon = icon, tint = Flamingo.colors.primary)
        Text(text = text, style = Flamingo.typography.h6, color = Flamingo.colors.primary)
    }
}
