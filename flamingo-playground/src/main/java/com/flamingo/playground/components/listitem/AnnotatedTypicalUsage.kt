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

package com.flamingo.playground.components.listitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flamingo.playground.Compose
import com.flamingo.demoapi.FlamingoComponentDemoName
import com.flamingo.demoapi.TypicalUsageDemo

@TypicalUsageDemo
@FlamingoComponentDemoName("AnnotatedString Usage")
class AnnotatedTypicalUsage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose { AnnotatedListItemSample() }
}
