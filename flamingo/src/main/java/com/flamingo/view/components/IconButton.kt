package com.flamingo.view.components

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Dimension
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.withStyledAttributes
import androidx.core.view.doOnAttach
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.flamingo.Flamingo
import com.flamingo.R
import com.flamingo.annotations.view.DsIconSet
import com.flamingo.annotations.view.FlamingoComponent
import com.flamingo.components.IconButton
import com.flamingo.components.IconButtonColor
import com.flamingo.components.IconButtonSize
import com.flamingo.components.IconButtonVariant
import com.flamingo.initUnitConversionsInCustomView
import com.flamingo.theme.FlamingoTheme
import com.flamingo.utils.SDK_29
import com.flamingo.utils.UnitConversions.dp
import com.flamingo.view.VIEW_DEPRECATION_MSG
import com.flamingo.view.requireDsIcon

@FlamingoComponent(
    displayName = "Icon Button",
    docs = """<h5></h5>
        <p><code>layout_width</code> и <code>layout_height</code> могут быть только 
        <code>wrap_content</code>.</p>

        <p>Если <code>loading = true</code>, то кнопка становится <code>disabled</code>, но геттер 
        <code>disabled</code> не знает об этом.</p>

        <p>Кнопка устанавливает <code>clipToPadding = false</code> у <code>ViewParent</code>, к 
        которому прикреплена.</p>
        <br>
    """,
    preview = "com.flamingo.playground.preview.IconButtonPreview",
    figmaUrl = "https://www.todo.com/file/qVO8jDuABDK9vsuLqRXeMx/UI-kit?node-id=628%3A5",
    permittedXmlAttributes = [
        "app:ds_ib_loading",
        "app:ds_ib_size",
        "app:ds_ib_color",
        "app:ds_ib_variant",
        "app:ds_ib_icon",
        "app:ds_ib_disabled",
    ],
    theDemos = ["com.flamingo.playground.components.IconButtonStatesPlayroom"]
)
@Suppress("TooManyFunctions")
@Deprecated(VIEW_DEPRECATION_MSG)
public class IconButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(
    ContextThemeWrapper(context, R.style.Theme_Flamingo),
    attrs, defStyleAttr, defStyleRes
), com.flamingo.view.components.FlamingoComponent {

    public inner class Accessor internal constructor() :
        com.flamingo.view.components.FlamingoComponent.Accessor {

        /** If true, button wil become [disabled], but [disabled] will not reflect this change */
        public var loading: Boolean
            get() = this@IconButton.loading
            set(value) = render(loading = value)

        public var size: Int
            get() = this@IconButton.size
            set(value) = render(size = value)

        public var color: Int
            get() = this@IconButton.color
            set(value) = render(color = value)

        public var variant: Int
            get() = this@IconButton.variant
            set(value) = render(variant = value)

        /** Icon shows only when [dropdown] == [DROPDOWN_HIDE] */
        @DsIconSet
        public var icon: Int
            get() = this@IconButton.icon
            set(value) = render(icon = value)

        /** Honored only when [loading] is false */
        public var disabled: Boolean
            get() = this@IconButton.disabled
            set(value) = render(disabled = value)

        public fun setOnClickListener(listener: OnClickListener?) {
            this@IconButton.setOnClickListener(listener)
        }

        public fun setOnLongClickListener(listener: OnLongClickListener?) {
            this@IconButton.setOnLongClickListener(listener)
        }
    }

    override val ds: Accessor = Accessor()

    internal var loading: Boolean = false
    internal var size: Int = 0
    internal var color: Int = 0
    internal var variant: Int = 0

    @DsIconSet
    internal var icon: Int = 0
    internal var disabled: Boolean = false

    private val composeView = ComposeView(context)

    init {
        initUnitConversionsInCustomView()
        addView(composeView)
        doOnAttach { (parent as? ViewGroup ?: return@doOnAttach).clipToPadding = false }
        context.withStyledAttributes(attrs, R.styleable.IconButton) {
            val loading = getBoolean(R.styleable.IconButton_ds_ib_loading, false)
            val size = getInt(R.styleable.IconButton_ds_ib_size, SIZE_MEDIUM)
            val color = getInt(R.styleable.IconButton_ds_ib_color, COLOR_DEFAULT)
            val variant = getInt(R.styleable.IconButton_ds_ib_variant, VARIANT_CONTAINED)
            val icon = getResourceId(R.styleable.IconButton_ds_ib_icon, 0)
            val disabled = getBoolean(R.styleable.IconButton_ds_ib_disabled, false)

            if (Build.VERSION.SDK_INT >= SDK_29) {
                saveAttributeDataForStyleable(
                    context, R.styleable.IconButton, attrs, this, defStyleAttr, defStyleRes
                )
            }

            render(
                loading = loading, size = size, color = color, variant = variant, icon = icon,
                disabled = disabled, fullRender = true
            )
        }
        // block custom size (from xml and code)
        doOnAttach {
            updateLayoutParams {
                width = ViewGroup.LayoutParams.WRAP_CONTENT
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
    }

    /**
     * When [render] is called, method parameter values are new, and property values are old.
     * Property values are updated in [updateInternalState], thus creating an ability for [render]
     * to compare old and new values, deducing, what actually changed and needs an action.
     *
     * @param fullRender if true, previously described old-to-new-values comparison is skipped,
     * forcing a full re-render.
     */
    @Suppress("LongParameterList", "ComplexMethod")
    private fun render(
        loading: Boolean = this.loading,
        size: Int = this.size,
        color: Int = this.color,
        variant: Int = this.variant,
        @DsIconSet icon: Int = this.icon,
        disabled: Boolean = this.disabled,
        fullRender: Boolean = false,
    ) {
        requireValidArguments(size, color, variant)
        if (icon != 0) requireDsIcon(icon)

        @Suppress("MagicNumber") composeView.updatePadding(2.dp, 2.dp, 2.dp, 3.dp)
        composeView.setContent {
            FlamingoTheme {
                if (icon == 0) return@FlamingoTheme
                IconButton(
                    onClick = { callOnClick() },
                    icon = Flamingo.icons.fromId(icon),
                    size = when (size) {
                        SIZE_SMALL -> IconButtonSize.SMALL
                        SIZE_MEDIUM -> IconButtonSize.MEDIUM
                        else -> wrongSize(size)
                    },
                    variant = when (variant) {
                        VARIANT_CONTAINED -> IconButtonVariant.CONTAINED
                        VARIANT_TEXT -> IconButtonVariant.TEXT
                        else -> wrongVariant(variant)
                    },
                    color = when (color) {
                        COLOR_DEFAULT -> IconButtonColor.DEFAULT
                        COLOR_PRIMARY -> IconButtonColor.PRIMARY
                        COLOR_WARNING -> IconButtonColor.WARNING
                        COLOR_ERROR -> IconButtonColor.ERROR
                        COLOR_WHITE -> IconButtonColor.WHITE
                        else -> wrongColor(color)
                    },
                    loading = loading,
                    disabled = disabled,
                    contentDescription = "icon button",
                )
            }
        }
        updateInternalState(loading, size, color, variant, icon, disabled)
    }

    @Suppress("LongParameterList")
    private fun requireValidArguments(size: Int, color: Int, variant: Int) {
        requireSize(size)
        requireColor(color)
        requireVariant(variant)
    }

    private fun requireSize(size: Int) =
        require(size in SIZE_SMALL..SIZE_MEDIUM) { wrongSize(size) }

    private fun requireColor(color: Int) = require(color in COLOR_DEFAULT..COLOR_WHITE) {
        wrongColor(color)
    }

    private fun requireVariant(variant: Int) = require(variant in VARIANT_TEXT..VARIANT_CONTAINED) {
        wrongVariant(variant)
    }

    @Suppress("LongParameterList")
    private fun updateInternalState(
        loading: Boolean,
        size: Int,
        color: Int,
        variant: Int,
        icon: Int,
        disabled: Boolean,
    ) {
        this.loading = loading
        this.size = size
        this.color = color
        this.variant = variant
        this.icon = icon
        this.disabled = disabled
    }

    private fun wrongColor(color: Int): Nothing = error("Wrong color: $color")
    private fun wrongSize(size: Int): Nothing = error("Wrong size: $size")
    private fun wrongVariant(variant: Int): Nothing = error("Wrong variant: $variant")

    public companion object {
        public const val VARIANT_TEXT: Int = 0
        public const val VARIANT_CONTAINED: Int = 1

        public const val COLOR_DEFAULT: Int = 0
        public const val COLOR_PRIMARY: Int = 1
        public const val COLOR_WARNING: Int = 2
        public const val COLOR_ERROR: Int = 3
        public const val COLOR_WHITE: Int = 4

        @Dimension(unit = Dimension.DP)
        public const val SIZE_SMALL: Int = 32

        @Dimension(unit = Dimension.DP)
        public const val SIZE_MEDIUM: Int = 40
    }
}
