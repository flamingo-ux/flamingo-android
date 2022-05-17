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

package com.flamingo.playground.components.icontogglebutton

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.flamingo.Flamingo
import com.flamingo.components.IconToggleButton
import com.flamingo.playground.Compose
import com.flamingo.demoapi.TypicalUsageDemo
import com.flamingo.playground.internalComponents

@Preview
@Composable
fun Heart() {
    var checked by remember { mutableStateOf(false) }
    internalComponents.IconToggleButton(
        checked = checked,
        onCheckedChange = { checked = !checked },
        disabled = false,
        checkedIcon = Flamingo.icons.HeartFilled,
        uncheckedIcon = Flamingo.icons.Heart,
        checkedTint = Flamingo.colors.error,
        uncheckedTint = Flamingo.colors.textSecondary,
    )
}

@Preview
@Composable
fun Camera() {
    var checked by remember { mutableStateOf(false) }
    internalComponents.IconToggleButton(
        checked = checked,
        onCheckedChange = { checked = !checked },
        disabled = false,
        checkedIcon = Flamingo.icons.Camera,
        uncheckedIcon = Flamingo.icons.CameraOff,
        checkedTint = Flamingo.colors.textSecondary,
        uncheckedTint = Flamingo.colors.textSecondary,
    )
}

@TypicalUsageDemo
class TypicalUsage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Heart()
            Camera()
        }
    }
}
