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

package com.flamingo.playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flamingo.Flamingo
import com.flamingo.Shadow
import com.flamingo.components.Checkbox
import com.flamingo.components.Switch
import com.flamingo.components.Text
import com.flamingo.components.TextField
import com.flamingo.components.listitem.ListItem
import com.flamingo.utils.updateMargins
import com.flamingo.utils.UnitConversions.dp as oldDp

class ShadowLabViewModel : ViewModel() {
    var elevation by mutableStateOf(8f)
    var alpha by mutableStateOf(1f)

    var elevationText by mutableStateOf(elevation.toString())
    var alphaText by mutableStateOf(alpha.toString())

    /**
     * Switches between [Slider] and [TextField] ([preciseMode] = true) inputs
     */
    var preciseMode by mutableStateOf(false)

    /**
     * Compose or android shadow implementation
     */
    var composeShadows by mutableStateOf(true)
}

class ComposeShadowLabFragment : Fragment() {
    private val cardCornerRadiusDp = 12f
    private val cardSizeDp = 100
    private lateinit var cardView: CardView
    private val cardShape = RoundedCornerShape(cardCornerRadiusDp.dp)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose { Content() }

    @Preview
    @Composable
    private fun Content() {
        val viewModel = viewModel<ShadowLabViewModel>()

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShadowPreview(viewModel)
            if (viewModel.preciseMode) PreciseControls(viewModel) else RoughControls(viewModel)

            ListItem(
                title = stringResource(R.string.lab_compose_shadow_exact_values),
                end = { Checkbox(checked = viewModel.preciseMode, onCheckedChange = null) },
                onClick = { viewModel.preciseMode = !viewModel.preciseMode },
            )
            ListItem(
                title = "UI framework",
                subtitle = if (viewModel.composeShadows) "Jetpack Compose" else "Android View",
                end = { Switch(checked = viewModel.composeShadows, onCheckedChange = null) },
                onClick = { viewModel.composeShadows = !viewModel.composeShadows },
                showDivider = false,
            )
        }
    }

    @Composable
    private fun ShadowPreview(viewModel: ShadowLabViewModel) {
        val backgroundColor = Flamingo.colors.primary
        if (viewModel.composeShadows) {
            internalComponents.Shadow(
                modifier = Modifier.padding(top = 30.dp, bottom = 80.dp),
                elevation = viewModel.elevation.dp,
                opacity = viewModel.alpha,
                shape = cardShape
            ) {
                Box(
                    modifier = Modifier
                        .requiredSize(cardSizeDp.dp)
                        .background(backgroundColor, cardShape)
                        .clip(cardShape)
                )
            }
        } else {
            AndroidView(factory = {
                cardView = CardView(it).apply {
                    setCardBackgroundColor(backgroundColor.toArgb())
                    radius = cardCornerRadiusDp.oldDp
                }
                FrameLayout(it).apply { addView(cardView) }
            }, update = {
                cardView.apply {
                    if (marginBottom == 0) {
                        updateLayoutParams {
                            width = cardSizeDp.oldDp
                            height = cardSizeDp.oldDp
                        }
                        updateMargins(80.oldDp, 30.oldDp, 80.oldDp, 80.oldDp)
                    }
                    cardElevation = viewModel.elevation.oldDp
                }
            })
        }
    }

    @Composable
    private fun RoughControls(viewModel: ShadowLabViewModel) {
        MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
            Text(text = "Elevation: ${viewModel.elevation} dp")
            Slider(
                value = viewModel.elevation,
                onValueChange = {
                    viewModel.elevation = it
                    viewModel.elevationText = it.toString()
                },
                valueRange = 0f..50f,
            )

            Spacer(modifier = Modifier.requiredHeight(8.dp))

            if (viewModel.composeShadows) {
                Text(text = "Alpha: ${viewModel.alpha}")
                Slider(
                    value = viewModel.alpha,
                    onValueChange = {
                        viewModel.alpha = it
                        viewModel.alphaText = it.toString()
                    },
                    valueRange = 0f..1f,
                )
            }
        }
    }

    @Composable
    private fun PreciseControls(viewModel: ShadowLabViewModel) {
        TextField(
            label = "Elevation (dp)",
            value = viewModel.elevationText,
            error = !validateElevation(viewModel.elevationText),
            errorText = stringResource(R.string.lab_compose_shadow_wrong_elevation),
            onValueChange = {
                viewModel.elevationText = it
                val new = if (validateElevation(it)) it.toFloat() else return@TextField
                viewModel.elevation = new
            },
        )

        if (viewModel.composeShadows) {
            TextField(
                label = "Alpha",
                value = viewModel.alphaText,
                error = !validateAlpha(viewModel.alphaText),
                errorText = stringResource(R.string.lab_compose_shadow_wrong_alpha),
                onValueChange = {
                    viewModel.alphaText = it
                    val new = if (validateAlpha(it)) it.toFloat() else return@TextField
                    viewModel.alpha = new
                },
            )
        }
    }

    private fun validateElevation(text: String): Boolean {
        val float = text.toFloatOrNull()
        return float != null && float >= 0
    }

    private fun validateAlpha(text: String): Boolean {
        val float = text.toFloatOrNull()
        return float != null && float in 0f..1f
    }
}
