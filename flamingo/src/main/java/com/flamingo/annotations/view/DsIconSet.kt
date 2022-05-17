package com.flamingo.annotations.view

import androidx.annotation.DrawableRes

/**
 * Denotes that an [Int] is a [DrawableRes] from the design system icon set.
 *
 * @param otherIconsAllowed if true, setting [DrawableRes] not from a design system icon set will
 * not result in a crash
 */
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
public annotation class DsIconSet(val otherIconsAllowed: Boolean = false)
