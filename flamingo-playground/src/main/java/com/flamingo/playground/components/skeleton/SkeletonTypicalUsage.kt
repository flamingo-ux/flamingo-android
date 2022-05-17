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

package com.flamingo.playground.components.skeleton

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.flamingo.components.Skeleton
import com.flamingo.playground.Compose
import com.flamingo.demoapi.TypicalUsageDemo
import com.flamingo.demoapi.WhiteModeDemo

@TypicalUsageDemo
class SkeletonTypicalUsage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose { TypicalUsageContent() }

    @Composable
    private fun TypicalUsageContent() = Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Circle()
        Text()
        Rectangle()
        RectangleNoRoundCorners()
        Square()
        WhiteModeDemo(Modifier.requiredSize(60.dp, 60.dp), white = true) {
            Skeleton(modifier = Modifier.requiredSize(30.dp, 30.dp))
        }
    }
}
