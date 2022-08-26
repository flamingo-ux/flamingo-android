@file:Suppress("MagicNumber")

package com.flamingo.view.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.Dimension.DP
import androidx.annotation.Dimension.SP
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.ColorUtils
import androidx.core.view.doOnAttach
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import coil.ImageLoader
import coil.imageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import coil.request.ImageResult
import com.flamingo.ALPHA_DISABLED
import com.flamingo.R
import com.flamingo.annotations.view.DsIconSet
import com.flamingo.annotations.view.FlamingoComponent
import com.flamingo.components.Avatar
import com.flamingo.hexp
import com.flamingo.initUnitConversionsInCustomView
import com.flamingo.utils.UnitConversions.dp
import com.flamingo.utils.UnitConversions.sp
import com.flamingo.utils.resolveColorAttr
import com.flamingo.view.AvatarTarget
import com.flamingo.view.VIEW_DEPRECATION_MSG
import com.flamingo.view.dsIcon
import com.google.android.material.card.MaterialCardView
import okhttp3.HttpUrl
import java.io.File
import kotlin.math.sqrt
import androidx.appcompat.R as AppCompatR

@FlamingoComponent(
    displayName = "Avatar",
    docs = """<h5></h5>
        <p>Может иметь 2 текстовых символа, иконку (из иконсета) или картинку. Соответствующие
        методы</p>
        <code>
        <ol>
            <li>setLetters</li>
            <li>setIcon</li>
            <li>setDrawable (loadDrawable для <i>coil</i>)</li>
        </ol>
        </code>
        
        <p>При первом вызове этих функций необходимо предоставить все аргументы (если нет xml 
        атрибутов), а при последующих вызовах можно опускать аргументы, имеющие дефолтные значения
        (ими являются текущие, установленные ранее, значения).</p>
        
        <p>Аргумент <code>letters</code> функции <code>setLetters</code> поддерживает <b>только</b>
        строки, длина которых ровно 2 символа.</p>
        
        <p>Функция <code>showIndicator</code> позволяет показать <code>Indicator</code> в правом
        нижнем углу <code>Avatar</code>, центр индикатора лежит на пересечении правой
        диагонали <code>Avatar</code>'а и его края. Перед вызовом <code>showIndicator</code>
        <b>необходимо</b> вызвать <code>setLetters</code>, <code>setIcon</code> или 
        <code>setDrawable</code>.</p>
        
        <p>Если размер <code>Avatar</code>'а <code>SIZE_24, SIZE_40, SIZE_56</code>, то размер
        <code>Indicator</code>'а <code>Indicator.SIZE_SMALL</code>, иначе - 
        <code>Indicator.SIZE_BIG</code>.</p>
        
        <p><b>Внимание!</b> <code>Indicator</code> поддерживается только, если перед вызовом 
        функции <code>showIndicator</code> была установлена <code>SHAPE_CIRCLE</code>. Иначе - 
        fatal exception.</p>
        
        <a href='https://youtu.be/SCmBIkDRTh4'>
        Получение формулы для расчёта положения Indicator (DS component) на Avatar</a>
        <br>
    """,
    preview = "com.flamingo.playground.preview.AvatarPreview",
    figmaUrl = "https://f.com/file/sPbkUbBGkp5Mstc0IQYubk/4.1.-UI-Android-kit?node-id=830" +
            "%3A17",
    permittedXmlAttributes = [
        "app:ds_a_letters",
        "app:ds_a_icon",
        "app:ds_a_drawable",
        "app:ds_a_background",
        "app:ds_a_size",
        "app:ds_a_shape",
    ],
    theDemos = [
        "com.flamingo.playground.components.AvatarStatesPlayroom",
        "com.flamingo.playground.components.AvatarTypicalUsage",
    ]
)
@Deprecated(VIEW_DEPRECATION_MSG)
public class Avatar @JvmOverloads public constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(ContextThemeWrapper(context, R.style.Theme_Flamingo), attrs, defStyleAttr),
    com.flamingo.view.components.FlamingoComponent {

    @Suppress("ChainWrapping")
    public inner class Accessor internal constructor() : com.flamingo.view.components
    .FlamingoComponent.AccessorWithComponentReference<Avatar>(this) {

        public var disabled: Boolean
            get() = this@Avatar.disabled
            set(value) {
                this@Avatar.disabled = value
            }

        public fun setLetters(
            letters: String = currentLetters,
            background: Int = currentBackground,
            shape: Int = currentShape,
            avatarSize: Int = currentSize,
        ): Unit = this@Avatar.setLetters(letters, background, shape, avatarSize)

        public fun setIcon(
            @DsIconSet iconRes: Int = currentIconRes,
            background: Int = currentBackground,
            shape: Int = currentShape,
            avatarSize: Int = currentSize,
        ): Unit = this@Avatar.setIcon(iconRes, background, shape, avatarSize)

        public fun setDrawable(
            drawable: Drawable = requireNotNull(currentBackgroundDrawable) {
                "It was a first call to the Avatar, you should have provided a drawable"
            },
            shape: Int = currentShape,
            avatarSize: Int = currentSize,
        ): Unit = this@Avatar.setDrawable(drawable, shape, avatarSize)

        /**
         * Loads an image into an [Avatar] using [coil].
         *
         * If you want to set shape and size (required, if you call this function for the first
         * time, just after creating an [Avatar]), use [shape] and [avatarSize].
         *
         * To use this function, [coil] dependency MUST BE added to the use-side gradle module.
         *
         * @param data The default supported data types are:
         * - [String] (mapped to a [Uri])
         * - [Uri] ("android.resource", "content", "file", "http", and "https" schemes only)
         * - [HttpUrl]
         * - [File]
         * - [DrawableRes]
         * - [Drawable]
         * - [Bitmap]
         */
        public inline fun loadDrawableAsync(
            data: Any,
            shape: Int = componentRef.currentShape,
            avatarSize: Int = componentRef.currentSize,
            imageLoader: ImageLoader = componentRef.context.imageLoader,
            builder: ImageRequest.Builder.() -> Unit = {},
        ): Disposable {
            val request = ImageRequest.Builder(componentRef.context)
                .data(data)
                .target(AvatarTarget(componentRef, shape, avatarSize))
                .apply(builder)
                .build()
            return imageLoader.enqueue(request)
        }

        /**  @see loadDrawableAsync */
        public suspend inline fun loadDrawable(
            data: Any,
            shape: Int = componentRef.currentShape,
            avatarSize: Int = componentRef.currentSize,
            imageLoader: ImageLoader = componentRef.context.imageLoader,
            builder: ImageRequest.Builder.() -> Unit = {},
        ): ImageResult {
            val request = ImageRequest.Builder(componentRef.context)
                .data(data)
                .target(AvatarTarget(componentRef, shape, avatarSize))
                .apply(builder)
                .build()
            return imageLoader.execute(request)
        }

        public fun setOnClickListener(onClickListener: OnClickListener?) {
            this@Avatar._onClickListener = onClickListener
        }

        /**
         * Supported only when [currentShape] is [SHAPE_CIRCLE]. Else, fatal exception will be
         * thrown.
         *
         * @param color one of [Indicator.COLOR_*]
         */
        public fun showIndicator(color: Int): Unit = this@Avatar.showIndicator(color)

        public fun hideIndicator(): Unit = this@Avatar.hideIndicator()
    }

    override val ds: Accessor = Accessor()

    private val background: ImageView
    private val foreground: ImageView
    private val letters: TextView
    private val indicator: Indicator
    private val card: MaterialCardView

    internal var currentLetters: String = NO_LETTERS
    internal var currentIconRes: Int = NO_ICON
    internal var currentBackgroundDrawable: Drawable? = null

    @PublishedApi
    internal var currentShape: Int = NO_SHAPE
    internal var currentBackground: Int = NO_BACKGROUND

    @PublishedApi
    internal var currentSize: Int = NO_SIZE

    @ColorInt
    private var currentForegroundTint: Int = Color.TRANSPARENT

    @ColorInt
    private var currentLettersTextColor: Int = Color.TRANSPARENT

    init {
        initUnitConversionsInCustomView()
        inflate(context, R.layout.avatar_layout, this)
        background = findViewById(R.id.background)
        foreground = findViewById(R.id.foreground)
        letters = findViewById(R.id.letters)
        indicator = findViewById(R.id.indicator)
        card = findViewById(R.id.card)

        context.withStyledAttributes(attrs, R.styleable.Avatar) {
            val disabled = getBoolean(R.styleable.Avatar_ds_a_disabled, false)
            this@Avatar.disabled = disabled

            val letters = getString(R.styleable.Avatar_ds_a_letters) ?: NO_LETTERS
            val icon = getResourceId(R.styleable.Avatar_ds_a_icon, NO_ICON)
            val drawable = getDrawable(R.styleable.Avatar_ds_a_drawable)
            val background = getResourceId(R.styleable.Avatar_ds_a_background, NO_BACKGROUND)
            val shape = getInt(R.styleable.Avatar_ds_a_shape, NO_SHAPE)
            val size = getInt(R.styleable.Avatar_ds_a_size, NO_SIZE)
            if (shape == NO_SHAPE || size == NO_SIZE) return@withStyledAttributes
            when {
                letters != NO_LETTERS -> {
                    if (background == NO_BACKGROUND) return@withStyledAttributes
                    setLetters(letters, background, shape, size)
                }
                icon != NO_ICON -> {
                    if (background == NO_BACKGROUND) return@withStyledAttributes
                    setIcon(icon, background, shape, size)
                }
                drawable != null -> setDrawable(drawable, shape, size)
            }
        }
        requireNotNull(fontSetter, { NO_FONT_SETTER_ERROR }).setFont(letters)
    }

    private var _onClickListener: OnClickListener? = null
        set(value) {
            if (value == field) return
            if (value != null) {
                setOnClickListener { if (!this.disabled) value.onClick(this) }
            } else {
                setOnClickListener(null)
            }
            field = value
            updateOnClickListener(_onClickListener, disabled)
        }

    internal var disabled: Boolean = false
        set(value) {
            if (value == field) return
            field = value
            updateOnClickListener(_onClickListener, disabled)
        }

    private fun updateOnClickListener(onClickListener: OnClickListener?, disabled: Boolean) {
        alpha = if (disabled) ALPHA_DISABLED else 1f
        card.foreground = if (onClickListener != null && !disabled) {
            getSelectableBackground()
        } else null
    }

    internal fun showIndicator(color: Int) {
        indicator.isVisible = true
        indicator.ds.color = color
        updateIndicator()
    }

    internal fun hideIndicator() {
        indicator.isVisible = false
    }

    internal fun setLetters(
        letters: String = currentLetters,
        background: Int = currentBackground,
        shape: Int = currentShape,
        avatarSize: Int = currentSize,
    ) {
        requireLetters(letters)
        requireBackground(background)
        requireShape(shape)
        requireSize(avatarSize)

        @ColorInt
        val newColor = when (background) {
            BACKGROUND_PRIMARY -> getColorPrimary()
            BACKGROUND_GREY -> getColorTextSecondary()
            else -> Color.WHITE
        }
        if (newColor != currentLettersTextColor) {
            this.letters.setTextColor(newColor)
            currentLettersTextColor = newColor
        }
        this.letters.isVisible = true
        foreground.isVisible = false
        if (letters != currentLetters) {
            this.letters.text = letters
            currentLetters = letters
        }
        if (avatarSize != currentSize) updateAvatarSize(avatarSize)
        if (currentBackground != background) updateBackground(background)
        if (shape != currentShape || avatarSize != currentSize) updateShape(shape, avatarSize)
        currentSize = avatarSize
    }

    internal fun setIcon(
        @DsIconSet iconRes: Int = currentIconRes,
        background: Int = currentBackground,
        shape: Int = currentShape,
        avatarSize: Int = currentSize,
    ) {
        requireIcon(iconRes)
        requireBackground(background)
        requireShape(shape)
        requireSize(avatarSize)

        letters.isVisible = false
        foreground.isVisible = true
        @ColorInt
        val newTint = when (background) {
            BACKGROUND_PRIMARY -> getColorPrimary()
            BACKGROUND_GREY -> getColorTextSecondary()
            else -> Color.WHITE
        }
        if (currentForegroundTint != newTint) {
            foreground.imageTintList = ColorStateList.valueOf(newTint)
            currentForegroundTint = newTint
        }
        if (avatarSize != currentSize) updateAvatarSize(avatarSize)
        if (currentIconRes != iconRes) {
            foreground.setImageResource(iconRes)
            currentIconRes = iconRes
        }
        if (currentBackground != background) updateBackground(background)
        if (shape != currentShape || avatarSize != currentSize) updateShape(shape, avatarSize)
        currentSize = avatarSize
    }

    internal fun setDrawable(
        drawable: Drawable = requireNotNull(currentBackgroundDrawable) {
            "It was a first call to the Avatar, you should have provided a drawable"
        },
        shape: Int = currentShape,
        avatarSize: Int = currentSize,
    ) {
        requireShape(shape)
        requireSize(avatarSize)

        letters.isVisible = false
        foreground.isVisible = false
        if (avatarSize != currentSize) updateAvatarSize(avatarSize)
        if (currentBackgroundDrawable?.constantState != drawable.constantState) {
            updateBackground(drawable)
        }
        if (shape != currentShape || avatarSize != currentSize) updateShape(shape, avatarSize)
        currentSize = avatarSize
    }

    private fun updateBackground(background: Int) {
        val drawable = when (background) {
            BACKGROUND_PRIMARY ->
                ColorDrawable(ColorUtils.setAlphaComponent(getColorPrimary(), 12.hexp))
            BACKGROUND_GREY -> ColorDrawable(getBackgroundTertiaryOpacity())
            else -> AppCompatResources.getDrawable(context, background)
        }
        this.background.setImageDrawable(drawable)
        currentBackground = background
    }

    private fun updateBackground(drawable: Drawable) {
        this.background.setImageDrawable(drawable)
        currentBackground = _BACKGROUND_SOME_DRAWABLE
        currentBackgroundDrawable = drawable
    }

    private fun updateShape(shape: Int, size: Int) {
        val maxRadius = containerSizes[size].dp.toFloat() / 2 // circle
        card.radius = when (shape) {
            SHAPE_CIRCLE -> maxRadius
            SHAPE_ROUNDED_CORNERS_BIG -> minOf(20f.dp, maxRadius)
            SHAPE_ROUNDED_CORNERS_MEDIUM -> minOf(12f.dp, maxRadius)
            SHAPE_ROUNDED_CORNERS_SMALL -> minOf(8f.dp, maxRadius)
            else -> error("Unknown shape option")
        }
        currentShape = shape
        updateIndicator()
    }

    private fun updateAvatarSize(avatarSize: Int) {
        @Px val containerSize = containerSizes[avatarSize].dp
        setSize(containerSize)
        background.setSize(containerSize)
        foreground.setSize(iconSizes[avatarSize].dp)
        setLettersSize(avatarSize)
        doOnNextLayout { updateIndicator() }
    }

    private fun updateIndicator() {
        if (!indicator.isVisible) return
        check(currentShape == SHAPE_CIRCLE) {
            "For now, Indicator is only supported with circle shape \uD83D\uDE1E"
        }
        indicator.ds.size = badgeSizes[currentSize]
        /** Avatar is a square, thus only one of ([View.getWidth], [View.getHeight]) matters */
        val avatarWidth = width
        val cardRadius = card.radius
        val halfAvatarWidth = avatarWidth.toFloat() / 2

        /** radius of an inscribed circle can't be larger than the half of the avatar width */
        val r = if (cardRadius > halfAvatarWidth) halfAvatarWidth else cardRadius
        val indicatorRadius = indicator.size.dp / 2

        // 🤓 https://youtu.be/SCmBIkDRTh4
        val indicatorTranslation =
            avatarWidth - (r * (1 - (SQRT_OF_TWO / 2))) - indicatorRadius

        indicator.translationX = indicatorTranslation
        indicator.translationY = indicatorTranslation
    }

    private fun setLettersSize(avatarSize: Int) {
        @Dimension(unit = SP) val textSize = textSizes[avatarSize]
        @Dimension(unit = SP) val textLineHeight = textLineHeights[avatarSize]
        this.letters.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        this.letters.setLineSpacing((textLineHeight - textSize).sp, 1f)
    }

    private fun View.setSize(@Px size: Int): Unit = doOnAttach {
        it.updateLayoutParams { width = size; height = size }
    }

    @ColorInt
    private var colorTextSecondary: Int? = null

    private fun getColorTextSecondary(): Int {
        if (colorTextSecondary != null) return colorTextSecondary!!
        colorTextSecondary = resolveColorAttr(android.R.attr.textColorSecondary)
        return colorTextSecondary!!
    }

    @ColorInt
    private var colorPrimary: Int? = null

    private fun getColorPrimary(): Int {
        if (colorPrimary != null) return colorPrimary!!
        colorPrimary = resolveColorAttr(AppCompatR.attr.colorPrimary)
        return colorPrimary!!
    }

    @ColorInt
    private var colorBackgroundTertiaryOpacity: Int? = null

    private fun getBackgroundTertiaryOpacity(): Int {
        if (colorBackgroundTertiaryOpacity != null) return colorBackgroundTertiaryOpacity!!
        colorBackgroundTertiaryOpacity = resolveColorAttr(R.attr.colorBackgroundTextField)
        return colorBackgroundTertiaryOpacity!!
    }

    private var selectableBackground: Drawable? = null

    private fun getSelectableBackground(): Drawable {
        if (selectableBackground != null) return selectableBackground!!
        val value = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, value, true)
        selectableBackground = ResourcesCompat.getDrawable(
            resources, value.resourceId, context.theme
        )
        return selectableBackground!!
    }

    private fun requireSize(size: Int) {
        require(size != NO_SIZE) {
            "It was a first call to the Avatar, you should have provided a size"
        }
        require(
            size == SIZE_24 ||
                    size == SIZE_32 ||
                    size == SIZE_40 ||
                    size == SIZE_56 ||
                    size == SIZE_72 ||
                    size == SIZE_88 ||
                    size == SIZE_112
        ) { "Unknown size option" }
    }

    private fun requireLetters(letters: String) {
        require(letters != NO_LETTERS) {
            "It was a first call to the Avatar, you should have provided letters"
        }
        require(letters.length == 2 || letters.length == 1) {
            "Avatar supports displaying only and exactly 2 letters"
        }
    }

    private fun requireIcon(@DsIconSet iconRes: Int) {
        require(iconRes != NO_ICON) {
            "It was a first call to the Avatar, you should have provided an icon"
        }
        iconRes.dsIcon
    }

    private fun requireBackground(background: Int) {
        require(background != NO_BACKGROUND) {
            "It was a first call to the Avatar, you should have provided a background"
        }
        require(
            background == BACKGROUND_PRIMARY ||
                    background == BACKGROUND_GREY ||
                    background == BACKGROUND_BLUE ||
                    background == BACKGROUND_GREEN ||
                    background == BACKGROUND_ORANGE ||
                    background == BACKGROUND_PURPLE ||
                    background == BACKGROUND_PINK ||
                    background == BACKGROUND_YELLOW
        ) { "Unknown background option" }
    }

    private fun requireShape(shape: Int) {
        require(shape != NO_SHAPE) {
            "It was a first call to the Avatar, you should have provided a shape"
        }
        require(shape in SHAPE_CIRCLE..SHAPE_ROUNDED_CORNERS_BIG) { "Unknown shape option" }
    }

    public companion object {
        public fun interface FontSetter {
            public fun setFont(textView: TextView)
        }

        /**
         * Must be set before using the component. Example:
         * ```
         * Avatar.fontSetter = FontSetter { textView ->
         *     textView.dsTextStyle(R.style.TextStyle_Flamingo_Body1)
         * }
         * ```
         */
        public var fontSetter: FontSetter? = null
        private const val NO_FONT_SETTER_ERROR =
            "Button.Companion.fontSetter must be set before using the component. To set it, " +
                    "initRobotoTypography() or initSbsansTypography() must be called"

        public const val SIZE_24: Int = 1
        public const val SIZE_32: Int = 2
        public const val SIZE_40: Int = 3
        public const val SIZE_56: Int = 4
        public const val SIZE_72: Int = 5
        public const val SIZE_88: Int = 6
        public const val SIZE_112: Int = 7

        public const val SHAPE_CIRCLE: Int = 1
        public const val SHAPE_ROUNDED_CORNERS_SMALL: Int = 2
        public const val SHAPE_ROUNDED_CORNERS_MEDIUM: Int = 3
        public const val SHAPE_ROUNDED_CORNERS_BIG: Int = 4

        public const val BACKGROUND_PRIMARY: Int = 1
        public const val BACKGROUND_GREY: Int = 2

        @DrawableRes
        public val BACKGROUND_BLUE: Int = R.drawable.ds_gradient_blue

        @DrawableRes
        public val BACKGROUND_GREEN: Int = R.drawable.ds_gradient_green

        @DrawableRes
        public val BACKGROUND_ORANGE: Int = R.drawable.ds_gradient_orange

        @DrawableRes
        public val BACKGROUND_PURPLE: Int = R.drawable.ds_gradient_purple

        @DrawableRes
        public val BACKGROUND_PINK: Int = R.drawable.ds_gradient_pink

        @DrawableRes
        public val BACKGROUND_YELLOW: Int = R.drawable.ds_gradient_yellow

        @Suppress("ObjectPropertyNaming")
        private const val _BACKGROUND_SOME_DRAWABLE: Int = Int.MAX_VALUE
        private const val NO_SHAPE: Int = 0
        private const val NO_BACKGROUND: Int = 0
        private const val NO_SIZE: Int = 0
        private const val NO_ICON: Int = 0
        private const val NO_LETTERS: String = "NO_LETTERS_MANGLED_rhn3oafhu30sxk#@*,±±§"

        private val SQRT_OF_TWO = sqrt(2f)

        // -1 are at zero places because 0's are NO_SOMETHING values

        @Dimension(unit = DP)
        private val containerSizes = intArrayOf(-1, 24, 32, 40, 56, 72, 88, 112)

        @Dimension(unit = SP)
        private val textSizes = floatArrayOf(-1f, 9f, 12f, 16f, 20f, 24f, 28f, 40f)

        @Dimension(unit = SP)
        private val textLineHeights = intArrayOf(-1, 20, 20, 20, 24, 28, 32, 48)

        @Dimension(unit = DP)
        private val iconSizes = intArrayOf(-1, 16, 16, 16, 24, 24, 40, 40)

        @Dimension(unit = DP)
        private val badgeSizes = intArrayOf(
            -1,
            Indicator.SIZE_SMALL,
            Indicator.SIZE_SMALL,
            Indicator.SIZE_SMALL,
            Indicator.SIZE_SMALL,
            Indicator.SIZE_BIG,
            Indicator.SIZE_BIG,
            Indicator.SIZE_BIG
        )
    }
}
