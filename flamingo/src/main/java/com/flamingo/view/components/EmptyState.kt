package com.flamingo.view.components

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.withStyledAttributes
import androidx.core.view.doOnAttach
import androidx.core.view.updateLayoutParams
import com.flamingo.Flamingo
import com.flamingo.R
import com.flamingo.annotations.view.DsIconSet
import com.flamingo.annotations.view.FlamingoComponent
import com.flamingo.components.Action
import com.flamingo.components.ActionGroup
import com.flamingo.components.EmptyState
import com.flamingo.components.EmptyStateImage
import com.flamingo.initUnitConversionsInCustomView
import com.flamingo.theme.FlamingoTheme
import com.flamingo.view.VIEW_DEPRECATION_MSG
import com.flamingo.view.dsIcon

@FlamingoComponent(
    displayName = "Empty State",
    docs = """<h5></h5>
        <p>Компонент Empty State содержит: иллюстрацию или иконку в аватаре, заголовок, описание, и
        кнопку. Каждый из этих элементов опционален (можно показать и скрыть). 
        <code>layout_width</code>: только <code>match_parent</code>; 
        <code>layout_height</code>: только <code>wrap_content</code>.</p>
        <br>
    """,
    preview = "com.flamingo.playground.preview.EmptyStatePreview",
    figmaUrl = "https://www.todo.com/file/sPbkUbBGkp5Mstc0IQYubk/4.1.-UI-Android-kit?node-id=830" +
            "%3A621",
    permittedXmlAttributes = [
        "app:ds_es_imageType",
        "app:ds_es_title",
        "app:ds_es_description",
        "app:ds_es_buttonText",
        "app:ds_es_icon",
    ],
    theDemos = [
        "com.flamingo.playground.components.EmptyStateTypicalUsage",
        "com.flamingo.playground.components.EmptyStateStatesPlayroom",
    ]
)
@Deprecated(VIEW_DEPRECATION_MSG)
public class EmptyState @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(ContextThemeWrapper(context, R.style.Theme_Flamingo), attrs, defStyleAttr),
    com.flamingo.view.components.FlamingoComponent {

    public inner class Accessor internal constructor() :
        com.flamingo.view.components.FlamingoComponent.Accessor {

        /**
         * One of [EmptyState.IMAGE_*]. Shows only when [iconRes] is null. Image constant names
         * correlate to the `ds_image_*****` drawables.
         */
        public var imageType: Int
            get() = this@EmptyState.imageType
            set(value) {
                this@EmptyState.imageType = value
            }

        /**
         * If not null, icon in an [Avatar] will be shown, and any [imageType] image will be hidden
         */
        @DsIconSet
        public var iconRes: Int?
            get() = this@EmptyState.iconRes
            set(value) {
                this@EmptyState.iconRes = value
            }

        /** If [String.isEmpty], text block will hide */
        public var title: String
            get() = this@EmptyState.title
            set(value) {
                this@EmptyState.title = value
            }

        /** If [String.isEmpty], text block will hide */
        public var description: String
            get() = this@EmptyState.description
            set(value) {
                this@EmptyState.description = value
            }

        public var actions: ActionGroup?
            get() = this@EmptyState.actions
            set(value) {
                this@EmptyState.actions = value
            }
    }

    override val ds: Accessor = Accessor()

    internal var imageType: Int = IMAGE_NO_IMAGE
        set(value) {
            requireImageType(imageType)
            field = value
            render()
        }

    @DsIconSet
    internal var iconRes: Int? = null
        set(value) {
            field = value?.dsIcon
            render()
        }

    internal var title: String = ""
        set(value) {
            field = value
            render()
        }

    internal var description: String = ""
        set(value) {
            field = value
            render()
        }

    internal var actions: ActionGroup? = null
        set(value) {
            field = value
            render()
        }

    private val composeView = ComposeView(context)

    init {
        initUnitConversionsInCustomView()
        addView(composeView)
        context.withStyledAttributes(attrs, R.styleable.EmptyState) {
            imageType = xmlAttrToImageType(getInt(R.styleable.EmptyState_ds_es_imageType, 0))
            iconRes = getResourceId(R.styleable.EmptyState_ds_es_icon, 0)
                .let { if (it == 0) null else it }
            title = getString(R.styleable.EmptyState_ds_es_title) ?: ""
            description = getString(R.styleable.EmptyState_ds_es_description) ?: ""
            val buttonText: String? = getString(R.styleable.EmptyState_ds_es_buttonText)
            if (!buttonText.isNullOrEmpty()) {
                actions = ActionGroup(Action(label = buttonText, onClick = {}))
            }
        }
        composeView.doOnAttach {
            it.updateLayoutParams {
                gravity = Gravity.CENTER
            }
        }
        doOnAttach {
            it.updateLayoutParams<ViewGroup.LayoutParams> {
                width = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
    }

    /** Transforms xml's enum index (from 0 to n) to [DrawableRes] from [imageTypes] */
    @Suppress("MagicNumber")
    private fun xmlAttrToImageType(xmlImageType: Int): Int {
        return imageTypes.getOrElse(xmlImageType) { error("Invalid image type: $xmlImageType") }
    }

    private fun render() {
        composeView.setContent { EmptyState() }
    }

    @Suppress("ComplexMethod", "MemberNameEqualsClassName", "FunctionNaming")
    @Composable
    private fun EmptyState() {
        FlamingoTheme {
            EmptyState(
                icon = iconRes?.let { Flamingo.icons.fromId(iconRes!!) },
                image = when (imageType) {
                    IMAGE_NOTIFICATION -> EmptyStateImage.NOTIFICATION
                    IMAGE_FEED -> EmptyStateImage.FEED
                    IMAGE_EMPTY -> EmptyStateImage.EMPTY
                    IMAGE_CALENDAR -> EmptyStateImage.CALENDAR
                    IMAGE_COMMUNITY -> EmptyStateImage.COMMUNITY
                    IMAGE_WEB -> EmptyStateImage.WEB
                    IMAGE_CRASH -> EmptyStateImage.CRASH
                    IMAGE_PEOPLE -> EmptyStateImage.PEOPLE
                    IMAGE_SHIELD -> EmptyStateImage.SHIELD
                    IMAGE_LOCK -> EmptyStateImage.LOCK
                    IMAGE_SEARCH -> EmptyStateImage.SEARCH
                    IMAGE_MAINTENANCE -> EmptyStateImage.IMAGE_MAINTENANCE
                    IMAGE_NO_IMAGE -> null
                    else -> error("Unknown imageType: $imageType")
                },
                title = title.takeIf { it.isNotEmpty() },
                description = description.takeIf { it.isNotEmpty() },
                actions = actions,
                // because developers already used EmptyState and were unaware of this baked in
                // scrolling behaviour
                verticalScroll = false,
                contentDescription = title.takeIf { it.isNotEmpty() }
            )
        }
    }

    private fun requireImageType(imageType: Int) {
        require(imageTypes.contains(imageType)) { "Invalid image type: $imageType" }
    }

    public companion object {
        public const val IMAGE_NO_IMAGE: Int = 0

        @DrawableRes
        public val IMAGE_NOTIFICATION: Int = R.drawable.ds_image_notification

        @DrawableRes
        public val IMAGE_FEED: Int = R.drawable.ds_image_feed

        @DrawableRes
        public val IMAGE_EMPTY: Int = R.drawable.ds_image_empty

        @DrawableRes
        public val IMAGE_CALENDAR: Int = R.drawable.ds_image_calendar

        @DrawableRes
        public val IMAGE_COMMUNITY: Int = R.drawable.ds_image_community

        @DrawableRes
        public val IMAGE_WEB: Int = R.drawable.ds_image_web

        @DrawableRes
        public val IMAGE_CRASH: Int = R.drawable.ds_image_crash

        @DrawableRes
        public val IMAGE_PEOPLE: Int = R.drawable.ds_image_people

        @DrawableRes
        public val IMAGE_SHIELD: Int = R.drawable.ds_image_shield

        @DrawableRes
        public val IMAGE_LOCK: Int = R.drawable.ds_image_lock

        @DrawableRes
        public val IMAGE_SEARCH: Int = R.drawable.ds_image_search

        @DrawableRes
        public val IMAGE_MAINTENANCE: Int = R.drawable.ds_image_maintenance

        private val imageTypes = intArrayOf(
            IMAGE_NO_IMAGE, IMAGE_NOTIFICATION, IMAGE_FEED, IMAGE_EMPTY, IMAGE_CALENDAR,
            IMAGE_COMMUNITY, IMAGE_WEB, IMAGE_CRASH, IMAGE_PEOPLE, IMAGE_SHIELD, IMAGE_LOCK,
            IMAGE_SEARCH, IMAGE_MAINTENANCE,
        )
    }
}
