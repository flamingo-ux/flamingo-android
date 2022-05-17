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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.flamingo.playground.Compose
import com.flamingo.demoapi.FlamingoComponentDemoName
import com.flamingo.demoapi.TypicalUsageDemo

@TypicalUsageDemo
@FlamingoComponentDemoName("Horizontal List")
class ButtonInRowTypicalUsage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose { ButtonInRowSample() }

    @Preview
    @Composable
    private fun ButtonInRowSample() = Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        ButtonsInRow()
        Squeezed()
        SqueezedAndClipped()
        Split()
        SplitWrapContent()
        SplitBigText()
        SplitAndSqueezedBigText()
        SplitMultiline()
        VerticallySqueezedMultilineButton()
        Spacer(modifier = Modifier.requiredHeight(24.dp))
    }
}
