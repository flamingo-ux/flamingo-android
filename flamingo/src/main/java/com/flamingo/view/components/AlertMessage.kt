package com.flamingo.view.components

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.text.SpannedString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.ColorUtils
import androidx.core.text.getSpans
import androidx.core.view.isVisible
import com.flamingo.R
import com.flamingo.annotations.view.FlamingoComponent
import com.flamingo.hexp
import com.flamingo.initUnitConversionsInCustomView
import com.flamingo.utils.UnitConversions.dp
import com.flamingo.utils.resolveColorAttr
import com.flamingo.view.VIEW_DEPRECATION_MSG
import androidx.appcompat.R as AppCompatR

@FlamingoComponent(
    displayName = "Alert Message",
    docs = """<h5></h5>
        <p>Alert message это контейнер с текстом, полупрозрачным фоном, иконкой и опциональным
        крестиком, предназначенный для уведомления пользователя о чём-либо.</p> 
        
        <p>Может быть 4 варианта (с соответствующим цветом фона)</p>
        <code>
        <ol>
            <li>Info</li>
            <li>Warning</li>
            <li>Success</li>
            <li>Error</li>
        </ol>
        </code>
        <br>
    """,
    preview = "com.flamingo.playground.preview.AlertMessagePreview",
    figmaUrl = "https://f.com/file/sPbkUbBGkp5Mstc0IQYubk/4.1.-UI-Android-kit?node-id=839" +
            "%3A681",
    permittedXmlAttributes = ["app:ds_variant", "app:ds_message"],
    theDemos = [
        "com.flamingo.playground.components.AlertMessageTypicalUsage",
        "com.flamingo.playground.components.AlertMessageStatesPlayroom",
    ]
)
@Deprecated(VIEW_DEPRECATION_MSG)
public class AlertMessage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(ContextThemeWrapper(context, R.style.Theme_Flamingo), attrs, defStyleAttr),
    com.flamingo.view.components.FlamingoComponent {

    public inner class Accessor internal constructor() :
        com.flamingo.view.components.FlamingoComponent.Accessor {
        /**
         * Color that can be used to highlight parts of the the [SpannedString] message on
         * [AlertMessage].
         *
         * Changes with the [variant].
         */
        @ColorInt
        public fun getTextHighlightColor(): Int = this@AlertMessage.textHighlightColor

        /**
         * One of: [AlertMessage.VARIANT_INFO], [AlertMessage.VARIANT_WARNING],
         * [AlertMessage.VARIANT_SUCCESS], [AlertMessage.VARIANT_ERROR],
         */
        public var variant: Int
            get() = this@AlertMessage.variant
            set(value) {
                this@AlertMessage.variant = value
            }

        public fun onCloseClick(onCloseListener: OnClickListener?) {
            this@AlertMessage.onCloseClick(onCloseListener)
        }

        public fun setMessage(@StringRes msg: Int): Unit = this@AlertMessage.setMessage(msg)
        public fun setMessage(msg: String): Unit = this@AlertMessage.setMessage(msg)
        public fun setMessage(msg: SpannedString): Unit = this@AlertMessage.setMessage(msg)
    }

    override val ds: Accessor = Accessor()

    private var message = ""
    private val messageView: TextView
    private val iconView: ImageView
    private val closeButton: View

    @ColorInt
    internal var textHighlightColor: Int = 0
        private set

    init {
        initUnitConversionsInCustomView()
        inflate(context, R.layout.alert_message, this)
        iconView = findViewById(R.id.icon)
        messageView = findViewById(R.id.message)
        closeButton = findViewById(R.id.btn_close)

        context.withStyledAttributes(attrs, R.styleable.AlertMessageView) {
            variant = getInt(R.styleable.AlertMessageView_ds_variant, DEFAULT)
            message = getString(R.styleable.AlertMessageView_ds_message) ?: ""
        }
        setMessage(message)
        requireNotNull(fontSetter, { NO_FONT_SETTER_ERROR }).setFont(messageView)
    }

    private var onCloseClick: OnClickListener? = null
        set(value) {
            field = value
            if (value != null) {
                closeButton.setOnClickListener { value.onClick(this) }
            } else {
                closeButton.setOnClickListener(null)
            }
            closeButton.isVisible = value != null
            field = value
        }

    internal fun onCloseClick(onCloseListener: OnClickListener?) {
        this.onCloseClick = onCloseListener
    }

    internal var variant: Int = DEFAULT
        set(value) {
            field = value
            textHighlightColor = resolveColorAttr(colorAttr(value))
            iconView.setImageResource(
                when (value) {
                    VARIANT_INFO, VARIANT_SUCCESS -> R.drawable.ds_ic_info
                    VARIANT_WARNING, VARIANT_ERROR -> R.drawable.ds_ic_alert_circle
                    else -> wrongVariant(value)
                }
            )
            iconView.setColorFilter(textHighlightColor)
            updateBackground()
        }

    internal fun setMessage(@StringRes msg: Int) {
        messageView.setText(msg)
    }

    internal fun setMessage(msg: String) {
        messageView.text = msg
    }

    internal fun setMessage(msg: SpannedString) {
        messageView.setText(msg, TextView.BufferType.SPANNABLE)
        if (msg.getSpans<ClickableSpan>().isNotEmpty()) {
            messageView.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun updateBackground() {
        val cr = context.resources.getDimension(R.dimen.corner_radius_big)
        background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii = floatArrayOf(cr, cr, cr, cr, cr, cr, cr, cr)
            val bgColor = ColorUtils.setAlphaComponent(textHighlightColor, ALPHA_BACKGROUND.hexp)
            setColor(bgColor)
            val strokeColor = ColorUtils.setAlphaComponent(textHighlightColor, ALPHA_OUTLINE.hexp)
            setStroke(1.dp, strokeColor)
        }
    }

    private fun colorAttr(variant: Int) = when (variant) {
        VARIANT_INFO -> R.attr.colorInfo
        VARIANT_WARNING -> R.attr.colorWarning
        VARIANT_SUCCESS -> R.attr.colorSuccess
        VARIANT_ERROR -> AppCompatR.attr.colorError
        else -> wrongVariant(variant)
    }

    private fun wrongVariant(variant: Int): Nothing {
        throw IllegalArgumentException("Unknown variant constant: $variant")
    }

    public companion object {
        public fun interface FontSetter {
            public fun setFont(textView: TextView)
        }

        /**
         * Must be set before using the component. Example:
         * ```
         * AlertMessage.fontSetter = FontSetter { textView ->
         *     textView.dsTextStyle(R.style.TextStyle_Flamingo_Body1)
         * }
         * ```
         */
        public var fontSetter: FontSetter? = null
        private const val NO_FONT_SETTER_ERROR =
            "Button.Companion.fontSetter must be set before using the component. To set it, " +
                    "initRobotoTypography() or initSbsansTypography() must be called"

        public const val VARIANT_INFO: Int = 0
        public const val VARIANT_WARNING: Int = 1
        public const val VARIANT_SUCCESS: Int = 2
        public const val VARIANT_ERROR: Int = 3

        private const val DEFAULT = VARIANT_INFO

        private const val ALPHA_BACKGROUND = 8
        private const val ALPHA_OUTLINE = 16
    }
}
