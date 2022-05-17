package com.flamingo.playground.view.theme

import android.content.res.ColorStateList
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.flamingo.playground.R
import com.flamingo.playground.context
import com.flamingo.playground.databinding.ItemThemeAttrDocBinding
import com.flamingo.playground.setForceDarkOnWebView
import com.flamingo.utils.isNightMode
import com.flamingo.utils.resolveColorAttr
import java.util.Locale

class AttrDocsAdapter(
    @ColorInt textColorSecondary: Int,
    private val attrDocs: List<AttrDoc>,
) : RecyclerView.Adapter<AttrDocsAdapter.ViewHolder>() {

    private val webViewTextColor: String = getWebViewTextColor(textColorSecondary)

    /** Swaps alpha from the front to the back, [https://stackoverflow.com/a/21917994] */
    private fun getWebViewTextColor(textColorSecondary: Int): String {
        val hex = Integer.toHexString(textColorSecondary)
        return "#${hex.drop(2)}${hex.take(2)}"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        b = ItemThemeAttrDocBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        webViewTextColor = webViewTextColor
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(attrDocs[position])
    }

    override fun getItemCount() = attrDocs.size

    class ViewHolder(
        private val b: ItemThemeAttrDocBinding,
        private val webViewTextColor: String,
    ) : RecyclerView.ViewHolder(b.root) {

        fun bind(attrDoc: AttrDoc) {
            b.attrName.text = if (attrDoc.deprecated) {
                attrDoc.attrName + " (DEPRECATED)"
            } else attrDoc.attrName
            bindAttrType(attrDoc)

            b.explanationWebView.isVisible = attrDoc.explanationInHtml
            b.explanationTextView.isVisible = !attrDoc.explanationInHtml
            if (attrDoc.explanationInHtml) {
                bindWebView(attrDoc)
            } else {
                b.explanationTextView.text = Html.fromHtml(attrDoc.explanation)
                b.explanationTextView.movementMethod = LinkMovementMethod.getInstance()
            }
        }

        private fun bindAttrType(attrDoc: AttrDoc) = when (attrDoc.type) {
            AttrType.COLOR -> {
                val attrType = context.getString(R.string.theme_demo_attr_type_color)
                @AttrRes val attr = context.resources
                    .getIdentifier(attrDoc.attrName, "attr", context.packageName)
                val color = resolveColorAttr(attr)
                val hexColor = "#" + Integer.toHexString(color).uppercase(Locale.US)

                b.attrType.text = context
                    .getString(R.string.theme_demo_attr_type_and_value, attrType, hexColor)

                b.colorPreview.backgroundTintList = ColorStateList.valueOf(color)
            }
            AttrType.OTHER -> {
                val attrType = context.getString(R.string.theme_demo_attr_type_other)
                b.attrType.text = context.getString(R.string.theme_demo_attr_type, attrType)
                b.colorPreview.backgroundTintList = null
            }
        }

        private fun bindWebView(attrDoc: AttrDoc) = with(b.explanationWebView) {
            settings.loadsImagesAutomatically = true
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            settings.defaultTextEncodingName = "UTF-8"
            context.setForceDarkOnWebView(this)
            loadData(
                buildString {
                    append("<body style=\"margin: 0; padding: 0; ")
                    if (!isNightMode()) append("color: $webViewTextColor;")
                    append("\">")
                    append(attrDoc.explanation.replace("\n", "<br>"))
                    append("</body>")
                }, "text/html; charset=utf-8", "UTF-8"
            )
        }
    }
}
