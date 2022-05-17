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
    "SpacingAroundParens",
)

package com.flamingo.playground.fontconfig.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.flamingo.annotations.UseNonFlamingoComponent
import com.flamingo.components.Text

private const val MATERIAL_EXPLANATION = "this ui is built using material ui"

@UseNonFlamingoComponent(MATERIAL_EXPLANATION)
@Composable
internal fun ParamsEditor(config: Config) = with(config) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            label = { Text(text = "Text") }
        )
        StatefulTextField(
            initialValue = remember { fontSize.value.toString().removeSuffix(".0") },
            label = "fontSize(sp)",
            onValueChange = { fontSize = it.toFloat().sp },
            isError = { it.toFloatOrNull() == null }
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Italic FontStyle")
            Switch(
                checked = fontStyle == FontStyle.Italic,
                onCheckedChange = { fontStyle = if (it) FontStyle.Italic else FontStyle.Normal }
            )
        }
        StatefulTextField(
            initialValue = remember { fontWeight.weight.toString().removeSuffix(".0") },
            label = "fontWeight(sp)",
            onValueChange = { fontWeight = FontWeight(it.toInt()) },
            isError = { it.toIntOrNull() !in 1..1000 }
        )
        StatefulTextField(
            initialValue = remember { letterSpacing.value.toString().removeSuffix(".0") },
            label = "letterSpacing(em)",
            onValueChange = { letterSpacing = it.toFloat().em },
            isError = { it.toFloatOrNull() == null }
        )
        StatefulTextField(
            initialValue = remember { lineHeight.value.toString().removeSuffix(".0") },
            label = "lineHeight(sp)",
            onValueChange = { lineHeight = it.toFloat().sp },
            isError = { it.toFloatOrNull() == null }
        )

        DimensionTextField("paddingTop", config.paddingTop)
        DimensionTextField("paddingBottom", config.paddingBottom)
        DimensionTextField("paddingFromBaselineTop", config.paddingFromBaselineTop)
        DimensionTextField("paddingFromBaselineBottom", config.paddingFromBaselineBottom)
    }
}

@UseNonFlamingoComponent(MATERIAL_EXPLANATION)
@Composable
private fun StatefulTextField(
    initialValue: String,
    label: String,
    onValueChange: (String) -> Unit,
    isError: (String) -> Boolean,
) {
    var text by remember { mutableStateOf(initialValue) }
    OutlinedTextField(
        value = text,
        onValueChange = { text = it; if (!isError(it)) onValueChange(it) },
        isError = isError(text),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        label = { Text(text = label) }
    )
}

@UseNonFlamingoComponent(MATERIAL_EXPLANATION)
@Composable
private fun DimensionTextField(
    label: String,
    dimension: MutableState<Dimension>,
) {
    Column(
        modifier = Modifier.padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StatefulTextField(
            initialValue = dimension.value.value.toString().removeSuffix(".0"),
            label = "$label(${if (dimension.value.isDp) "dp" else "sp"})",
            onValueChange = {
                val float = it.toFloatOrNull() ?: return@StatefulTextField
                dimension.value = dimension.value.copy(value = float)
            },
            isError = { it.toFloatOrNull() == null }
        )

        Switch(checked = dimension.value.isDp, onCheckedChange = {
            dimension.value = dimension.value.copy(isDp = it)
        })
    }
}
