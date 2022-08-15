package com.flamingo.components.socialgroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flamingo.Flamingo

@Composable
internal inline fun SocialGroupLayout(
    modifier: Modifier = Modifier,
    size: SocialGroupSize,
    content: @Composable RowScope.() -> Unit,
) = Row(
    modifier = Modifier
        .clip(SocialGroupLayoutShape)
        .then(modifier)
        .padding(size.layoutPadding),
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalAlignment = Alignment.CenterVertically,
    content = content,
)

private val SocialGroupLayoutShape = RoundedCornerShape(12.dp)

public enum class SocialGroupColor {
    DEFAULT, PRIMARY,
}

public enum class SocialGroupSize(
    internal val iconSize: Dp,
    internal val layoutPadding: Dp,
) {
    SMALL(iconSize = 16.dp, layoutPadding = 6.dp) {
        @Composable
        @ReadOnlyComposable
        override fun textStyle(): TextStyle = Flamingo.typography.body2
    },
    BIG(iconSize = 24.dp, layoutPadding = 8.dp) {
        @Composable
        @ReadOnlyComposable
        override fun textStyle(): TextStyle = Flamingo.typography.body1
    },
    ;

    @Composable
    @ReadOnlyComposable
    internal abstract fun textStyle(): TextStyle
}
