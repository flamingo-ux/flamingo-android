package com.flamingo.demoapi

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder

/**
 * Preference that allows to set a very long [Preference.setSummary].
 */
public class LongSummaryPreference(
    context: Context?,
    attrs: AttributeSet?,
) : Preference(context, attrs) {

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        holder.itemView.findViewById<TextView>(android.R.id.summary).apply {
            isSingleLine = false
            maxLines = Int.MAX_VALUE
        }
    }
}
