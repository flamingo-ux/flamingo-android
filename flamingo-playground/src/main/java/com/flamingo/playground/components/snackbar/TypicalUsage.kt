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

package com.flamingo.playground.components.snackbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.flamingo.components.snackbar.LocalSnackbarHostState
import com.flamingo.components.snackbar.SnackbarHost
import com.flamingo.demoapi.TypicalUsageDemo
import com.flamingo.loremIpsum
import com.flamingo.playground.Compose
import com.flamingo.playground.preview.EmptyStateComposePreview

@TypicalUsageDemo
class TypicalUsage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose {
        SnackbarHost {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                repeat(3) { EmptyStateComposePreview() }
            }
            val snackbarHostState = LocalSnackbarHostState.current
            LaunchedEffect(true) {
                snackbarHostState.showSnackbar(
                    message = loremIpsum(4),
                    actionLabel = loremIpsum(1),
                )
                snackbarHostState.showSnackbar(
                    message = loremIpsum(40),
                    actionLabel = loremIpsum(1),
                )
                snackbarHostState.showSnackbar(
                    message = loremIpsum(3),
                    actionLabel = loremIpsum(2),
                )
            }
        }
    }
}
