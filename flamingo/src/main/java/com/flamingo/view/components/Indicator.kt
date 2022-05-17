@file:Suppress("MagicNumber")

package com.flamingo.view.components

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.Dimension.DP
import androidx.annotation.Px
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.withStyledAttributes
import androidx.core.view.doOnAttach
import androidx.core.view.updateLayoutParams
import com.flamingo.R
import com.flamingo.annotations.view.FlamingoComponent
import com.flamingo.initUnitConversionsInCustomView
import com.flamingo.utils.UnitConversions.dp
import com.flamingo.utils.resolveColorAttr
import com.flamingo.view.VIEW_DEPRECATION_MSG
import androidx.appcompat.R as AppCompatR

@FlamingoComponent(
    displayName = "Indicator",
    docs = """<h5></h5>
        <p>Используется в компоненте <b>Avatar</b> как индикатор доступности пользователя</p>
        <br>
    """,
    preview = "com.flamingo.playground.preview.IndicatorPreview",
    figmaUrl = "https://www.todo.com/file/sPbkUbBGkp5Mstc0IQYubk/4.1.-UI-Android-kit?node-id=670" +
        "%3A8",
    permittedXmlAttributes = ["app:ds_i_size", "app:ds_i_color"],
    theDemos = [
        "com.flamingo.playground.components.IndicatorStatesPlayroom",
        "com.flamingo.playground.components.IndicatorTypicalUsage",
    ]
)
@Deprecated(VIEW_DEPRECATION_MSG)
public class Indicator @JvmOverloads public constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(ContextThemeWrapper(context, R.style.Theme_Flamingo), attrs, defStyleAttr),
    com.flamingo.view.components.FlamingoComponent {

    public inner class Accessor internal constructor() :
        com.flamingo.view.components.FlamingoComponent.Accessor {

        /** One of [Indicator].COLOR_* */
        public var color: Int
            get() = this@Indicator.color
            set(value) {
                this@Indicator.color = value
            }

        /** One of [Indicator].SIZE_* */
        public var size: Int
            get() = this@Indicator.size
            set(value) {
                this@Indicator.size = value
            }
    }

    override val ds: Accessor = Accessor()

    init {
        initUnitConversionsInCustomView()
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setColor(resolveColorAttr(AppCompatR.attr.colorPrimary))
        background = shape

        context.withStyledAttributes(attrs, R.styleable.Indicator) {
            isInitialization = true
            color = getInt(R.styleable.Indicator_ds_i_color, COLOR_PRIMARY)
            size = getInt(R.styleable.Indicator_ds_i_size, SIZE_BIG)
            isInitialization = false
        }
    }

    private var isInitialization: Boolean = false

    internal var color: Int = COLOR_PRIMARY
        set(value) {
            if (!isInitialization && value == field) return
            @ColorInt val color = when (value) {
                COLOR_PRIMARY -> resolveColorAttr(AppCompatR.attr.colorPrimary)
                COLOR_ERROR -> resolveColorAttr(AppCompatR.attr.colorError)
                COLOR_SECONDARY -> resolveColorAttr(R.attr.colorInfo)
                COLOR_WARNING -> resolveColorAttr(R.attr.colorWarning)
                else -> error("Unknown color option")
            }
            (background as GradientDrawable).apply { setColor(color) }
            field = value
        }

    internal var size: Int = SIZE_BIG
        set(value) {
            if (value == field) return
            updateSize(value)
            field = value
        }

    private fun updateSize(size: Int) {
        requireSize(size)
        @Px val size = size.dp
        doOnAttach { updateLayoutParams { width = size; height = size } }
    }

    private fun requireSize(size: Int) {
        require(size == SIZE_SMALL || size == SIZE_BIG) { "Unknown size option" }
    }

    public companion object {
        @Dimension(unit = DP)
        public const val SIZE_BIG: Int = 12

        @Dimension(unit = DP)
        public const val SIZE_SMALL: Int = 8

        public const val COLOR_PRIMARY: Int = 1
        public const val COLOR_ERROR: Int = 2
        public const val COLOR_SECONDARY: Int = 3
        public const val COLOR_WARNING: Int = 4
    }
}
