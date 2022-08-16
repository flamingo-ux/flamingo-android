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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.flamingo.Flamingo
import com.flamingo.annotations.view.FlamingoComponent
import com.flamingo.components.AlertMessage
import com.flamingo.components.AlertMessageVariant
import com.flamingo.components.IconButton
import com.flamingo.components.IconButtonVariant
import com.flamingo.components.Text
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.listitem.ListItem
import com.flamingo.crab.FlamingoComponentRecord
import com.flamingo.crab.FlamingoRegistry
import com.flamingo.demoapi.FlamingoComponentDemoName
import com.flamingo.playground.Compose
import com.flamingo.playground.R
import com.flamingo.playground.StagingFragmentContainer
import com.flamingo.playground.components.alertmessage.TheaterPkg
import com.flamingo.playground.fromArguments
import com.flamingo.playground.showBoast
import com.flamingo.playground.theater.BackstageFragment
import com.flamingo.playground.theater.TheaterFragment
import com.theater.TheaterPackage
import timber.log.Timber
import kotlin.reflect.KClass
import com.flamingo.R as FlamingoR
import com.flamingo.view.components.FlamingoComponent as FlamingoComponentInterface

/**
 * Fragment that allows to view a design system component preview, name, demos, docs, etc.
 * This info is gathered from [FlamingoRegistry].
 */
class ComponentDetailsFragment : Fragment() {

