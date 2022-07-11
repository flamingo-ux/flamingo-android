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
import com.flamingo.components.Indicator
import com.flamingo.components.IndicatorColor
import com.flamingo.components.IndicatorSize
import com.flamingo.view.components.Indicator

class IndicatorPreview : ViewPreview<Indicator>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Indicator(requireContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.updateLayoutParams<FrameLayout.LayoutParams> { gravity = Gravity.CENTER }
    }
}

@Preview
@Composable
@Suppress("FunctionNaming")
fun IndicatorComposePreview() = Indicator(size = IndicatorSize.BIG, color = IndicatorColor.PRIMARY)
