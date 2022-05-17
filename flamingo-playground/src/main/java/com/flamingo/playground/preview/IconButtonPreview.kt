package com.flamingo.playground.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.Flamingo
import com.flamingo.components.IconButton
import com.flamingo.components.IconButtonColor
import com.flamingo.components.IconButtonIndicator
import com.flamingo.components.IndicatorColor
import com.flamingo.playground.boast
import com.flamingo.playground.utils.Boast
import com.flamingo.view.components.Button
import com.flamingo.view.components.IconButton
import com.flamingo.R as FlamingoR

class IconButtonPreview : ViewPreview<IconButton>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): IconButton {
        val button = IconButton(requireContext())
        button.ds.apply {
            icon = FlamingoR.drawable.ds_ic_aperture
            color = Button.COLOR_PRIMARY
            setOnClickListener { Boast.showText(context, "Click") }
        }
        return button
    }
}

@Composable
@Preview
@Suppress("FunctionNaming")
fun IconButtonComposePreview() = IconButton(
    onClick = boast("Click"),
    icon = Flamingo.icons.Info,
    color = IconButtonColor.PRIMARY,
    indicator = IconButtonIndicator(color = IndicatorColor.WARNING),
    contentDescription = "preview info",
)
