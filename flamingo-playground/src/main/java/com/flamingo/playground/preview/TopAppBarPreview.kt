package com.flamingo.playground.preview

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.flamingo.Flamingo
import com.flamingo.components.AvatarContent
import com.flamingo.components.AvatarIndicator
import com.flamingo.components.IconButtonIndicator
import com.flamingo.components.IndicatorColor
import com.flamingo.components.topappbar.ActionItem
import com.flamingo.components.topappbar.CenterItem
import com.flamingo.components.topappbar.EdgeItem
import com.flamingo.components.topappbar.TopAppBar
import com.flamingo.demoapi.R as DemoR

@Preview
@Composable
@Suppress("FunctionNaming")
fun TopAppBarPreview(listState: LazyListState = rememberLazyListState()) {
    TopAppBar(
        start = EdgeItem.IconButton(Flamingo.icons.ArrowLeft) {},
        center = CenterItem.Center(
            title = "Title",
            subtitle = "Subtitle",
            avatar = CenterItem.Avatar(
                content = AvatarContent.Image(painterResource(id = DemoR.drawable.example_dog)),
                indicator = AvatarIndicator(IndicatorColor.WARNING),
            )
        ),
        action1 = ActionItem(
            Flamingo.icons.Bell,
            indicator = IconButtonIndicator(color = IndicatorColor.PRIMARY)
        ) {},
        action2 = ActionItem(Flamingo.icons.Calendar) {},
        end = EdgeItem.IconButton(Flamingo.icons.Coffee) {},
        listState = listState,
    )
}

@Composable
@Suppress("FunctionNaming")
fun TopAppBarPreview2(scrollState: ScrollState) {
    TopAppBar(
        start = EdgeItem.IconButton(Flamingo.icons.ArrowLeft) {},
        center = CenterItem.Center(
            title = "Title",
            subtitle = "Subtitle",
            avatar = CenterItem.Avatar(
                content = AvatarContent.Image(painterResource(id = DemoR.drawable.example_dog)),
                indicator = AvatarIndicator(IndicatorColor.WARNING),
            )
        ),
        action1 = ActionItem(
            Flamingo.icons.Bell,
            indicator = IconButtonIndicator(color = IndicatorColor.PRIMARY)
        ) {},
        action2 = ActionItem(Flamingo.icons.Calendar) {},
        end = EdgeItem.IconButton(Flamingo.icons.Coffee) {},
        scrollState = scrollState,
    )
}
