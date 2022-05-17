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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.flamingo.Flamingo
import com.flamingo.components.Text
import com.flamingo.components.listitem.ListItem

class AccessibilityAndUiTestingLabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose { Content() }

    @Preview
    @Composable
    private fun Content() {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val testTagValue = "test tag example value"
            val contentDescriptionValue = "content description example value"
            val customSemanticsPropertyValue = "custom semantics property example value"

            Box(
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 16.dp)
                    .requiredSize(60.dp)
                    .shadow(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Flamingo.colors.primary)
                    .semantics {
                        testTag = testTagValue
                        contentDescription = contentDescriptionValue
                        customSemanticsProperty = customSemanticsPropertyValue
                    }
            )

            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(R.string.accessibility_and_ui_testing_lab_text),
                textAlign = TextAlign.Center,
                style = Flamingo.typography.h3
            )

            ListItem(
                title = SemanticsProperties.TestTag.name,
                description = testTagValue
            )
            ListItem(
                title = SemanticsProperties.ContentDescription.name,
                description = contentDescriptionValue
            )
            ListItem(
                title = SemanticsProperties.CustomSemanticsProperty.name,
                description = customSemanticsPropertyValue
            )
        }
    }
}

private val SemanticsProperties.CustomSemanticsProperty get() = customSemanticsProperty
private val customSemanticsProperty = SemanticsPropertyKey<String>(
    name = "CustomSemanticsProperty",
    /* Never merge */
    mergePolicy = { parentValue, _ -> parentValue }
)

private var SemanticsPropertyReceiver.customSemanticsProperty by customSemanticsProperty
