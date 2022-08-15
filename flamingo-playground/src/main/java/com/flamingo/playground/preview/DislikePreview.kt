package com.flamingo.playground.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.components.socialgroup.SocialGroup
import com.flamingo.components.socialgroup.likedislike.Dislike

@Composable
@Preview
fun DislikePreview() {
    var checked by remember { mutableStateOf(false) }
    SocialGroup.Dislike(
        checked = checked,
        onCheckedChange = { checked = it },
        counter = (if (checked) 75 else 74).toString(),
    )
}
