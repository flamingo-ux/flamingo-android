@file:Suppress("TooManyFunctions")

package com.flamingo.demoapi

import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup
import androidx.preference.SeekBarPreference
import androidx.preference.TwoStatePreference
import com.flamingo.utils.resolveColorAttr
import androidx.appcompat.R as AppCompatR

public fun EditTextPreference.initPref(savedInstanceState: Bundle?, defVal: String) {
    if (savedInstanceState == null) text = defVal
    callChangeListener(text)
}

public fun ListPreference.initPref(savedInstanceState: Bundle?, defVal: Any) {
    if (savedInstanceState == null) value = defVal.toString()
    callChangeListener(value)
}

public fun SeekBarPreference.initPref(savedInstanceState: Bundle?, defVal: Int) {
    if (savedInstanceState == null) value = defVal
    callChangeListener(value)
}

public fun TwoStatePreference.initPref(savedInstanceState: Bundle?, defVal: Boolean) {
    if (savedInstanceState == null) isChecked = defVal
    callChangeListener(isChecked)
}

public inline fun DropDownPreference.onChange(crossinline block: (String) -> Boolean): Unit =
    onChange<DropDownPreference, String>(block)

public inline fun TwoStatePreference.onChange(crossinline block: (Boolean) -> Boolean): Unit =
    onChange<TwoStatePreference, Boolean>(block)

public inline fun EditTextPreference.onChange(crossinline block: (String) -> Boolean): Unit =
    onChange<EditTextPreference, String>(block)

public inline fun SeekBarPreference.onChange(crossinline block: (Int) -> Boolean): Unit =
    onChange<SeekBarPreference, Int>(block)

public inline fun ListPreference.onChange(crossinline block: (String) -> Boolean): Unit =
    onChange<ListPreference, String>(block)

public inline fun MultiSelectListPreference.onChange(crossinline block: (String) -> Boolean): Unit =
    onChange<MultiSelectListPreference, String>(block)

@PublishedApi
@Suppress("UNCHECKED_CAST")
internal inline fun <PREF : Preference, ATTR : Any> PREF.onChange(
    crossinline block: (ATTR) -> Boolean
): Unit = setOnPreferenceChangeListener { _, newValue -> block(newValue as ATTR) }

public inline fun <T : Preference> PreferenceFragmentCompat.configurePreference(
    key: CharSequence,
    block: T.() -> Unit,
): T? {
    val pref = findPreference(key) as? T
    pref?.block()
    return pref
}

public fun <T : Preference> PreferenceFragmentCompat.findPreference(key: CharSequence): T? {
    return findPreference(key) as? T
}

/**
 * Sets tint for all preference's icons recursively.
 * @param colorAttr used to retrieve themed color to set the tint
 * @param iconTintColor if != -1, it used to set the tint, ignoring [colorAttr]
 * @param filter if lambda returns `true`, tint is applied to the [Preference],
 * `false` - not applied.
 */
public fun Preference.tintIcons(
    colorAttr: Int = AppCompatR.attr.colorControlNormal,
    @ColorInt iconTintColor: Int = -1,
    filter: Preference.() -> Boolean = { true },
) {
    val resolvedColor =
        if (iconTintColor == -1) context.resolveColorAttr(colorAttr) else iconTintColor
    if (this is PreferenceGroup) {
        for (i in 0 until preferenceCount) {
            getPreference(i).tintIcons(iconTintColor = resolvedColor, filter = filter)
        }
    } else {
        if (icon != null && filter()) DrawableCompat.setTint(icon, resolvedColor)
    }
}
