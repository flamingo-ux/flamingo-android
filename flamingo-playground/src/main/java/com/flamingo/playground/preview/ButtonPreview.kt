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
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.playground.boast
import com.flamingo.playground.utils.Boast
import com.flamingo.view.components.Button
import com.flamingo.R as FlamingoR

class ButtonPreview : ViewPreview<Button>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Button {
        val button = Button(requireContext())
        button.ds.apply {
            label = "Button"
            icon = FlamingoR.drawable.ds_ic_aperture
            color = Button.COLOR_PRIMARY
            setOnClickListener { Boast.showText(context, "Click") }
        }
        return button
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.updateLayoutParams<FrameLayout.LayoutParams> {
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }
    }
}

@Composable
@Preview
@Suppress("FunctionNaming")
fun ButtonComposePreview() {
    Button(
        onClick = boast("Click"),
        label = "Button",
        loading = false,
        color = ButtonColor.Primary,
        widthPolicy = ButtonWidthPolicy.STRICT,
        icon = Flamingo.icons.Briefcase,
    )
}
