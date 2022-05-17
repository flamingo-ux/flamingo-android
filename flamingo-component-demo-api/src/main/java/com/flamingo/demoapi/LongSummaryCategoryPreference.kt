package com.flamingo.demoapi

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceViewHolder
import com.flamingo.utils.UnitConversions.dp

import com.flamingo.utils.updateMargins

/**
 * Preference that allows to set a very long [Preference.setSummary].
 */
public class LongSummaryCategoryPreference(
    context: Context?,
    attrs: AttributeSet?,
) : PreferenceCategory(context, attrs) {

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        holder.itemView.findViewById<TextView>(android.R.id.summary).apply {
            updateMargins(right = LONG_CATEGORY_PREFERENCE_RIGHT_MARGIN.dp)
            isSingleLine = false
            maxLines = Int.MAX_VALUE
        }
    }
}

private const val LONG_CATEGORY_PREFERENCE_RIGHT_MARGIN = 16
