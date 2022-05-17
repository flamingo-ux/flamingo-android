package com.flamingo.utils

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

/**
 * Retrieves the color value for the attribute. If the attribute references a color resource holding
 * a [ColorStateList], then the [ColorStateList.getDefaultColor] is returned.
 *
 * Example:
 * ```
 * @ColorInt val primaryColor = context.resolveColorAttr(R.attr.colorPrimary)
 * @ColorInt val textPrimaryColor = /* default ColorStateList color */
 *     context.resolveColorAttr(android.R.attr.textColorPrimary)
 * ```
 * @receiver [Context] with the correct theme, it matters!
 * @param attr The resource identifier of the desired theme attribute
 */
@ColorInt
public fun Context.resolveColorAttr(@AttrRes attr: Int): Int = theme.resolveColorAttr(attr)

/** @see resolveColorAttr */
@ColorInt
public fun RecyclerView.ViewHolder.resolveColorAttr(@AttrRes attr: Int): Int {
    return itemView.context.resolveColorAttr(attr)
}

/** @see resolveColorAttr */
@ColorInt
public fun View.resolveColorAttr(@AttrRes attr: Int): Int = context.resolveColorAttr(attr)

/**
 * Must only be called after [Fragment.onCreateView] (uses [Fragment.requireView])
 * @see resolveColorAttr
 */
@ColorInt
public fun Fragment.resolveColorAttr(@AttrRes attr: Int): Int = requireView().resolveColorAttr(attr)

/**
 * @param attr The resource identifier of the desired theme attribute
 * @see resolveColorAttr
 */
@ColorInt
public fun Resources.Theme.resolveColorAttr(@AttrRes attr: Int): Int {
    val typedArray = obtainStyledAttributes(intArrayOf(attr))
    @ColorInt val color = typedArray.getColor(0, 0)
    typedArray.recycle()
    return color
}
