package com.flamingo.playground

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.flamingo.Flamingo
import com.flamingo.components.EmptyState
import com.flamingo.components.EmptyStateImage
import com.flamingo.components.Icon
import com.flamingo.components.Text
import com.flamingo.components.topappbar.CenterItem
import com.flamingo.components.topappbar.TopAppBar
import com.flamingo.theme.FlamingoIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogosDemoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose { Content() }

    private data class Logo(
        /** in pascal case */
        val name: String,
        val icon: FlamingoIcon,
    )

    private var logos by mutableStateOf<List<Logo>?>(null)

    @OptIn(ExperimentalFoundationApi::class)
    @Preview
    @Composable
    private fun Content() {
        val context = LocalContext.current
        LoadIcons(context)
        Column {
            var searchRequest by remember { mutableStateOf("") }
            val listState = rememberLazyGridState()
            TopAppBar(
                center = CenterItem.Search(
                    context = context,
                    value = searchRequest,
                    onValueChange = { searchRequest = it },
                ),
                showShadow = remember {
                    // https://youtu.be/eDcGrY_AVlw?t=2793
                    derivedStateOf {
                        listState.firstVisibleItemScrollOffset != 0 ||
                                listState.firstVisibleItemIndex > 0
                    }
                }.value
            )

            val items = logos?.filter { it.name.contains(searchRequest, ignoreCase = true) }
            if (items == null) {
                Loading()
            } else if (items.isEmpty()) {
                NoIcons()
            } else LazyVerticalGrid(columns = GridCells.Adaptive(120.dp), state = listState) {
                items(items) { IconBox(it) }
            }
        }
    }

    @Composable
    private fun LoadIcons(context: Context) = LaunchedEffect(Unit) {
        launch(Dispatchers.IO) {
            logos = loadLogos().map {
                val icon = Flamingo.logos.fromId(it)
                Logo(
                    name = icon.getName(context).snakeToCamelCase(),
                    icon = icon
                )
            }
        }
    }

    @Composable
    private fun NoIcons() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            EmptyState(
                image = EmptyStateImage.EMPTY,
                title = stringResource(id = R.string.design_demo_icons_no_icons),
                contentDescription = stringResource(id = R.string.design_demo_icons_no_icons),
            )
        }
    }

    @Composable
    private fun IconBox(logo: Logo) = Column(
        modifier = Modifier
            .aspectRatio(1f)
            .border(0.7.dp, Flamingo.colors.separator)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(icon = logo.icon)
        Text(text = logo.name, textAlign = TextAlign.Center, style = Flamingo.typography.body2)
    }

    companion object {
        /**
         * Reflection is used because the [designSystemLogos] array field cannot be made public (it
         * will become modifiable), and cannot be converted into a [List] (boxing -> bad perf at
         * prod runtime). But bad perf from reflection in a staging demo is an acceptable tradeoff.
         */
        @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
        fun loadLogos(): IntArray {
            return Class.forName("com.flamingo.view.IconsKt")
                .getDeclaredField("designSystemLogos")
                .also { it.isAccessible = true }
                .get(null) as IntArray
        }

        private val snakeRegex = "_[a-zA-Z0-9]".toRegex()
        private fun String.snakeToCamelCase(): String = snakeRegex.replace(this) {
            it.value.drop(1).uppercase()
        }.replaceFirstChar { it.titlecase() }
    }
}