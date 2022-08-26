package com.flamingo.playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.flamingo.Flamingo
import com.flamingo.components.Text
import com.flamingo.loremIpsum
import com.flamingo.theme.typography.FlamingoTypography
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class FontsDemoFragment : Fragment() {

    private val properties = run {
        val props = FlamingoTypography::class.declaredMemberProperties
        FlamingoTypography::class
            .primaryConstructor!!
            .parameters.map { param -> props.find { it.name == param.name }!! }
            .filter { it.visibility == KVisibility.PUBLIC }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = Compose {
        val typography = Flamingo.typography
        val fontStyles = remember(typography) {
            properties.map {
                it.name.replaceFirstChar { it.titlecase() } to it.get(typography) as TextStyle
            }
        }
        val exampleText = remember { loremIpsum(2) }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(fontStyles) { (name, fontStyle) ->
                Text(
                    text = if (name.contains("display", ignoreCase = true)) name
                    else "$name: $exampleText",
                    style = fontStyle
                )
            }
        }
    }
}
