package com.flamingo.view.components

import android.animation.AnimatorInflater
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.ColorUtils
import androidx.core.view.doOnAttach
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.flamingo.ALPHA_DISABLED
import com.flamingo.Flamingo
import com.flamingo.R
import com.flamingo.annotations.view.DsIconSet
import com.flamingo.annotations.view.FlamingoComponent
import com.flamingo.components.button.Button
import com.flamingo.components.button.ButtonColor
import com.flamingo.components.button.ButtonIconPosition
import com.flamingo.components.button.ButtonSize
import com.flamingo.components.button.ButtonVariant
import com.flamingo.components.button.ButtonWidthPolicy
import com.flamingo.featureflags.FeatureFlag
import com.flamingo.hexp
import com.flamingo.initUnitConversionsInCustomView
import com.flamingo.theme.FlamingoTheme
import com.flamingo.utils.SDK_23
import com.flamingo.utils.SDK_29
import com.flamingo.utils.UnitConversions.dp
import com.flamingo.utils.gone
import com.flamingo.utils.resolveColorAttr
import com.flamingo.utils.updateMargins
import com.flamingo.view.VIEW_DEPRECATION_MSG
import com.flamingo.view.requireDsIcon
import androidx.appcompat.R as AppCompatR

@FlamingoComponent(
    displayName = "Button",
    docs = """<h5></h5>
        <p><code>layout_width</code> может быть <code>match_parent</code> или 
        <code>wrap_content</code>. <code>layout_height</code> - только 
        <code>wrap_content</code>.</p> 

        <p>Если <code>icon</code> и <code>dropdown</code> установлены одновременно, то 
        <code>icon</code> скрывается.</p>

        <p>Если <code>loading = true</code>, то кнопка становится <code>disabled</code>, но геттер 
        <code>disabled</code> не знает об этом.</p>

        <p>Кнопка устанавливает <code>clipToPadding = false</code> у <code>ViewParent</code>, к 
        которому прикреплена.</p>
        <br>
    """,
    preview = "com.flamingo.playground.preview.ButtonPreview",
    figmaUrl = "https://www.todo.com/file/qVO8jDuABDK9vsuLqRXeMx/UI-kit?node-id=628%3A5",
    permittedXmlAttributes = [
        "app:ds_b_label",
        "app:ds_b_loading",
        "app:ds_b_size",
        "app:ds_b_color",
        "app:ds_b_variant",
        "app:ds_b_icon",
        "app:ds_b_singleLine",
        "app:ds_b_dropdown",
        "app:ds_b_disabled",
    ],
    theDemos = [
        "com.flamingo.playground.components.ButtonTypicalUsage",
        "com.flamingo.playground.components.ButtonStatesPlayroom",
    ]
)
@Suppress("TooManyFunctions")
@Deprecated(VIEW_DEPRECATION_MSG)
public class Button @JvmOverloads constructor(
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

        public var label: String
            get() = this@Button.label
            set(value) = render(label = value)

        /** If true, button wil become [disabled], but [disabled] will not reflect this change */
        public var loading: Boolean
            get() = this@Button.loading
            set(value) = render(loading = value)

        public var size: Int
            get() = this@Button.size
            set(value) = render(size = value)

        public var color: Int
            get() = this@Button.color
            set(value) = render(color = value)

        public var variant: Int
            get() = this@Button.variant
            set(value) = render(variant = value)

        @DsIconSet
        public var icon: Int
            get() = this@Button.icon
            set(value) = render(icon = value)

        /** ButtonIconPosition.END is supported only in Compose implementation of the Button */
        public var iconPosition: ButtonIconPosition
            get() = this@Button.iconPosition
            set(value) = render(iconPosition = value)

        public var singleLine: Boolean
            get() = this@Button.singleLine
            set(value) = render(singleLine = value)

        /** Honored only when [loading] is false */
        public var disabled: Boolean
            get() = this@Button.disabled
            set(value) = render(disabled = value)

        public fun setOnClickListener(listener: OnClickListener?) {
            this@Button.setOnClickListener(listener)
        }

        public fun setOnLongClickListener(listener: OnLongClickListener?) {
            this@Button.setOnLongClickListener(listener)
        }
    }

    override val ds: Accessor = Accessor()

    internal var label: String = ""
    internal var loading: Boolean = false
    internal var size: Int = 0
    internal var color: Int = 0
    internal var variant: Int = 0

    @DsIconSet
    internal var icon: Int = 0
    internal var iconPosition: ButtonIconPosition = ButtonIconPosition.START
    internal var singleLine: Boolean = false
    internal var dropdown: Int = 0
    internal var disabled: Boolean = false

    private lateinit var iconView: View
    private lateinit var textView: TextView
    private lateinit var progressView: ProgressBar
    private lateinit var dropdownView: View
    private lateinit var nonLoadingContainerView: LinearLayout

    private lateinit var rippleDrawable: RippleDrawable
    private lateinit var backgroundDrawable: GradientDrawable
    private lateinit var composeView: ComposeView

    private val whiteStateList = ColorStateList.valueOf(Color.WHITE)
    private val colorControlHighlight =
        ColorStateList.valueOf(resolveColorAttr(android.R.attr.colorControlHighlight))
    private val textColorPrimaryStateList =
        ColorStateList.valueOf(resolveColorAttr(android.R.attr.textColorPrimary))

    init {
        initUnitConversionsInCustomView()
        inflate(context, R.layout.button, this)
        bindChildViews()
        if (FeatureFlag.BUTTON_COMPOSE_IMPL()) {
            gone(progressView, nonLoadingContainerView)
            composeView = ComposeView(context)
            addView(composeView)
            @Suppress("MagicNumber") composeView.updatePadding(2.dp, 2.dp, 2.dp, 3.dp)
        } else {
            backgroundDrawable = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = CORNER_RADIUS.dp
            }
            val mask = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = CORNER_RADIUS.dp
                setColor(MASK_COLOR.toInt()) // irrelevant, ignored
            }
            rippleDrawable = RippleDrawable(colorControlHighlight, backgroundDrawable, mask)
            background = rippleDrawable
            isFocusable = true

            doOnAttach {
                (parent as? ViewGroup ?: return@doOnAttach).clipToPadding = false
                stateListAnimator = if (Build.VERSION.SDK_INT >= SDK_23) {
                    R.animator.button_state_list_anim
                } else {
                    // on API 21 and API 22 there is a JNI CRASH
                    // more: https://stackoverflow.com/q/40928788
                    R.animator.button_state_list_anim_v21
                }.let { AnimatorInflater.loadStateListAnimator(context, it) }
            }
        }

        context.withStyledAttributes(attrs, R.styleable.Button) {
            val label = getString(R.styleable.Button_ds_b_label) ?: ""
            val loading = getBoolean(R.styleable.Button_ds_b_loading, false)
            val size = getInt(R.styleable.Button_ds_b_size, SIZE_MEDIUM)
            val color = getInt(R.styleable.Button_ds_b_color, COLOR_DEFAULT)
            val variant = getInt(R.styleable.Button_ds_b_variant, VARIANT_CONTAINED)
            val icon = getResourceId(R.styleable.Button_ds_b_icon, 0)
            val singleLine = getBoolean(R.styleable.Button_ds_b_singleLine, false)
            val dropdown = getInt(R.styleable.Button_ds_b_dropdown, DROPDOWN_HIDE)
            val disabled = getBoolean(R.styleable.Button_ds_b_disabled, false)

            if (Build.VERSION.SDK_INT >= SDK_29) {
                saveAttributeDataForStyleable(
                    context, R.styleable.Button, attrs, this, defStyleAttr, defStyleRes
                )
            }

            render(
                label = label, loading = loading, size = size, color = color, variant = variant,
                icon = icon, singleLine = singleLine, dropdown = dropdown, disabled = disabled,
                fullRender = true
            )
        }
        // block custom size (from xml and code)
        doOnAttach { updateLayoutParams { height = ViewGroup.LayoutParams.WRAP_CONTENT } }
    }

    private fun bindChildViews() {
        iconView = findViewById(R.id.iconView)
        textView = findViewById(R.id.textView)
        progressView = findViewById(R.id.progressView)
        dropdownView = findViewById(R.id.dropdownView)
        nonLoadingContainerView = findViewById(R.id.nonLoadingContainer)
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
        label: String = this.label,
        loading: Boolean = this.loading,
        size: Int = this.size,
        color: Int = this.color,
        variant: Int = this.variant,
        @DsIconSet icon: Int = this.icon,
        iconPosition: ButtonIconPosition = this.iconPosition,
        singleLine: Boolean = this.singleLine,
        dropdown: Int = this.dropdown,
        disabled: Boolean = this.disabled,
        fullRender: Boolean = false,
    ) {
        requireValidArguments(size, color, variant, dropdown)
        if (icon != 0) requireDsIcon(icon)

        if (FeatureFlag.BUTTON_COMPOSE_IMPL()) {
            composeView.setContent {
                ComposeButton(
                    label, icon, iconPosition, size, variant, color, loading, disabled, singleLine,
                )
            }
        } else {
            require(iconPosition == ButtonIconPosition.START) {
                "ButtonIconPosition.END is supported only in Compose implementation of the Button"
            }
            if (fullRender || disabled != this.disabled || loading != this.loading) {
                renderDisabled(loading || disabled)
            }
            if (fullRender || loading != this.loading) renderLoading(loading)
            if (fullRender || size != this.size) renderSize(size)
            if (fullRender || size != this.size) renderFont(size)
            if (fullRender || variant != this.variant || color != this.color) {
                renderOnColor(variant, color)
                renderBackground(variant, color)
            }
            if (fullRender || label != this.label) textView.text = label
            if (fullRender || icon != this.icon || dropdown != this.dropdown) {
                renderIcon(icon, dropdown)
            }
            if (fullRender || singleLine != this.singleLine) renderSingleLine(singleLine)
            if (fullRender || dropdown != this.dropdown) renderDropdown(dropdown)

            /**
             * For example, if icon changed, but only from one icon to another (icon is still
             * present and != 0), then there is no need to [renderSpacings]. Same with [dropdown].
             */
            val needToRenderSpacings = fullRender ||
                    (icon != 0) != (this.icon != 0) ||
                    (dropdown != DROPDOWN_HIDE) != (this.dropdown != DROPDOWN_HIDE) ||
                    variant != this.variant ||
                    size != this.size

            if (needToRenderSpacings) {
                renderSpacings(icon != 0, dropdown != DROPDOWN_HIDE, variant, size)
            }
        }

        updateInternalState(
            label, loading, size, color, variant, icon, iconPosition, singleLine, dropdown, disabled
        )
    }

    @Suppress("LongParameterList", "ComplexMethod", "FunctionNaming")
    @Composable
    private fun ComposeButton(
        label: String,
        icon: Int,
        iconPosition: ButtonIconPosition,
        size: Int,
        variant: Int,
        color: Int,
        loading: Boolean,
        disabled: Boolean,
        singleLine: Boolean,
    ) {
        FlamingoTheme {
            Button(
                onClick = { callOnClick() },
                label = label,
                icon = if (icon == 0) null else Flamingo.icons.fromId(icon),
                iconPosition = iconPosition,
                size = when (size) {
                    SIZE_SMALL -> ButtonSize.SMALL
                    SIZE_MEDIUM -> ButtonSize.MEDIUM
                    SIZE_LARGE -> ButtonSize.LARGE
                    else -> wrongSize(size)
                },
                variant = when (variant) {
                    VARIANT_CONTAINED -> ButtonVariant.CONTAINED
                    VARIANT_TEXT -> ButtonVariant.TEXT
                    else -> wrongVariant(variant)
                },
                color = when (color) {
                    COLOR_DEFAULT -> ButtonColor.Default
                    COLOR_PRIMARY -> ButtonColor.Primary
                    COLOR_WARNING -> ButtonColor.Warning
                    COLOR_ERROR -> ButtonColor.Error
                    COLOR_WHITE -> ButtonColor.White
                    else -> wrongColor(color)
                },
                loading = loading,
                disabled = disabled,
                widthPolicy = if (singleLine) {
                    ButtonWidthPolicy.TRUNCATING
                } else {
                    ButtonWidthPolicy.MULTILINE
                },
            )
        }
    }

    @Suppress("LongParameterList")
    private fun requireValidArguments(size: Int, color: Int, variant: Int, dropdown: Int) {
        requireSize(size)
        requireColor(color)
        requireVariant(variant)
        requireDropdown(dropdown)
    }

    private fun requireSize(size: Int) = require(size in SIZE_SMALL..SIZE_LARGE) { wrongSize(size) }

    private fun requireColor(color: Int) = require(color in COLOR_DEFAULT..COLOR_WHITE) {
        wrongColor(color)
    }

    private fun requireVariant(variant: Int) = require(variant in VARIANT_TEXT..VARIANT_CONTAINED) {
        wrongVariant(variant)
    }

    private fun requireDropdown(dropdown: Int) {
        require(dropdown in DROPDOWN_HIDE..DROPDOWN_EXPAND) { wrongDropdown(dropdown) }
    }

    private fun renderDisabled(disabled: Boolean) {
        alpha = if (disabled) ALPHA_DISABLED else 1f
        isEnabled = !disabled
    }

    private fun renderLoading(loading: Boolean) {
        nonLoadingContainerView.isInvisible = loading
        progressView.isVisible = loading
    }

    private fun renderSingleLine(singleLine: Boolean) {
        textView.maxLines = if (singleLine) 1 else Int.MAX_VALUE
    }

    @Suppress("MagicNumber")
    private fun renderSize(size: Int) {
        val verticalMargin = when (size) {
            SIZE_SMALL -> 8.dp
            SIZE_MEDIUM -> 12.dp
            SIZE_LARGE -> 18.dp
            else -> wrongSize(size)
        }
        textView.updateMargins(top = verticalMargin, bottom = verticalMargin)
    }

    private fun renderFont(size: Int) {
        requireNotNull(fontSetter, { NO_FONT_SETTER_ERROR }).setFont(textView, size == SIZE_LARGE)
        val previousColor = textView.textColors
        textView.setTextColor(previousColor)
    }

    private fun renderOnColor(variant: Int, color: Int) {
        val colorStateList = when (variant) {
            VARIANT_CONTAINED -> when (color) {
                COLOR_DEFAULT -> textColorPrimaryStateList
                COLOR_WHITE -> ColorStateList.valueOf(resources.getColor(R.color.grey_850))
                COLOR_PRIMARY, COLOR_WARNING, COLOR_ERROR -> whiteStateList
                else -> wrongColor(color)
            }
            VARIANT_TEXT -> {
                when (color) {
                    COLOR_DEFAULT -> textColorPrimaryStateList
                    COLOR_PRIMARY ->
                        ColorStateList.valueOf(resolveColorAttr(AppCompatR.attr.colorPrimary))
                    COLOR_WARNING ->
                        ColorStateList.valueOf(resolveColorAttr(R.attr.colorWarning))
                    COLOR_ERROR ->
                        ColorStateList.valueOf(resolveColorAttr(AppCompatR.attr.colorError))
                    COLOR_WHITE -> ColorStateList.valueOf(Color.WHITE)
                    else -> wrongColor(color)
                }
            }
            else -> wrongVariant(variant)
        }
        iconView.backgroundTintList = colorStateList
        textView.setTextColor(colorStateList)
        progressView.indeterminateTintList = colorStateList
        dropdownView.backgroundTintList = colorStateList
    }

    private fun renderBackground(variant: Int, color: Int) {
        renderRippleColor(variant, color)
        renderBackgroundColor(variant, color)
    }

    private fun renderRippleColor(variant: Int, color: Int) = when (variant) {
        VARIANT_CONTAINED -> rippleDrawable.setColor(colorControlHighlight)
        VARIANT_TEXT -> {
            val rippleColor = if (color == COLOR_DEFAULT) {
                resolveColorAttr(android.R.attr.colorControlHighlight)
            } else {
                when (color) {
                    COLOR_PRIMARY -> resolveColorAttr(AppCompatR.attr.colorPrimary)
                    COLOR_WARNING -> resolveColorAttr(R.attr.colorWarning)
                    COLOR_ERROR -> resolveColorAttr(AppCompatR.attr.colorError)
                    COLOR_WHITE -> Color.WHITE
                    else -> wrongColor(color)
                }.let { ColorUtils.setAlphaComponent(it, RIPPLE_ALPHA.hexp) }
            }
            rippleDrawable.setColor(ColorStateList.valueOf(rippleColor))
        }
        else -> wrongVariant(variant)
    }

    private fun renderBackgroundColor(variant: Int, color: Int) {
        backgroundDrawable.color = when (variant) {
            VARIANT_CONTAINED -> when (color) {
                COLOR_DEFAULT -> resolveColorAttr(R.attr.colorBackgroundTextField)
                COLOR_PRIMARY -> resolveColorAttr(AppCompatR.attr.colorPrimary)
                COLOR_WARNING -> resolveColorAttr(R.attr.colorWarning)
                COLOR_ERROR -> resolveColorAttr(AppCompatR.attr.colorError)
                COLOR_WHITE -> Color.WHITE
                else -> wrongColor(color)
            }
            VARIANT_TEXT -> Color.TRANSPARENT
            else -> wrongVariant(variant)
        }.let(ColorStateList::valueOf)
    }

    private fun renderIcon(icon: Int, dropdown: Int) {
        iconView.isVisible = dropdown == DROPDOWN_HIDE && icon != 0
        if (icon == 0) return
        iconView.background = getDrawable(context, icon)
    }

    private fun renderDropdown(dropdown: Int) = with(dropdownView) {
        isVisible = dropdown != DROPDOWN_HIDE
        rotation = when (dropdown) {
            DROPDOWN_HIDE -> return
            DROPDOWN_EXPAND -> 0f
            DROPDOWN_COLLAPSE -> @Suppress("MagicNumber") 180f
            else -> wrongDropdown(dropdown)
        }
        dropdownView.background = getDrawable(context, R.drawable.ds_ic_arrow_drop_down)
    }

    /**
     * @param icon true, if icon is shown, false - if hidden
     * @param dropdown true, if dropdown is shown, false - if hidden
     */
    @Suppress("ComplexMethod", "MagicNumber")
    private fun renderSpacings(icon: Boolean, dropdown: Boolean, variant: Int, size: Int) {
        @Dimension(unit = Dimension.DP) var textViewLeftMarginDp = 0
        @Dimension(unit = Dimension.DP) var textViewRightMarginDp = 0
        @Dimension(unit = Dimension.DP) var iconViewLeftMarginDp = 0
        @Dimension(unit = Dimension.DP) var dropdownViewRightMarginDp = 0
        val sizeLarge = size == SIZE_LARGE
        when (variant) {
            VARIANT_TEXT -> {
                textViewLeftMarginDp = if (sizeLarge) 16 else 8
                textViewRightMarginDp = if (sizeLarge) 16 else 8
                if (dropdown) {
                    textViewRightMarginDp = 4
                    dropdownViewRightMarginDp = if (sizeLarge) 16 else 4
                } else if (icon) {
                    iconViewLeftMarginDp = if (sizeLarge) 12 else 8
                }
            }
            VARIANT_CONTAINED -> {
                textViewLeftMarginDp = if (sizeLarge) 20 else 16
                textViewRightMarginDp = if (sizeLarge) 20 else 16
                if (dropdown) {
                    textViewRightMarginDp = 4
                    dropdownViewRightMarginDp = if (sizeLarge) 16 else 12
                } else if (icon) {
                    textViewLeftMarginDp = 8
                    iconViewLeftMarginDp = if (sizeLarge) 16 else 12
                }
            }
        }
        textView.updateMargins(
            left = textViewLeftMarginDp.dp,
            right = textViewRightMarginDp.dp
        )
        iconView.updateMargins(left = iconViewLeftMarginDp.dp)
        dropdownView.updateMargins(right = dropdownViewRightMarginDp.dp)
    }

    @Suppress("LongParameterList")
    private fun updateInternalState(
        label: String,
        loading: Boolean,
        size: Int,
        color: Int,
        variant: Int,
        icon: Int,
        iconPosition: ButtonIconPosition,
        singleLine: Boolean,
        dropdown: Int,
        disabled: Boolean,
    ) {
        this.label = label
        this.loading = loading
        this.size = size
        this.color = color
        this.variant = variant
        this.icon = icon
        this.iconPosition = iconPosition
        this.singleLine = singleLine
        this.dropdown = dropdown
        this.disabled = disabled
    }

    private fun wrongColor(color: Int): Nothing = error("Wrong color: $color")
    private fun wrongSize(size: Int): Nothing = error("Wrong size: $size")
    private fun wrongVariant(variant: Int): Nothing = error("Wrong variant: $variant")
    private fun wrongDropdown(dropdown: Int): Nothing = error("Wrong dropdown: $dropdown")

    public companion object {
        public fun interface FontSetter {
            public fun setFont(textView: TextView, isLarge: Boolean)
        }

        /**
         * Must be set before using the component. Example:
         * ```
         * Button.fontSetter = FontSetter { textView, isLarge ->
         *     textView.dsTextStyle(
         *         if (isLarge) {
         *             R.style.TextStyle_Flamingo_Button
         *         } else {
         *             R.style.TextStyle_Flamingo_Button2
         *         }
         *     )
         * }
         * ```
         */
        public var fontSetter: FontSetter? = null
        private const val NO_FONT_SETTER_ERROR =
            "Button.Companion.fontSetter must be set before using the component. To set it, " +
                    "initRobotoTypography() or initSbsansTypography() must be called"

        public const val VARIANT_TEXT: Int = 0
        public const val VARIANT_CONTAINED: Int = 1

        public const val DROPDOWN_HIDE: Int = 0
        public const val DROPDOWN_COLLAPSE: Int = 1
        public const val DROPDOWN_EXPAND: Int = 2

        public const val COLOR_DEFAULT: Int = 0
        public const val COLOR_PRIMARY: Int = 1
        public const val COLOR_WARNING: Int = 2
        public const val COLOR_ERROR: Int = 3
        public const val COLOR_WHITE: Int = 4

        @Dimension(unit = Dimension.DP)
        public const val SIZE_SMALL: Int = 32

        @Dimension(unit = Dimension.DP)
        public const val SIZE_MEDIUM: Int = 40

        @Dimension(unit = Dimension.DP)
        public const val SIZE_LARGE: Int = 56

        @Dimension(unit = Dimension.DP)
        private const val CORNER_RADIUS = 12f
        private const val MASK_COLOR = 0xFF000000
        private const val RIPPLE_ALPHA = 12
    }
}
