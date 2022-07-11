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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.flamingo.Flamingo
import com.flamingo.overlay.DebugOverlay
import com.flamingo.overlay.componentFunName
import com.flamingo.overlay.currentFunctionName
import com.flamingo.overlay.debugOverlayConfigForComponent
import com.flamingo.uiTestingTag

/**
 * All flamingo components MUST BE implemented in [content] of this function.
 *
 * @param uiTestingTag if null, and [Flamingo.uiTestingTagsEnabled] is true, short fun name is used
 * as tag. if [uiTestingTag] is non-null, it is used as tag.
 *
 * @param drawOverlay if false, you MUST manually draw [DebugOverlay.Config] into your component.
 * Default implementation is:
 *
 * @sample com.flamingo.playground.overlay.DebugOverlayImpl.DebugOverlay
 *
 * @param drawOverlayText if false, you MUST manually draw [DebugOverlay.Config.text] into your
 * component
 *
 * @param componentFunName qualified name of the flamingo component's function, for example:
 * `com.flamingo.components.RadioButton`
 */
@Composable
internal fun FlamingoComponentBase(
    uiTestingTag: String? = null,
    drawOverlay: Boolean = true,
    drawOverlayText: Boolean = true,
    componentFunName: String,
    content: @Composable () -> Unit
): Unit = FlamingoComponentBaseInternal(
    uiTestingTag = uiTestingTag ?: if (Flamingo.uiTestingTagsEnabled) {
        remember(componentFunName) { componentFunName.substringAfterLast('.') }
    } else null,
    drawOverlay = drawOverlay,
    drawOverlayText = drawOverlayText,
    componentFunName = componentFunName,
    content = content,
)

/**
 * All flamingo components MUST BE implemented in [content] of this function.
 *
 * Differs from other overload: automatic function name acquisition (see [currentFunctionName]) is
 * used. Because of it, [FlamingoComponentBase] MUST BE called in the body of the component's
 * function, without nesting into other functions. If there is a need to call
 * [FlamingoComponentBase] from other function, you MUST use other overload.
 *
 * Function is `internal` because of the instability of [currentFunctionName].
 *
 * @param uiTestingTag if null, and [Flamingo.uiTestingTagsEnabled] is true, short fun name is used
 * as tag. if [uiTestingTag] is non-null, it is used as tag.
 *
 * @param drawOverlay if false, you MUST manually draw [DebugOverlay.Config] into your component.
 * Default implementation is:
 *
 * @sample com.flamingo.playground.overlay.DebugOverlayImpl.DebugOverlay
 *
 * @param drawOverlayText if false, you MUST manually draw [DebugOverlay.Config.text] into your
 * component
 */
@Composable
internal fun FlamingoComponentBase(
    uiTestingTag: String? = null,
    drawOverlay: Boolean = true,
    drawOverlayText: Boolean = true,
    content: @Composable () -> Unit
) = FlamingoComponentBaseInternal(
    uiTestingTag = uiTestingTag,
    drawOverlay = drawOverlay,
    drawOverlayText = drawOverlayText,
    componentFunName = null,
    content = content,
)

@Composable
private fun FlamingoComponentBaseInternal(
    uiTestingTag: String? = null,
    drawOverlay: Boolean = true,
    drawOverlayText: Boolean = true,
    componentFunName: String? = null,
    content: @Composable () -> Unit
) {
    Flamingo.checkFlamingoPresence()

    val uiTestingTag = if (Flamingo.uiTestingTagsEnabled) {
        uiTestingTag ?: remember { componentFunName(callDepth = 2).substringAfterLast('.') }
    } else ""

    if (!drawOverlay) {
        UiTestingTagWrapper(uiTestingTag, content)
        return
    }

    val overlayConfig = debugOverlayConfig(componentFunName)

    if (overlayConfig == null) {
        UiTestingTagWrapper(uiTestingTag, content)
        return
    }

    Box(modifier = Modifier.wrapContentSize(), propagateMinConstraints = true) {
        UiTestingTagWrapper(uiTestingTag, content)
        (Flamingo.LocalDebugOverlay.current ?: return@Box).DebugOverlay(
            modifier = Modifier.matchParentSize(),
            config = overlayConfig,
            drawText = drawOverlayText
        )
    }
}

@Composable
internal fun UiTestingTagWrapper(tag: String, content: @Composable () -> Unit) {
    if (Flamingo.uiTestingTagsEnabled) {
        Box(modifier = Modifier.uiTestingTag(tag), propagateMinConstraints = true) { content() }
    } else {
        content()
    }
}

@Composable
private fun debugOverlayConfig(componentFunName: String? = null): DebugOverlay.Config? {
    return if (componentFunName == null) {
        debugOverlayConfigForComponent(callDepth = 3)
    } else {
        Flamingo.debugOverlayConfigForComponent(componentFunName)
    }
}
