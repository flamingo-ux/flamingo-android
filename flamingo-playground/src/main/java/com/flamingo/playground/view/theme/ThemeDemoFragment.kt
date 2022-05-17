package com.flamingo.playground.view.theme

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Xml
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import com.flamingo.playground.R
import com.flamingo.playground.databinding.ThemeDemoFragmentBinding
import com.flamingo.playground.utils.viewBinding
import com.flamingo.utils.resolveColorAttr
import java.io.InputStream

class ThemeDemoFragment : Fragment(R.layout.theme_demo_fragment) {

    private val b by viewBinding(ThemeDemoFragmentBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ThemeDemoFragmentBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launch {
            val attrDocs = withContext(Dispatchers.IO) {
                parseThemesFile(resources.assets.open("themes.xml"))
            } ?: return@launch
            val webViewTextColor = resolveColorAttr(android.R.attr.textColorSecondary)
            b.attrDocs.adapter = AttrDocsAdapter(webViewTextColor, attrDocs)
            b.docsTextView.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    companion object {
        internal fun parseThemesFile(inputStream: InputStream): List<AttrDoc>? = runCatching {
            inputStream.use {
                Xml.newPullParser().run {
                    setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                    setInput(inputStream, null)
                    readThemeAttrs()
                }
            }
        }.getOrNull()

        /** @return list of valid doc comments */
        private fun XmlPullParser.readThemeAttrs(): List<AttrDoc> {
            val entries = mutableListOf<AttrDoc>()
            while (nextToken() != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.COMMENT) {
                    val text = text ?: continue
                    // skip non-doc comments and incorrectly formatted doc comments
                    if (AttrDoc.isDocComment(text)) AttrDoc.fromDocComment(text)?.let(entries::add)
                }
            }
            return entries
        }
    }
}
