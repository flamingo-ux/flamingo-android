package com.flamingo.playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.flamingo.Flamingo
import com.flamingo.components.Text
import com.flamingo.components.listitem.ListItem
import com.flamingo.theme.colors.FlamingoColors
import com.flamingo.withStyle
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.jvmErasure

class ThemeColorsDemoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose { Content() }

    private data class ThemeColor(
        val name: String,
        val value: Color,
        val deprecated: String? = null,
    )

    private fun obtainThemeColors(
        clazz: KClass<*>,
        instance: Any,
        propertyName: String,
        deprecated: String?,
    ): Node? = runCatching {
        val children = clazz
            .declaredMemberProperties
            .filter { it.name != "isLight" }
            .mapNotNull {
                val property = it as KProperty1<Any, *>
                val returnClazz: KClass<out Any> = property.returnType.jvmErasure
                if (returnClazz == Color::class) runCatching {
                    Node(
                        value = ThemeColor(
                            name = property.name,
                            value = property.get(instance) as Color,
                            deprecated = property.findAnnotation<Deprecated>()?.message
                        )
                    )
                }.getOrNull() else obtainThemeColors(
                    clazz = returnClazz,
                    instance = property.get(instance)!!,
                    propertyName = property.name,
                    deprecated = property.findAnnotation<Deprecated>()?.message,
                )
            }

        val rootNode = Node(ThemeColor(propertyName, Color.Unspecified, deprecated), children)
        return rootNode
    }.getOrNull()

    private class Node(
        val value: ThemeColor,
        val children: List<Node> = listOf(),
    )

    @Composable
    private fun Content() {
        val colorsTree = obtainThemeColors(
            clazz = FlamingoColors::class,
            instance = Flamingo.colors,
            propertyName = ROOT_NODE_NAME,
            deprecated = null
        )!!
        LazyColumn {
            item { Introduction() }
            drawTree(colorsTree, depth = -1)
        }
    }

    private fun LazyListScope.drawTree(node: Node, depth: Int) {
        if (node.children.isEmpty()) item {
            Section(
                themeColor = node.value,
                startPadding = DEPTH_PADDING * depth,
                boldTitle = false,
            )
        } else {
            if (node.value.name != ROOT_NODE_NAME) item {
                Section(
                    themeColor = node.value,
                    startPadding = DEPTH_PADDING * depth,
                    boldTitle = true,
                )
            }
            node.children.forEach { drawTree(it, depth + 1) }
        }
    }

    @Composable
    private fun Introduction() {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            style = Flamingo.typography.body1,
            text = stringResource(R.string.theme_color_demo_introduction),
        )
    }

    @Composable
    private fun Section(
        themeColor: ThemeColor,
        startPadding: Dp,
        boldTitle: Boolean,
    ) = Box(modifier = Modifier.padding(start = startPadding)) {
        ListItem(
            title = buildAnnotatedString {
                val sectionTitleStyle =
                    with(Flamingo.typography) { if (boldTitle) h5 else body1 }.run {
                        if (themeColor.deprecated != null) {
                            copy(textDecoration = TextDecoration.LineThrough)
                        } else {
                            this
                        }
                    }

                withStyle(sectionTitleStyle) { append(themeColor.name) }
            },
            subtitle = themeColor.deprecated?.let { AnnotatedString("Deprecated: $it") },
            end = { if (themeColor.value.isSpecified) ColorShowcase(themeColor.value) },
        )
    }

    @Composable
    private fun ColorShowcase(color: Color) = Box(
        modifier = Modifier
            .requiredSize(40.dp)
            .border(1.dp, color = Flamingo.colors.outlineDark, shape = CircleShape)
            .background(color, shape = CircleShape)
    )

    private companion object {
        private const val ROOT_NODE_NAME = "O(EFUY(S*DEFOIUF"
        private val DEPTH_PADDING = 12.dp
    }
}
