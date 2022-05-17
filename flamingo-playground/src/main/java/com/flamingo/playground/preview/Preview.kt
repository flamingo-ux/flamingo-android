@file:Suppress("SpacingAroundParens")

package com.flamingo.playground.preview

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.flamingo.playground.gallery.ViewComponentDetailsFragment
import com.flamingo.view.components.FlamingoComponent

/**
 * Used to display a preview of the component on the [ViewComponentDetailsFragment].
 */
abstract class Preview<T> : Fragment() where T : FlamingoComponent {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.updateLayoutParams<FrameLayout.LayoutParams> {
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }
    }
}

/**
 * Used to display a preview of the Android View component on the [ViewComponentDetailsFragment].
 */
abstract class ViewPreview<T> : Preview<T>() where T : FlamingoComponent, T : View {
    abstract override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): T
}
