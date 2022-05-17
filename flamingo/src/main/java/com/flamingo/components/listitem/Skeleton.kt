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
    "LongMethod",
    "ComplexMethod",
    "SpacingAroundParens"
)

package com.flamingo.components.listitem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flamingo.annotations.FlamingoComponent
import com.flamingo.components.Avatar
import com.flamingo.components.AvatarShape
import com.flamingo.components.AvatarSize
import com.flamingo.components.FlamingoComponentBase
import com.flamingo.components.Skeleton
import com.flamingo.components.SkeletonShape
import com.flamingo.components.listitem.TextBlocksSkeleton.TWO
import com.flamingo.Flamingo

@Suppress("MaxLineLength")
/**
 * Used to show loading of the [ListItem]s.
 * If there are more than two non-null params from the following list set in a [ListItem]
 * - `title`
 * - `subtitle`
 * - `description`
 *
 * [TextBlocksSkeleton.TWO] SHOULD BE used, ignoring other params.
 *
 * @param avatar SHOULD BE non-null only if [ListItem]'s start slot contains [Avatar]
 *
 * @sample com.flamingo.playground.components.listitem.ListLoadingTypicalUsage.Content no preview
 */
@FlamingoComponent(
    displayName = "List Item Skeleton",
    preview = "com.flamingo.playground.preview.ListItemSkeletonPreview",
    figma = "https://todo.com/file/qVO8jDuABDK9vsuLqRXeMx/3.-Friend-UI-Components?node-id=" +
            "11850%3A130044",
    specification = "https://todo.com/display/FLAMINGO/ListItem#ListItem-Loading",
    demo = [
        "com.flamingo.playground.components.ListItemSkeletonStatesPlayroom",
        "com.flamingo.playground.components.listitem.ListLoadingTypicalUsage",
    ],
    supportsWhiteMode = true,
)
@Composable
public fun ListItemSkeleton(
    avatar: AvatarSkeleton? = null,
    textBlocks: TextBlocksSkeleton,
): Unit = FlamingoComponentBase {
    AdvancedListItem(
        start = if (avatar != null) {
            {
                Skeleton(
                    modifier = Modifier.requiredSize(avatar.avatarSize.avatar),
                    shape = SkeletonShape.AnyShape(avatar.avatarShape.shape())
                )
            }
        } else null,
        title = {
            Skeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .run { if (textBlocks == TWO) padding(bottom = 4.dp) else this },
                shape = SkeletonShape.Text(textStyle = Flamingo.typography.caption)
            )
        },
        subtitle = if (textBlocks == TWO) {
            {
                Skeleton(
                    modifier = Modifier.fillMaxWidth(0.65f),
                    shape = SkeletonShape.Text(textStyle = Flamingo.typography.caption)
                )
            }
        } else null,
        showDivider = false,
    )
}

public data class AvatarSkeleton(
    val avatarSize: AvatarSize,
    val avatarShape: AvatarShape,
)

public enum class TextBlocksSkeleton { ONE, TWO, }
