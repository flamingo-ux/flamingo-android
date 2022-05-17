package com.flamingo.playground.components.roundedrectwithcutout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.flamingo.Flamingo
import com.flamingo.components.Text
import com.flamingo.demoapi.StatesPlayroomDemo
import com.flamingo.playground.Compose

@StatesPlayroomDemo
class StatesPlayroom : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose(applyDebugColor = false) {
        val rectWidth = remember { mutableStateOf(120.dp) }
        val rectHeight = remember { mutableStateOf(80.dp) }
        val cornerRadius = remember { mutableStateOf(30.dp) }
        val circleRadius = remember { mutableStateOf(6.dp) }
        val cutoutRadiusPadding = remember { mutableStateOf(2.dp) }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, color = Flamingo.colors.outline, RoundedCornerShape(10.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                RoundedRectWithCutoutShapeSample(
                    rectSize = DpSize(rectWidth.value, rectHeight.value),
                    cornerRadius = cornerRadius.value,
                    circleRadius = circleRadius.value,
                    cutoutRadiusPadding = cutoutRadiusPadding.value,
                )
            }

            Spacer(modifier = Modifier.requiredHeight(12.dp))

            Slider("Rectangle width", rectWidth, -5.dp..120.dp)
            Slider("Rectangle height", rectHeight, -5.dp..120.dp)
            Slider("Corner radius", cornerRadius, -5.dp..120.dp)
            Slider("Circle radius", circleRadius, -5.dp..70.dp)
            Slider("Cutout radius padding", cutoutRadiusPadding, 0.dp..100.dp)
        }
    }

    @Composable
    private fun Slider(
        label: String,
        value: MutableState<Dp>,
        valueRange: ClosedRange<Dp>,
    ) = Column {
        Text(text = "$label: ${value.value}", style = Flamingo.typography.body1)
        Slider(
            value = value.value.value,
            valueRange = valueRange.start.value..valueRange.endInclusive.value,
            onValueChange = { value.value = it.dp },
        )
    }
}
