package com.flamingo.playground.preview

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.updateLayoutParams
import com.flamingo.Flamingo
import com.flamingo.components.Action
import com.flamingo.components.ActionGroup
import com.flamingo.components.EmptyState
import com.flamingo.playground.boast
import com.flamingo.loremIpsum
import com.flamingo.view.components.EmptyState

class EmptyStatePreview : ViewPreview<EmptyState>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): EmptyState {
        val emptyState = EmptyState(requireContext())
        emptyState.ds.apply {
            title = "Title"
            description = "Description, " + @Suppress("MagicNumber") loremIpsum(10)
            actions = ActionGroup(Action(label = "Button text", onClick = {}))
            imageType = EmptyState.IMAGE_EMPTY
        }
        return emptyState
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.updateLayoutParams<FrameLayout.LayoutParams> {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }
    }
}

@Composable
@Preview
@Suppress("FunctionNaming", "MagicNumber")
fun EmptyStateComposePreview() = EmptyState(
    icon = Flamingo.icons.Archive,
    title = loremIpsum(3),
    description = loremIpsum(8),
    actions = ActionGroup(Action("Button", onClick = boast("Action click"))),
    contentDescription = "demo"
)