    private lateinit var componentRecord: FlamingoComponentRecord

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): ComposeView {
        val funName = fromArguments<String>("funName")!!
        componentRecord = FlamingoRegistry.components.first { it.funName == funName }
        return Compose { Content() }
    }

    @Preview
    @Composable
    private fun Content() {
        if (LocalInspectionMode.current) {
            componentRecord = FlamingoRegistry.components.first()
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Flamingo.colors.background)
        ) {
            item { PreviewSection() }
            item { TitleSection() }
            item { InternalComponentSection() }
            item { TheaterSection() }
            DemosSection()
            SamplesSection()
            item { ViewImplSection() }
            item { FigmaSection() }
            item { SpecSection() }
            item { WhiteModeSection() }
            item { UsedInsteadOfSection() }
            item { DocsSection() }
        }
    }

    @Composable
    private fun InternalComponentSection() {
        if (!componentRecord.internal) return
        Spacer(modifier = Modifier.padding(top = 16.dp))
        AlertMessage(
            text = stringResource(R.string.component_details_internal_component),
            variant = AlertMessageVariant.WARNING
        )
    }

    @Preview
    @Composable
    fun TheaterDialog(
        onDismissRequest: () -> Unit = {},
        stagingFragmentContainer: StagingFragmentContainer = object : StagingFragmentContainer {
            override fun openFragment(fragment: Fragment, tag: String?) = Unit
        },
        theaterPackage: String = TheaterPkg::class.java.name,
    ) = Dialog(onDismissRequest = onDismissRequest) {
        val theaterPackageInstance = remember {
            Class.forName(theaterPackage).newInstance() as TheaterPackage
        }
        LazyColumn(
            modifier = Modifier
                .background(Flamingo.colors.background, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp)),
            contentPadding = PaddingValues(8.dp)
        ) {
            item {
                ListItem(title = "Render video", showDivider = false, onClick = {
                    stagingFragmentContainer.openFragment(
                        TheaterFragment.newInstance(theaterPackage, renderMode = true)
                    )
                })
            }

            item { ListItem(subtitle = "Backstages:", showDivider = false) }

            itemsIndexed(theaterPackageInstance.play.backstages) { index, item ->
                ListItem(title = item.name, showDivider = false, onClick = {
                    stagingFragmentContainer.openFragment(
                        BackstageFragment.newInstance(theaterPackage, index)
                    )
                })
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun TheaterSection() {
        val theaterPackage = componentRecord.theaterPackage?.qualifiedName ?: return
        var isTheaterDialogShown by remember { mutableStateOf(false) }
        val stagingFragmentContainer = remember { requireActivity() as StagingFragmentContainer }
        if (isTheaterDialogShown) TheaterDialog(
            onDismissRequest = { isTheaterDialogShown = false },
            stagingFragmentContainer = stagingFragmentContainer,
            theaterPackage = theaterPackage,
        )
        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(12.dp), clip = false)
                .clip(RoundedCornerShape(12.dp))
                .combinedClickable(onLongClick = { isTheaterDialogShown = true }, onClick = {
                    stagingFragmentContainer.openFragment(
                        TheaterFragment.newInstance(theaterPackage, renderMode = false)
                    )
                }),
        ) {
            Image(
                modifier = Modifier.matchParentSize(),
                painter = painterResource(id = FlamingoR.drawable.ds_gradient_purple),
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.component_details_theater_title),
                    color = Flamingo.palette.white,
                    style = Flamingo.typography.h2,
                )
                Spacer(modifier = Modifier.requiredHeight(8.dp))
                Text(
                    text = stringResource(R.string.component_details_theater_subtitle),
                    color = Flamingo.palette.white,
                    style = Flamingo.typography.body1,
                )
            }
        }
    }

    @Composable
    private fun PreviewSection() = Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .border(2.dp, Flamingo.colors.outline, previewShape)
            .clip(previewShape)
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        componentRecord.preview()
    }

    @Composable
    private fun TitleSection() = Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 4.dp),
        text = componentRecord.displayName,
        textAlign = TextAlign.Center,
        style = Flamingo.typography.h1,
    )

    private fun LazyListScope.DemosSection() {
        if (componentRecord.demo.isEmpty()) return
        item { SectionName(stringResource(R.string.component_details_demo_section)) }
        val demos = loadDemos()
        if (demos == null) item { DemosLoadError() }
        else items(demos) { DemoItem(it) }
    }

    private fun LazyListScope.SamplesSection() {
        if (componentRecord.samples.isEmpty()) return
        item { SectionName(stringResource(R.string.component_details_samples_section)) }
        items(componentRecord.samples) { sampleRecord ->
            val sampleName = sampleRecord.funName.substringAfterLast('.')
            ListItem(
                title = sampleName,
                end = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (sampleRecord.sourceCode != null) {
                            SourceCodeButton(sampleRecord.sourceCode, sampleName)
                        }
                        if (sampleRecord.content != null) ContentButton(sampleRecord.content)
                    }
                }
            )
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun ContentButton(content: @Composable () -> Unit) {
        var contentDialog by rememberSaveable { mutableStateOf(false) }
        if (contentDialog) Dialog(
            onDismissRequest = { contentDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Flamingo.colors.background, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .padding(16.dp),
            ) {
                content.invoke()
            }
        }
        IconButton(
            onClick = { contentDialog = true },
            icon = Flamingo.icons.Play,
            variant = IconButtonVariant.TEXT,
            contentDescription = null
        )
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun SourceCodeButton(
        sourceCode: String,
        sampleName: String,
    ) {
        var sourceCodeDialog by rememberSaveable { mutableStateOf(false) }
        if (sourceCodeDialog) Dialog(
            onDismissRequest = { sourceCodeDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) { SourceCodeDialogContent({ sourceCodeDialog = false }, sampleName, sourceCode) }
        IconButton(
            onClick = { sourceCodeDialog = true },
            icon = Flamingo.icons.Code,
            variant = IconButtonVariant.TEXT,
            contentDescription = null
        )
    }

    @Composable
    private fun SourceCodeDialogContent(
        onDismissRequest: () -> Unit,
        name: String,
        sourceCode: String,
    ) = Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Flamingo.colors.background,
                RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
    ) {
        ListItem(title = name)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
                .horizontalScroll(rememberScrollState())
                .verticalScroll(rememberScrollState()),
            text = sourceCode,
            fontFamily = FontFamily.Monospace,
            color = Flamingo.colors.textPrimary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
        ) {
            val clipboardManager = LocalClipboardManager.current
            val context = LocalContext.current
            Button(onClick = onDismissRequest, label = "Close")
            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString(sourceCode))
                    context.showBoast("Copied to clipboard")
                },
                label = "Copy",
                color = ButtonColor.Primary
            )
        }
    }

    @Composable
    private fun UrlSection(sectionName: String, url: String) {
        SectionName(name = sectionName)

        val uriHandler = LocalUriHandler.current

        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { uriHandler.openUri(url) },
            text = url,
            color = Flamingo.colors.secondary,
            textDecoration = TextDecoration.Underline,
            style = Flamingo.typography.body2,
        )
    }

    @Composable
    private fun SpecSection() {
        UrlSection(
            sectionName = stringResource(R.string.component_details_spec_section),
            url = componentRecord.specification?.toString() ?: return
        )
    }

    @Composable
    private fun FigmaSection() {
        UrlSection(
            sectionName = stringResource(R.string.component_details_figma_section),
            url = componentRecord.figma?.toString() ?: return
        )
    }

    @Composable
    fun WhiteModeSection() {
        SectionName(stringResource(R.string.component_details_white_mode_section))
        Text(
            modifier = Modifier
                .padding(top = 8.dp),
            text = stringResource(
                if (componentRecord.supportsWhiteMode) {
                    R.string.component_details_yes
                } else {
                    R.string.component_details_no
                }
            ),
            color = Flamingo.colors.textSecondary,
            style = Flamingo.typography.body2,
        )
    }

    @Composable
    fun UsedInsteadOfSection() {
        if (componentRecord.usedInsteadOf.isEmpty()) return
        SectionName(stringResource(R.string.component_details_used_instead_of_section))
        val simpleNameStyle = Flamingo.typography.subtitle2
        val packageTextStyle = SpanStyle(
            color = Flamingo.colors.textTertiary,
            fontSize = simpleNameStyle.fontSize * 0.8
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = buildAnnotatedString {
                componentRecord.usedInsteadOf.forEachIndexed { index, it ->
                    val packageName = it.substringBeforeLast('.') + "."
                    val simpleName = it.substringAfterLast('.')

                    withStyle(packageTextStyle) { append(packageName) }
                    append(simpleName)

                    val isLast = componentRecord.usedInsteadOf.lastIndex == index
                    if (!isLast) append('\n')
                }
            },
            color = Flamingo.colors.textSecondary,
            style = simpleNameStyle,
        )
    }

    @Composable
    fun ViewImplSection() {
        val viewImpl = componentRecord.viewImplementation
        SectionName(stringResource(R.string.component_details_android_view_impl_section))
        Spacer(modifier = Modifier.padding(top = 8.dp))

        if (viewImpl == null) {
            Text(
                text = stringResource(R.string.component_details_no),
                color = Flamingo.colors.textSecondary,
                style = Flamingo.typography.body2,
            )
        } else {
            Text(
                modifier = Modifier.clickable { openViewImpl(viewImpl) },
                text = stringResource(R.string.component_details_open_android_impl),
                color = Flamingo.colors.secondary,
                textDecoration = TextDecoration.Underline,
                style = Flamingo.typography.body2,
            )
        }
    }

    private fun openViewImpl(viewImpl: KClass<out FlamingoComponentInterface>) {
        val annClass = FlamingoComponent::class.java
        val viewComponent = viewImpl.java.getAnnotation(annClass)
            ?: error("Annotation @${annClass.simpleName} not found")

        (requireActivity() as StagingFragmentContainer)
            .openFragment(ViewComponentDetailsFragment.newInstance(viewComponent))
    }

    @Composable
    private fun DocsSection() {
        componentRecord.docs?.let {
            SectionName(stringResource(R.string.component_details_docs_section))
            Text(
                text = AnnotatedString(it),
                fontFamily = FontFamily.Monospace,
                color = Flamingo.colors.textSecondary,
            )
        }
    }

    @Composable
    private fun SectionName(name: String) {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = name,
            color = Flamingo.colors.primary,
            style = Flamingo.typography.body1,
        )
    }

    @Composable
    private fun DemoItem(demo: Demo) {
        ListItem(
            title = demo.title,
            subtitle = demo.subtitle,
            onClick = {
                val fragment = demo.instantiateFragment()
                (requireActivity() as StagingFragmentContainer)
                    .openFragment(fragment, demo.clazz.name)
            }
        )
    }

    /** @return null, if failed to load demos */
    private fun loadDemos(): List<Demo>? = runCatching {
        componentRecord.demo.mapIndexed { index, fqn ->
            val clazz = Class.forName(fqn) as Class<out Fragment>
            val optionalName = clazz
                .getAnnotation(FlamingoComponentDemoName::class.java)?.name
            val typeName = renderDemoTypeName(clazz)
            Demo(
                clazz = clazz,
                title = optionalName
                    ?: requireContext().getString(R.string.component_details_demo_item, index + 1),
                subtitle = typeName.ifEmpty { null },
            )
        }
    }.onFailure(Timber::e).getOrNull()

    @Composable
    private fun DemosLoadError() = Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 16.dp),
        text = stringResource(R.string.error_while_loading_demos_for_component),
        textAlign = TextAlign.Center,
        color = Flamingo.colors.error,
        style = Flamingo.typography.body2,
    )

    companion object {
        private val previewShape = RoundedCornerShape(10.dp)

        @JvmStatic
        fun newInstance(funName: String) = ComponentDetailsFragment().apply {
            arguments = bundleOf("funName" to funName)
        }
    }
}
