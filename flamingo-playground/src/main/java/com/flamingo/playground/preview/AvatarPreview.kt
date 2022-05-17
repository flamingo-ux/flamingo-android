package com.flamingo.playground.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.Flamingo
import com.flamingo.components.Avatar
import com.flamingo.components.AvatarBackground
import com.flamingo.components.AvatarContent
import com.flamingo.components.AvatarIndicator
import com.flamingo.components.AvatarShape
import com.flamingo.components.IndicatorColor
import com.flamingo.view.components.Avatar

class AvatarPreview : ViewPreview<Avatar>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): Avatar {
        val avatar = Avatar(requireContext())
        avatar.ds.setLetters(
            letters = "AA",
            background = Avatar.BACKGROUND_ORANGE,
            shape = Avatar.SHAPE_ROUNDED_CORNERS_MEDIUM,
            avatarSize = Avatar.SIZE_112
        )
        return avatar
    }
}

@Composable
@Preview
@Suppress("FunctionNaming")
fun AvatarComposePreview() = Avatar(
    content = AvatarContent.Icon(Flamingo.icons.Archive, AvatarBackground.PRIMARY),
    indicator = AvatarIndicator(IndicatorColor.PRIMARY),
    shape = AvatarShape.ROUNDED_CORNERS_SMALL,
    contentDescription = null
)
