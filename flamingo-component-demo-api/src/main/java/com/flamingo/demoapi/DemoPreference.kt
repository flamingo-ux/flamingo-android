@file:Suppress("SpacingAroundParens")

package com.flamingo.demoapi

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.view.doOnAttach
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.flamingo.theme.FlamingoTheme
import com.flamingo.utils.UnitConversions.dp as viewDp

/**
 * Preference that allows to demonstrate a [View] in a fancy🌟 frame.
 */
public class DemoPreference(
    context: Context?,
    attrs: AttributeSet?,
) : Preference(context, attrs) {

    init {
        layoutResource = R.layout.demo_preference
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        holder.itemView.isClickable = false // disable parent click
        val frameLayout = holder.findViewById(R.id.design_component_frame) as FrameLayout
        frameLayout.setPadding(if (isCompose) 0 else PADDING.viewDp)
        if (component != null && component!!.parent == null) frameLayout.addView(component)
    }

    private var component: View? = null
    private var isCompose: Boolean = false

    public fun setDesignComponent(view: View) {
        isCompose = false
        component = view
        view.doOnAttach {
            it.updateLayoutParams<FrameLayout.LayoutParams> { gravity = Gravity.CENTER }
        }
    }

    public fun setComposeDesignComponent(content: @Composable () -> Unit) {
        val composeView = ComposeView(context).apply {
            setContent {
                FlamingoTheme {
                    Box(Modifier.padding(PADDING.dp), contentAlignment = Alignment.Center) {
                        content()
                    }
                }
            }
        }
        setDesignComponent(composeView)
        isCompose = true
    }
}

private const val PADDING = 24
