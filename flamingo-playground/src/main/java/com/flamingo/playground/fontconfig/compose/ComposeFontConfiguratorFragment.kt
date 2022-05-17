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

package com.flamingo.playground.fontconfig.compose

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.flamingo.Flamingo
import com.flamingo.components.Icon
import com.flamingo.components.Text
import com.flamingo.playground.Compose
import com.flamingo.playground.R
import com.flamingo.playground.isEmulator
import com.flamingo.playground.utils.Boast

class ComposeFontConfiguratorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose {
        val typography = Typography(
            h1 = TextStyle(color = Color.Black),
            h2 = TextStyle(color = Color.Black),
            h3 = TextStyle(color = Color.Black),
            h4 = TextStyle(color = Color.Black),
            h5 = TextStyle(color = Color.Black),
            h6 = TextStyle(color = Color.Black),
            subtitle1 = TextStyle(color = Color.Black),
            subtitle2 = TextStyle(color = Color.Black),
            body1 = TextStyle(color = Color.Black),
            body2 = TextStyle(color = Color.Black),
            button = TextStyle(color = Color.Black),
            caption = TextStyle(color = Color.Black),
            overline = TextStyle(color = Color.Black),
        )
        MaterialTheme(
            colors = if (isSystemInDarkTheme()) darkColors() else lightColors(),
            typography = typography
        ) {
            FontConfiguratorContent()
        }
    }
}

@Preview(device = Devices.PIXEL_2)
@Composable
private fun FontConfiguratorContent() {
    Box {
        val config by rememberSaveable(stateSaver = Config.stateSaver) { mutableStateOf(Config()) }

        Row(modifier = Modifier.padding(8.dp)) {
            Box(modifier = Modifier.weight(6f)) { ParamsEditor(config) }
            Box(
                modifier = Modifier
                    .weight(5f)
                    .padding(start = 4.dp)
                    .border(Dp.Hairline, Color.Red)
            ) {
                FontPreview(config)
            }
        }

        val clipboardManager = LocalClipboardManager.current
        val ctx = LocalContext.current
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            onClick = { exportConfig(config, clipboardManager, ctx) }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    icon = Flamingo.icons.ExternalLink,
                    tint = Color.White,
                    contentDescription = "export font config"
                )
                Text(text = "Export", color = Color.White)
            }
        }
    }
}

@Composable
private fun FontPreview(config: Config) = with(config) {
    Text(
        modifier = Modifier
            .padding(
                top = paddingTop.value.toDp(),
                bottom = paddingBottom.value.toDp()
            )
            .paddingFromBaseline(
                top = paddingFromBaselineTop.value.toDp(),
                bottom = paddingFromBaselineBottom.value.toDp(),
            ),
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontStyle = fontStyle,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight,
    )
}

private fun exportConfig(config: Config, clipboardManager: ClipboardManager, ctx: Context) {
    clipboardManager.setText(AnnotatedString(config.toString()))
    val copiedMsg = ctx.getString(R.string.lab_compose_fonts_configurator_config_copied_msg)
    val emulatorMsg =
        ctx.getString(R.string.lab_compose_fonts_configurator_config_copied_in_emulator_msg)
    Boast.showText(ctx, "$copiedMsg${if (isEmulator()) emulatorMsg else ""}")
}
