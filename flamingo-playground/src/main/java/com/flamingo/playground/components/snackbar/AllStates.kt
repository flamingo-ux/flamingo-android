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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import com.flamingo.components.snackbar.Snackbar
import com.flamingo.components.snackbar.SnackbarData
import com.flamingo.components.snackbar.SnackbarDuration
import com.flamingo.components.snackbar.SnackbarHost
import com.flamingo.demoapi.AllPossibleStatesDemo
import com.flamingo.loremIpsum
import com.flamingo.playground.Compose
import com.flamingo.playground.internalComponents

@AllPossibleStatesDemo
class AllStates : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose {
        SnackbarHost {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                arrayOf(false, true).forEach { isError ->
                    with(internalComponents) {
                        Snackbar(snackbarData = SnackbarDataImpl(
                            message = loremIpsum(5),
                            isError = isError
                        ))
                        Snackbar(snackbarData = SnackbarDataImpl(
                            message = loremIpsum(5),
                            actionLabel = loremIpsum(1),
                            isError = isError
                        ))

                        Snackbar(snackbarData = SnackbarDataImpl(
                            message = loremIpsum(50),
                            isError = isError
                        ))
                        Snackbar(snackbarData = SnackbarDataImpl(
                            message = loremIpsum(50),
                            actionLabel = loremIpsum(2),
                            isError = isError
                        ))

                        Snackbar(snackbarData = SnackbarDataImpl(
                            message = loremIpsum(5),
                            actionLabel = loremIpsum(2),
                            actionOnNewLine = true,
                            isError = isError
                        ))
                        Snackbar(snackbarData = SnackbarDataImpl(
                            message = loremIpsum(50),
                            actionLabel = loremIpsum(2),
                            actionOnNewLine = true,
                            isError = isError
                        ))
                    }
                }
            }
        }
    }
}

private data class SnackbarDataImpl(
    override val message: String,
    override val actionLabel: String? = null,
    override val actionOnNewLine: Boolean = false,
    override val isError: Boolean = false,
) : SnackbarData {
    override val duration: SnackbarDuration = SnackbarDuration.Indefinite
    override fun performAction() = Unit
    override fun dismiss() = Unit
}
