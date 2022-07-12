package com.flamingo.crab.codegen

import com.flamingo.crab.THREE_QUOTES
import com.flamingo.crab.escapeForSourceCode
import com.google.devtools.ksp.symbol.KSName

@Suppress("LongMethod")
internal fun registry(records: List<String>): String {
    return """// This file was generated using crab. 
// Please, refer to https://todo.ru/x/YA6oQwE for more details.
        
package com.flamingo.crab

import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import com.flamingo.annotations.FlamingoComponent
import java.net.URL
import com.flamingo.view.components.FlamingoComponent as FlamingoComponentInterface
import com.flamingo.InternalComponents
import com.theater.TheaterPackage
import com.flamingo.Flamingo
import kotlin.reflect.KClass

/**
 * For more info about this class, see [FlamingoComponent] docs.
 * 
 * @property displayName of the component. If not explicitly specified in 
 * [FlamingoComponent.displayName], name of the function is used
 * @property demo fully qualified names of demo [Fragment]s
 * @property usedInsteadOf fully qualified names of functions that this component must replace, 
 * meaning that this component must be used instead of those functions. See
 * [com.flamingo.annotations.UsedInsteadOf]
 * @property supportsWhiteMode true, if component supports [Flamingo.LocalWhiteMode]
 * @property internal true, if component cannot be used outside of flamingo library. See
 * [InternalComponents]
 * @property funName fully qualified name of the function
 * @property docs KDocs of the function, if present
 * @property samples, referenced in [docs]
 * 
 * [Documentation](https://todo.ru/x/YA6oQwE)
 */
public class FlamingoComponentRecord(
    val displayName: String,
    val preview: @Composable () -> Unit,
    val figma: URL?,
    val specification: URL?,
    val viewImplementation: KClass<out FlamingoComponentInterface>? = null,
    val theaterPackage: KClass<out TheaterPackage>? = null,
    val demo: List<String>,
    val usedInsteadOf: List<String>,
    val supportsWhiteMode: Boolean,
    val internal: Boolean,
    
    val funName: String,
    val docs: String?,
    val samples: List<SampleRecord>,
)

/**
 * Sample, referenced in KDocs of [FlamingoComponent]
 * 
 * @property sourceCode of the file, containing a sample
 */
public class SampleRecord(
    val funName: String,
    val sourceCode: String?,
    val content: (@Composable () -> Unit)?,
)

/**
 * Contains metadata info about [FlamingoComponent]s, gathered by crab. For more info,
 * see [FlamingoComponent].
 * 
 * [Documentation](https://todo.ru/x/YA6oQwE)
 */
public object FlamingoRegistry {
    public val components: List<FlamingoComponentRecord> = listOf(
        ${records.joinToString()}
    )
}"""
}

internal fun componentRecord(
    funName: KSName,
    docs: String?,
    samples: List<String>,
    annSource: String,
): String {
    val docs =
        if (docs != null) "$THREE_QUOTES${docs.escapeForSourceCode()}$THREE_QUOTES" else "null"
    return """
    FlamingoComponentRecord(
        $annSource
        funName = "${funName.asString()}",
        docs = $docs,
        samples = listOf(${samples.joinToString(separator = ",")}),
    )
""".trimIndent()
}

internal fun sample(
    funName: String,
    sourceCode: String?,
    composableFunName: KSName?,
): String {
    val sourceCode = buildString {
        if (sourceCode != null) {
            append("\"\"\"")
            append(sourceCode.escapeForSourceCode())
            append("\"\"\"")
        } else append("null")
    }
    val composableLambda =
        if (composableFunName == null) "null" else "{${composableFunName.asString()}()}"
    return "SampleRecord(funName=\"$funName\",sourceCode=$sourceCode,content=$composableLambda)"
}
