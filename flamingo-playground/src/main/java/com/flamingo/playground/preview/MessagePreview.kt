package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.socialgroup.SocialGroup
import com.flamingo.components.socialgroup.button.Message
import com.flamingo.playground.boast

@Composable
@Preview
fun MessagePreview() = SocialGroup.Message(
    onClick = boast(),
    counter = "75",
)
