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
    "SpacingAroundParens"
)

package com.flamingo.playground.components.button

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo
import com.flamingo.components.Text
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.loremIpsum
import com.flamingo.playground.boast

private val names = (1..15).map { "Name" + it }
private val loremIpsum20 = loremIpsum(20)

@Composable
private fun ButtonInRow(label: String) = Button(
    onClick = boast("Click"),
    label = label,
    widthPolicy = ButtonWidthPolicy.STRICT,
)

@Composable
private fun ScrollableRow(content: @Composable RowScope.() -> Unit) = Row(
    modifier = Modifier.horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    content = content,
)

@Composable
private fun Section(title: String, content: @Composable () -> Unit) = Column {
    Text(text = title, modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 4.dp))
    content()
}


@Preview
@Composable
fun ButtonsInRow() = Section("Button Row") {
    ScrollableRow {
        names.forEach { name -> ButtonInRow(name) }
    }
}

@Preview
@Composable
fun Squeezed() = Section("Button Row, restricted width") {
    ScrollableRow {
        names.forEach { name ->
            Box(modifier = Modifier.requiredWidth(40.dp)) {
                ButtonInRow(name)
            }
        }
    }
}

@Preview
@Composable
fun SqueezedAndClipped() = Section("Button Row, restricted width and clipped") {
    ScrollableRow {
        names.forEach { name ->
            Box(
                modifier = Modifier
                    .requiredWidth(40.dp)
                    .clip(RectangleShape)
            ) {
                ButtonInRow(name)
            }
        }
    }
}

@Preview
@Composable
fun Split() = Section("2 buttons in fixed Row (width = screen width)") {
    Row {
        Box(modifier = Modifier.weight(0.5f)) {
            Button(
                onClick = boast("Click"),
                label = loremIpsum(1),
                icon = Flamingo.icons.Briefcase,
                fillMaxWidth = true,
                widthPolicy = ButtonWidthPolicy.TRUNCATING,
            )
        }
        Box(modifier = Modifier.weight(0.5f)) {
            Button(
                onClick = boast("Click"),
                label = loremIpsum(1),
                icon = Flamingo.icons.Briefcase,
                fillMaxWidth = true,
                widthPolicy = ButtonWidthPolicy.TRUNCATING,
            )
        }
    }
}

@Preview
@Composable
fun SplitWrapContent() = Section("2 buttons in fixed Row (width = button content)") {
    Row {
        Box(modifier = Modifier.weight(0.5f, fill = false)) {
            Button(
                onClick = boast("Click"),
                label = loremIpsum(1),
                icon = Flamingo.icons.Briefcase,
                fillMaxWidth = false,
                widthPolicy = ButtonWidthPolicy.TRUNCATING,
            )
        }
        Box(modifier = Modifier.weight(0.5f, fill = false)) {
            Button(
                onClick = boast("Click"),
                label = loremIpsum(1),
                icon = Flamingo.icons.Briefcase,
                fillMaxWidth = false,
                widthPolicy = ButtonWidthPolicy.TRUNCATING,
            )
        }
    }
}

@Preview
@Composable
fun SplitBigText() = Section(
    "2 buttons with large text in fixed Row (width = screen width)"
) {
    Row {
        Box(modifier = Modifier.weight(0.5f)) {
            Button(
                onClick = boast("Click"),
                label = loremIpsum20,
                icon = Flamingo.icons.Briefcase,
                fillMaxWidth = true,
                widthPolicy = ButtonWidthPolicy.TRUNCATING,
            )
        }
        Box(modifier = Modifier.weight(0.5f)) {
            Button(
                onClick = boast("Click"),
                label = loremIpsum20,
                icon = Flamingo.icons.Briefcase,
                fillMaxWidth = true,
                widthPolicy = ButtonWidthPolicy.TRUNCATING,
            )
        }
    }
}

@Preview
@Composable
fun SplitAndSqueezedBigText() = Section(
    "2 buttons in fixed Row (width = screen width), one of them is pinned"
) {
    Row {
        Box(modifier = Modifier.weight(0.15f)) {
            Button(
                onClick = boast("Click"),
                label = loremIpsum20,
                icon = Flamingo.icons.Briefcase,
                fillMaxWidth = true,
                widthPolicy = ButtonWidthPolicy.TRUNCATING,
            )
        }
        Box(modifier = Modifier.weight(0.85f)) {
            Button(
                onClick = boast("Click"),
                label = loremIpsum20,
                fillMaxWidth = true,
                widthPolicy = ButtonWidthPolicy.TRUNCATING,
            )
        }
    }
}

@Preview
@Composable
fun SplitMultiline() = Section(
    "2 multiline buttons in fixed Row (width = screen width)"
) {
    Row {
        Box(modifier = Modifier.weight(0.5f)) {
            Button(
                onClick = boast("Click"),
                label = loremIpsum20,
                icon = Flamingo.icons.Briefcase,
                fillMaxWidth = true,
                widthPolicy = ButtonWidthPolicy.MULTILINE,
            )
        }
        Box(modifier = Modifier.weight(0.5f)) {
            Button(
                onClick = boast("Click"),
                label = loremIpsum20,
                icon = Flamingo.icons.Briefcase,
                fillMaxWidth = true,
                widthPolicy = ButtonWidthPolicy.MULTILINE,
            )
        }
    }
}

@Preview
@Composable
fun VerticallySqueezedMultilineButton() = Section("Vertically compressed multiline button") {
    Box(modifier = Modifier
        .requiredHeight(40.dp)
        .clip(RectangleShape)) {
        Button(
            onClick = boast("Click"),
            label = loremIpsum20,
            widthPolicy = ButtonWidthPolicy.MULTILINE,
        )
    }
}
