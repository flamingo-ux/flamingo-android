@file:Suppress("MagicNumber")

package com.flamingo.playground.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.updateLayoutParams
import com.flamingo.components.Action
import com.flamingo.components.ActionGroup
import com.flamingo.components.AlertMessage
import com.flamingo.components.AlertMessageVariant
import com.flamingo.loremIpsum
import com.flamingo.playground.boast
import com.flamingo.playground.utils.Boast
import com.flamingo.view.components.AlertMessage

class AlertMessagePreview : ViewPreview<AlertMessage>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = AlertMessage(requireContext()).apply {
        ds.setMessage(loremIpsum(10))
        ds.onCloseClick { Boast.showText(context, "Close click") }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.updateLayoutParams<FrameLayout.LayoutParams> {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }
}

@Composable
@Preview
@Suppress("FunctionNaming")
fun AlertMessageComposePreview() {
    AlertMessage(
        text = loremIpsum(10),
        variant = AlertMessageVariant.INFO,
        actions = ActionGroup(
            Action("Button1", onClick = { }),
            Action("Button2", onClick = { })
        ),
        onClose = boast("Close click")
    )
}
