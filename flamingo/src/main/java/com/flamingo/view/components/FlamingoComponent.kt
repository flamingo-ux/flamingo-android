package com.flamingo.view.components

import com.flamingo.view.VIEW_DEPRECATION_MSG

/** Every design system component should implement this. See
 * [com.flamingo.annotations.view.FlamingoComponent] for more */
@Deprecated(VIEW_DEPRECATION_MSG)
public sealed interface FlamingoComponent {

    /**
     * # Accessor pattern
     *
     * To interact with any design component from code, [ds] property MUST BE used. For example:
     * ```
     * override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
     *     b.textButton.ds.label = "" // ✅
     *     b.textButton.setText("") // ❌
     *     b.textButton.ds.setOnClickListener { } = "" // ✅
     *     b.textButton.setOnClickListener { } = "" // ❌
     * }
     * ```
     *
     * ## Why
     *
     * To limit possible functionality of the android view. Using this pattern, design component
     * creator can declare an [Accessor] that states, how a developer can interact with the
     * component. [Accessor] limits possible view states and allows to reliably test all possible
     * view states and be sure, that a particular component behaves as expected. It also ensures
     * **visual** design consistency across the app.
     *
     * **IMPORTANT:** usage of any other function/property from an android view system (without an
     * accessor) can lead to an **undefined behaviour** of the particular design component.
     *
     * ## How
     *
     * [Accessor] should be an `inner class` (to be able to override it and to capture `this`) with
     * `internal constructor` (to prevent the constructor from appearing in autocomplete).
     *
     * @see com.flamingo.view.components.AlertMessage.Accessor
     */
    public val ds: Accessor

    /** @see ds */
    public interface Accessor

    /**
     * Used mainly in combination with inline functions. Unfortunately, reference to the outer class
     * of the inner class is accessible only inside an inner class body using `this@OuterClass`, and
     * is **not** inlined. Thus, to be able to use inline functions inside an accessor, user MUST:
     * 1. use [AccessorWithComponentReference]
     * 2. reference outer class inside an inline function (including default params of an inline
     * function) **only** by [componentRef].
     *
     * [https://stackoverflow.com/a/52089591]
     */
    public abstract class AccessorWithComponentReference<out T : FlamingoComponent>(
        @PublishedApi
        internal val componentRef: T,
    ) : Accessor
}
