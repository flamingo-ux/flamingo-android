package com.flamingo.view

import android.graphics.drawable.Drawable
import coil.target.ViewTarget
import com.flamingo.view.components.Avatar

/**
 * A [Target] to load an image into an [Avatar] using [coil].
 * @param initialShape is used as an argument to the [Avatar.Accessor.setDrawable]
 * @param initialSize is used as an argument to the [Avatar.Accessor.setDrawable]
 */
public class AvatarTarget(
    override val view: Avatar,
    private val initialShape: Int = view.currentShape,
    private val initialSize: Int = view.currentSize,
) : ViewTarget<Avatar> {
    override fun onError(error: Drawable?) {
        if (error == null) return
        view.ds.setDrawable(error, initialShape, initialSize)
    }

    override fun onStart(placeholder: Drawable?) {
        if (placeholder == null) return
        view.ds.setDrawable(placeholder, initialShape, initialSize)
    }

    override fun onSuccess(result: Drawable) {
        view.ds.setDrawable(result, initialShape, initialSize)
    }
}
