@file:Suppress("MatchingDeclarationName")

package com.flamingo.playground

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.flamingo.InternalComponents
import com.flamingo.components.CircularLoader
import com.flamingo.components.CircularLoaderColor
import com.flamingo.components.CircularLoaderSize
import com.flamingo.playground.utils.Boast
import com.flamingo.theme.FlamingoTheme
import com.flamingo.utils.SDK_29
import com.flamingo.utils.isNightMode
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

/**
 * Example:
 * ```
 * val muffin = "chocolate delight"
 * ```
 * Property:
 * ```
 * Property(name="muffin", value="chocolate delight")
 * ```
 * @param name programmer-defined identificator of the kotlin property
 * @param value of the property
 */
data class Property<T>(val name: String, val value: T)

/**
 * Obtains a list of properties declared in a class [CLASS], whose names start with [prefix],
 * and their values, **unsafely** casted to [PROP_VAL].
 *
 * Useful to dynamically collect custom android [View] int fields, which are used instead of enums.
 *
 * @param obj instance of the [CLASS], used to obtain property values at runtime
 * @param prefix (optional) if non-null, returned list will contain only properties with names that
 * start with [prefix]
 * @param PROP_VAL all property values will be **unsafely** cast to [PROP_VAL]
 */
inline fun <reified CLASS : Any, PROP_VAL : Any?> getProperties(
    obj: CLASS,
    prefix: String? = null,
): List<Property<PROP_VAL>> {
    return CLASS::class.memberProperties
        .run { if (prefix == null) this else filter { it.name.startsWith(prefix) } }
        .map { Property(it.name, it.get(obj) as PROP_VAL) }
}

/**
 * On [SDK_29] an higher system will make [WebView] content dark, if system dark mode is enabled
 */
@SuppressLint("WrongConstant")
fun Context.setForceDarkOnWebView(webView: WebView) {
    if (Build.VERSION.SDK_INT < SDK_29) return
    webView.settings.forceDark =
        if (isNightMode()) WebSettings.FORCE_DARK_ON else WebSettings.FORCE_DARK_OFF
}

@Suppress("FunctionName")
fun Fragment.Compose(
    applyDebugColor: Boolean = false,
    content: @Composable () -> Unit,
): ComposeView = ComposeView(requireContext()).apply {
    setContent { FlamingoTheme(applyDebugColor = applyDebugColor) { content() } }
}

val RecyclerView.ViewHolder.context: Context get() = itemView.context

inline fun <reified T> Fragment.fromArguments(key: String): T? {
    return arguments?.get(key) as T?
}

private val emulatorProducts = listOf(
    "sdk_google", "google_sdk", "sdk", "sdk_x86", "vbox86p", "emulator", "simulator"
)

fun isEmulator(): Boolean {
    return Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") ||
            Build.FINGERPRINT.startsWith("generic") ||
            Build.FINGERPRINT.startsWith("unknown") ||
            Build.HARDWARE.contains("goldfish") ||
            Build.HARDWARE.contains("ranchu") ||
            Build.MODEL.contains("google_sdk") ||
            Build.MODEL.contains("Emulator") ||
            Build.MODEL.contains("Android SDK built for x86") ||
            Build.MANUFACTURER.contains("Genymotion") ||
            emulatorProducts.any { Build.PRODUCT.contains(it) }
}

@Composable
@ReadOnlyComposable
fun boast(msg: String = "onClick"): () -> Unit = LocalContext.current.boast(msg)
fun Context.boast(msg: String = "onClick"): () -> Unit = { showBoast(msg) }
fun Context.showBoast(msg: String = "onClick"): Unit = Boast.showText(this, msg, Toast.LENGTH_SHORT)

/** @see InternalComponents */
internal val internalComponents: InternalComponents by lazy {
    val className = "com.flamingo.InternalComponents"
    val field = Class.forName(className).getDeclaredField("INSTANCE")
    field.isAccessible = true
    field.get(null) as InternalComponents
}

internal fun Context.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}

@Composable
internal fun Loading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularLoader(size = CircularLoaderSize.LARGE, color = CircularLoaderColor.PRIMARY)
    }
}

/**
 * The list of __all__ (not just immediate, like in [KClass.sealedSubclasses]) subclasses if this
 * class is a sealed class, or an empty list otherwise
 */
internal val <T : Any> KClass<T>.deepSealedSubclasses: List<KClass<out T>>
    get() = if (!isSealed) emptyList() else deepSealedSubclasses() as List<KClass<out T>>

private fun KClass<*>.deepSealedSubclasses(): List<KClass<*>> =
    if (!isSealed) listOf(this)
    else sealedSubclasses.flatMap { it.deepSealedSubclasses() }
