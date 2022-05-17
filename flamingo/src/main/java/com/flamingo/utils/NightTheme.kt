package com.flamingo.utils

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.view.View

/** @return true, if night mode is currently enabled, false otherwise */
public fun View.isNightMode(): Boolean = context.isNightMode()

/** @return true, if night mode is currently enabled, false otherwise */
public fun Context.isNightMode(): Boolean {
    return resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}
