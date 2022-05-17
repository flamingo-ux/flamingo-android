@file:Suppress("NOTHING_TO_INLINE")

package com.flamingo.utils

import android.view.View

/**
 * Used for more concise and less eye-straining calls to change visibility of views.
 * Found especially useful when used to change visibility of a bulk of views at once, e. g.:
 *
 * ```
 * gone(qwe, zxc, fgh)
 * visible(asd, rty)
 * ```
 *
 * VS
 *
 * ```
 * qwe.visibility = View.GONE
 * asd.visibility = View.VISIBLE
 * zxc.visibility = View.GONE
 * rty.visibility = View.VISIBLE
 * fgh.visibility = View.GONE
 * ```
 */
public inline fun gone(vararg views: View) {
    for (view in views) view.visibility = View.GONE
}

/**
 * @see [gone]
 */
public inline fun visible(vararg views: View) {
    for (view in views) view.visibility = View.VISIBLE
}

/**
 * @see [gone]
 */
public inline fun invisible(vararg views: View) {
    for (view in views) view.visibility = View.INVISIBLE
}
