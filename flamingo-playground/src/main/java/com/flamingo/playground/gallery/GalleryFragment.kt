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

package com.flamingo.playground.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.flamingo.Flamingo
import com.flamingo.annotations.view.FlamingoComponent
import com.flamingo.components.EmptyState
import com.flamingo.components.EmptyStateImage
import com.flamingo.components.Switch
import com.flamingo.components.listitem.ListItem
import com.flamingo.components.listitem.ListItemSkeleton
import com.flamingo.components.listitem.TextBlocksSkeleton
import com.flamingo.crab.FlamingoComponentRecord
import com.flamingo.playground.Compose
import com.flamingo.playground.R
import com.flamingo.playground.StagingFragmentContainer

/**
 * A fragment that lists all design system components and allows opening
 * [ViewComponentDetailsFragment] or [ComponentDetailsFragment].
 */
@OptIn(ExperimentalFoundationApi::class)
class GalleryFragment : Fragment() {

    private val viewModel by viewModels<GalleryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose { Content() }

    @Composable
    private fun Content() = Column {
        val viewComponents: List<FlamingoComponent>? by viewModel.viewComponents.collectAsState()
        val filter by viewModel.filter.collectAsState()
        val composeComponentsNumber = viewModel.composeComponents.size
        val viewComponentsNumber = viewComponents?.size

        val numberOfComponentsString =
            stringResource(R.string.design_demo_components_gallery_number_of_components)

        ListItem(
            title = if (filter.composeComponents) {
                "Compose ($numberOfComponentsString $composeComponentsNumber)"
            } else {
                "Android View" + if (viewComponentsNumber != null) {
                    " ($numberOfComponentsString $viewComponentsNumber)"
                } else {
                    ""
                }
            },
            showDivider = false,
            onClick = { viewModel.changeComposeComponentsFilter() },
            end = { Switch(checked = filter.composeComponents, onCheckedChange = null) }
        )

        AnimatedVisibility(filter.composeComponents) {
            ListItem(
                title = stringResource(R.string.design_demo_components_gallery_show_previews),
                showDivider = false,
                onClick = { viewModel.changeShowPreviewsFilter() },
                end = { Switch(checked = filter.showPreviews, onCheckedChange = null) }
            )
        }

        Crossfade(filter) {
            when {
                it.composeComponents && it.showPreviews -> ComposeComponentsWithPreview()
                it.composeComponents -> ComposeComponents()
                else -> ViewComponents(viewComponents)
            }
        }
    }

    @ExperimentalFoundationApi
    @Composable
    private fun ViewComponents(viewComponents: List<FlamingoComponent>?) {
        if (viewComponents?.isEmpty() == true) NoComponents(
            stringResource(R.string.design_demo_components_gallery_no_view_components)
        ) else LazyVerticalGrid(
            columns = GridCells.Fixed(COLUMN_COUNT),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            if (viewComponents == null) items(12) { ViewSkeleton() }
            else items(viewComponents) { ViewComponent(it) }
        }
    }

    @Composable
    private fun ViewSkeleton() = ListItemSkeleton(textBlocks = TextBlocksSkeleton.ONE)

    @Composable
    private fun ComposeComponents() = Column {
        val records = viewModel.composeComponents.sortedBy { it.displayName }
        if (records.isEmpty()) NoComponents(
            stringResource(R.string.design_demo_components_gallery_no_compose_components)
        ) else {
            ListItem(
                subtitle = stringResource(R.string.design_demo_components_gallery_dots_meaning),
                showDivider = false,
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(COLUMN_COUNT),
                contentPadding = PaddingValues(bottom = 16.dp),
            ) {
                items(records) { ComposableComponent(it) }
            }
        }
    }

    @Composable
    private fun ComposeComponentsWithPreview() {
        val records = viewModel.composeComponents.sortedBy { it.displayName }
        if (records.isEmpty()) NoComponents(
            stringResource(R.string.design_demo_components_gallery_no_compose_components)
        ) else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) { items(records) { record -> ComposableComponentNew(record) } }
        }
    }

    @Composable
    private fun ComposableComponentNew(record: FlamingoComponentRecord) {
        val shape = RoundedCornerShape(16.dp)
        Column(
            modifier = Modifier
                .clip(shape)
                .border(width = 2.dp, color = Flamingo.colors.outline, shape = shape)
                .clickable {
                    (requireActivity() as StagingFragmentContainer)
                        .openFragment(ComponentDetailsFragment.newInstance(record.funName))
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 12.dp, end = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                record.preview()
            }
            ListItem(title = record.displayName, showDivider = false)
        }
    }

    @Composable
    private fun NoComponents(description: String) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            EmptyState(
                title = stringResource(
                    R.string.design_demo_components_gallery_no_components_empty_state_title
                ),
                description = description,
                image = EmptyStateImage.EMPTY,
                contentDescription = null
            )
        }
    }

    @Composable
    private fun ViewComponent(viewComponent: FlamingoComponent) = ListItem(
        title = viewComponent.displayName,
        showDivider = false,
        onClick = {
            (requireActivity() as StagingFragmentContainer)
                .openFragment(ViewComponentDetailsFragment.newInstance(viewComponent))
        }
    )

    @Composable
    private fun ComposableComponent(record: FlamingoComponentRecord) = ListItem(
        title = buildAnnotatedString {
            append(record.displayName)
            if (record.viewImplementation != null) {
                withStyle(SpanStyle(Flamingo.colors.warning)) { append(" ⬤") }
            }
        },
        showDivider = false,
        onClick = {
            (requireActivity() as StagingFragmentContainer)
                .openFragment(ComponentDetailsFragment.newInstance(record.funName))
        }
    )
}

private const val COLUMN_COUNT = 2
