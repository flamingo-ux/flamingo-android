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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.flamingo.components.Action
import com.flamingo.components.ActionGroup
import com.flamingo.components.EmptyState
import com.flamingo.components.EmptyStateImage
import com.flamingo.components.button.Button
import com.flamingo.components.listitem.ListItem
import com.flamingo.components.listitem.ListItemSkeleton
import com.flamingo.components.listitem.TextBlocksSkeleton
import com.flamingo.playground.Compose
import com.flamingo.demoapi.FlamingoComponentDemoName
import com.flamingo.playground.R
import com.flamingo.demoapi.TypicalUsageDemo
import com.flamingo.playground.boast
import com.flamingo.playground.utils.exhaustive

@TypicalUsageDemo
@FlamingoComponentDemoName("Loadable List With Error Handling")
class ListLoadingTypicalUsage : Fragment() {
    private val viewModel by viewModels<MoviesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose { Content() }

    @Composable
    internal fun Content() {
        val moviesState by viewModel.state.collectAsState()

        when (val state = moviesState) {
            MoviesState.Error -> Error()
            MoviesState.Loading -> LazyColumn {
                items(4) { ListItemSkeleton(textBlocks = TextBlocksSkeleton.TWO) }
            }
            is MoviesState.Success -> MoviesList(state.movies)
        }.exhaustive
    }

    @Composable
    private fun MoviesList(movies: List<Movie>) {
        LazyColumn {
            items(movies) {
                ListItem(
                    title = it.title,
                    description = it.shortDescription,
                    onClick = boast("Click on \"" + it.title + "\" movie")
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { viewModel.loadMoviesList() },
                        label = stringResource(R.string.list_loading_typical_usage_refresh)
                    )
                }
            }
        }
    }

    @Composable
    private fun Error() = EmptyState(
        image = EmptyStateImage.CRASH,
        title = stringResource(R.string.list_loading_typical_usage_loading_error_title),
        description = stringResource(R.string.list_loading_typical_usage_loading_error_description),
        actions = ActionGroup(
            Action(
                label = stringResource(R.string.list_loading_typical_usage_refresh),
                onClick = { viewModel.loadMoviesList() })
        ),
        verticalScroll = true,
        contentDescription = null
    )
}

class MoviesViewModel : ViewModel() {
    private val _state = MutableStateFlow<MoviesState>(MoviesState.Loading)
    val state: StateFlow<MoviesState> get() = _state

    private var isError: Boolean = false
        get() {
            field = !field
            return field
        }

    init {
        loadMoviesList()
    }

    fun loadMoviesList() {
        viewModelScope.launch {
            _state.value = MoviesState.Loading
            delay(1000) // imitates loading from server
            _state.value = if (isError) MoviesState.Error else MoviesState.Success(moviesDB)
        }
    }
}


sealed class MoviesState {
    object Loading : MoviesState()
    object Error : MoviesState()
    data class Success(val movies: List<Movie>) : MoviesState()
}

data class Movie(val title: String, val shortDescription: String)


@Suppress("MaxLineLength")
private val moviesDB = listOf(
    Movie(
        title = "The Shawshank Redemption",
        shortDescription = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."
    ),
    Movie(
        title = "Schindler's List",
        shortDescription = "In German-occupied Poland during World War II, industrialist Oskar Schindler gradually becomes concerned for his Jewish workforce after witnessing their persecution by the Nazis."
    ),
    Movie(
        title = "Forrest Gump",
        shortDescription = "The presidencies of Kennedy and Johnson, the Vietnam War, the Watergate scandal and other historical events unfold from the perspective of an Alabama man with an IQ of 75, whose only desire is to be reunited with his childhood sweetheart."
    ),
    Movie(
        title = "One Flew Over the Cuckoo's Nest",
        shortDescription = "A criminal pleads insanity and is admitted to a mental institution, where he rebels against the oppressive nurse and rallies up the scared patients."
    ),
)
