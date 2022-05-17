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

package com.theater

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private class TransformationState {
    var rotationX = mutableStateOf(0f)
    var rotationY = mutableStateOf(0f)
    var rotationZ = mutableStateOf(0f)

    var scaleX = mutableStateOf(1f)
    var scaleY = mutableStateOf(1f)

    var translationX = mutableStateOf(0f)
    var translationY = mutableStateOf(0f)

    var alpha = mutableStateOf(1f)
    var transformOrigin = mutableStateOf(TransformOrigin.Center)
    var shadowElevation = mutableStateOf(0f)
    var cameraDistance = mutableStateOf(4f)

    var clip = mutableStateOf(false)
    var shape = mutableStateOf(RectangleShape)
}

private fun Modifier.graphicsLayer(trState: TransformationState) = graphicsLayer {
    rotationX = trState.rotationX.value
    rotationY = trState.rotationY.value
    rotationZ = trState.rotationZ.value

    scaleX = trState.scaleX.value
    scaleY = trState.scaleY.value

    translationX = trState.translationX.value * density
    translationY = trState.translationY.value * density

    alpha = trState.alpha.value
    transformOrigin = trState.transformOrigin.value
    shadowElevation = trState.shadowElevation.value * density
    cameraDistance = trState.cameraDistance.value * density

    clip = trState.clip.value
    shape = trState.shape.value
}

/**
 * 3D studio for creating a [Plot].
 */
public data class Backstage(
    val name: String = "Main",
    val actor: @Composable () -> Actor,
) {
    public constructor(
        name: String = "Main",
        actor: Actor,
    ) : this(name = name, actor = { remember { actor } })

    @Suppress("MemberNameEqualsClassName")
    @Composable
    public fun Backstage(sizeConfig: TheaterPlay.SizeConfig) {
        BackstageUi(actor = actor(), sizeConfig = sizeConfig)
    }
}

/**
 * @see Backstage
 */
@Suppress("LongMethod")
@Composable
private fun BackstageUi(
    actor: Actor,
    sizeConfig: TheaterPlay.SizeConfig,
): Unit = BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
    MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
        val wrongOrientation1 =
            maxWidth > maxHeight && sizeConfig.size.width > sizeConfig.size.height
        val wrongOrientation2 =
            maxWidth < maxHeight && sizeConfig.size.width < sizeConfig.size.height
        if (wrongOrientation1 || wrongOrientation2) {
            Text(
                text = "Пожалуйста, переверните экран \uD83D\uDD04", // 🔄
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
            return@MaterialTheme
        }

        val maxWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val maxHeightPx = with(LocalDensity.current) { maxHeight.toPx() }
        val sizeCoeff = maxWidthPx / sizeConfig.size.width
        val density = Density(sizeConfig.density.density * sizeCoeff, sizeConfig.density.fontScale)
        Column {
            val trState1 = remember { TransformationState() }
            val trState2 = remember { TransformationState() }
            val rotatableState1 = rememberRotatableState(
                maxAngle = 180f, angleX = trState1.rotationX, angleY = trState1.rotationY
            )
            val rotatableState2 = rememberRotatableState(
                maxAngle = 180f, angleX = trState2.rotationX, angleY = trState2.rotationY
            )

            var firstLayer by remember { mutableStateOf(true) }
            var showFirstLayerOutline by remember { mutableStateOf(false) }

            CompositionLocalProvider(LocalDensity provides density) {
                val rotatableState = if (firstLayer) rotatableState1 else rotatableState2
                ActorPreview(
                    rotatableState, trState1, trState2, showFirstLayerOutline, sizeConfig,
                    sizeCoeff, actor
                )
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LabeledCheckbox("First layer", firstLayer) { firstLayer = it }
                    LabeledCheckbox("First layer outline", showFirstLayerOutline) {
                        showFirstLayerOutline = it
                    }

                    val clipboardManager = LocalClipboardManager.current
                    val context = LocalContext.current
                    Button(onClick = {
                        val state = if (firstLayer) trState1 else trState2
                        state.copyStateToClipboard(firstLayer, clipboardManager)
                        Toast.makeText(context, "Copied in the clipboard", Toast.LENGTH_SHORT)
                            .show()
                    }) {
                        Text(text = "Copy values", color = LocalContentColor.current)
                    }
                }

                TweakPanel(if (firstLayer) trState1 else trState2, maxWidthPx, maxHeightPx)
            }
        }
    }
}

private fun TransformationState.copyStateToClipboard(
    isFirstLayer: Boolean,
    clipboardManager: ClipboardManager,
) {
    val varName = if (isFirstLayer) "layer0" else "layer1"
    val sourceCode = """
        $varName.animateTo(
            rotationX = ${rotationX.value.toSourceCode()},
            rotationY = ${rotationY.value.toSourceCode()},
            rotationZ = ${rotationZ.value.toSourceCode()},
            scaleX = ${scaleX.value.toSourceCode()},
            scaleY = ${scaleY.value.toSourceCode()},
            translationX = ${translationX.value.toSourceCode()},
            translationY = ${translationY.value.toSourceCode()},
            shadowElevation = ${shadowElevation.value.toSourceCode()},
            cameraDistance = ${cameraDistance.value.toSourceCode()},
        )
    """.trimIndent()

    clipboardManager.setText(AnnotatedString(sourceCode))
}

private fun TransformOrigin.toSourceCode(): String {
    val x = pivotFractionX.toSourceCode()
    val y = pivotFractionY.toSourceCode()
    return """
            TransformOrigin($x, $y)
    """.trimIndent()
}

private fun Float.toSourceCode(): String {
    val s = toString().removeSuffix(".0") + "f"
    return if (s == "-0f") "0f" else s
}

@Composable
private fun ActorPreview(
    rotatableState: RotatableState,
    trState1: TransformationState,
    trState2: TransformationState,
    showFirstLayerOutline: Boolean,
    sizeConfig: TheaterPlay.SizeConfig,
    sizeCoeff: Float,
    actor: Actor,
) {
    val actorScope = remember {
        object : ActorScope {
            override val theaterPackage: TheaterPackage? = null
            override val sizeConfig: TheaterPlay.SizeConfig = sizeConfig
        }
    }
    val width = with(LocalDensity.current) { (sizeConfig.size.width * sizeCoeff).toDp() }
    val height = with(LocalDensity.current) { (sizeConfig.size.height * sizeCoeff).toDp() }
    Box(
        modifier = Modifier
            .requiredSize(width, height)
            .border(if (LocalInspectionMode.current) 1.dp else Dp.Hairline, Color.Red)
            .rotatable(rotatableState)
            .clip(RectangleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.requiredSize(
                width = width * sizeConfig.stageSizeMultiplier,
                height = height * sizeConfig.stageSizeMultiplier
            ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer(trState1)
                    .run { if (showFirstLayerOutline) border(Dp.Hairline, Color.Red) else this }
            ) {
                Box(modifier = Modifier.graphicsLayer(trState2)) {
                    with(actor) { actorScope.Actor() }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.TweakPanel(
    trState: TransformationState,
    maxWidthPx: Float,
    maxHeightPx: Float,
) {
    val density = LocalDensity.current
    val densityVal = LocalDensity.current.density
    val maxWidthDp = with(density) { maxWidthPx.toDp().value }
    val maxHeightDp = with(density) { maxHeightPx.toDp().value }

    Rotation(trState)
    val widthRange =
        (-maxWidthDp * TRANSLATION_RANGE_MULTIPLIER)..(maxWidthDp * TRANSLATION_RANGE_MULTIPLIER)
    val heightRange =
        (-maxHeightDp * TRANSLATION_RANGE_MULTIPLIER)..(maxHeightDp * TRANSLATION_RANGE_MULTIPLIER)
    ParamTweaker("translationX", widthRange, trState.translationX)
    ParamTweaker("translationY", heightRange, trState.translationY)
    ScaleTweaker(trState)
    ParamTweaker("cameraDistance", 0f..(densityVal * 15), trState.cameraDistance, { it.value = 4f })
    ParamTweaker("shadowElevation", 0f..(densityVal * 20), trState.shadowElevation)
}

private const val TRANSLATION_RANGE_MULTIPLIER = 1.4f

@Composable
private fun ColumnScope.Rotation(trState: TransformationState) {
    ParamTweaker(
        name = "rotationX", valueRange = ROTATION_RANGE, state = trState.rotationX,
        onValueChange = { trState.rotationX.value = it.roundToInt().toFloat() }
    )
    ParamTweaker(
        name = "rotationY", valueRange = ROTATION_RANGE, state = trState.rotationY,
        onValueChange = { trState.rotationY.value = it.roundToInt().toFloat() }
    )
    ParamTweaker(
        name = "rotationZ", valueRange = ROTATION_RANGE, state = trState.rotationZ,
        onValueChange = { trState.rotationZ.value = it.roundToInt().toFloat() }
    )
}

private val ROTATION_RANGE = -180f..180f

@Composable
private fun ScaleTweaker(trState: TransformationState) {
    var separateScale by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (separateScale) Column {
                ParamTweaker("scaleX", SCALE_RANGE, trState.scaleX, { it.value = 1f })
                ParamTweaker("scaleY", SCALE_RANGE, trState.scaleY, { it.value = 1f })
            } else ParamTweaker(
                name = "scale",
                valueRange = SCALE_RANGE,
                state = trState.scaleX,
                onReset = {
                    trState.scaleX.value = 1f
                    trState.scaleY.value = 1f
                },
                onValueChange = {
                    trState.scaleX.value = it
                    trState.scaleY.value = it
                }
            )
        }
        LabeledCheckbox("Раздельно", separateScale) { separateScale = it }
    }
}

private val SCALE_RANGE = 0f..10f

@Composable
private fun LabeledCheckbox(
    label: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
) = Row(
    modifier = Modifier.clickable { onValueChange(!value) },
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    Text(text = label)
    Checkbox(checked = value, onCheckedChange = null)
}

@Composable
private fun ParamTweaker(
    name: String,
    valueRange: ClosedFloatingPointRange<Float>,
    state: MutableState<Float>,
    onReset: (MutableState<Float>) -> Unit = { it.value = 0f },
    onValueChange: (Float) -> Unit = { state.value = it },
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            val value by state
            Text(text = "$name: $value")
            Slider(
                modifier = Modifier.requiredHeight(30.dp),
                value = value, valueRange = valueRange, onValueChange = onValueChange
            )
        }
        Button(onClick = { onReset(state) }) {
            Text(text = "Reset", color = LocalContentColor.current)
        }
    }
}
