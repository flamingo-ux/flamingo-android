package com.flamingo.annotations.view

import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import org.intellij.lang.annotations.Language
import com.flamingo.initUnitConversionsInCustomView
import com.flamingo.utils.UnitConversions
import com.flamingo.view.components.EmptyState
import com.flamingo.view.components.FlamingoComponent

/**
 * Every design system component, that has Android View implementation, MUST BE annotated with
 * [FlamingoComponent].
 *
 * __WARNING:__ design system components with Android View implementation are __DEPRECATED__. It is
 * forbidden to create new ones. Instead, use compose implementation.
 * See [com.flamingo.annotations.FlamingoComponent].
 *
 * ## [Video introduction](https://video.todo.ru/watch/6083)
 * Probably outdated and partially inaccurate.
 *
 * # What is a design system component
 *
 * It is a custom android view with special properties.
 *
 * # How to create a design system component
 *
 * There is a template for fast creation of design system component class.
 * [How to use it](https://video.todo.ru/watch/6223).
 *
 * - Subclass any android view and put it in [com.flamingo.view.components]
 * package in the `flamingo` gradle module
 * - Add [ContextThemeWrapper] as a value to the `context` parameter of the [View] constructor with
 * [R.style.Theme_Flamingo] as a [ContextThemeWrapper] theme (as shown in [EmptyState]). This is
 * needed to forcefully apply [R.style.Theme_Flamingo] to the component even if user wouldn't
 * apply it.
 * - Annotate your class with @[FlamingoComponent]
 * - Implement [FlamingoComponent] (even if superclass already implements it)
 * - Implement [FlamingoComponent.Accessor] and add docs to its properties/functions
 * - Call [initUnitConversionsInCustomView] before a first call to the
 * [UnitConversions] extension properties. See [initUnitConversionsInCustomView] for more info.
 * - Create [com.flamingo.playground.preview.Preview]
 * - Create necessary (in your opinion) demos (at least 1 is required). StatesPlayroom MUST BE the
 * priority. See [com.flamingo.playground.FlamingoComponentDemoType] for more info on
 * that
 * - List custom view attributes in the [permittedXmlAttributes] with the 'app:' namespace, if your
 * view has any
 * - Test that your component behaves as expected in light AND night modes using demos. To be able
 * to enter the night mode, turn on [CommonFeature.NIGHT_MODE] in the staging settings.
 *
 * ## Modification of @[FlamingoComponent]
 *
 * There is a lot of code (lint checks and unit tests) that is dependent on the structure described
 * above and, in particular, structure of [FlamingoComponent] class declaration. Before editing
 * the declaration, be sure not to break this code. Look into it!
 *
 * @param displayName MUST NOT BE blank. It will be used on the component's details page.
 * @param docs is an html string that describes details of the component usage to the programmers.
 * All docs MUST BE there, usage of KDoc comments is prohibited.
 * @param figmaUrl points to the figma design node. Must start with https://www.todo.com
 * @param preview see [com.flamingo.playground.preview.Preview]
 * @param permittedXmlAttributes specifies names of the custom xml attributes of this component.
 * All other attributes are prohibited.
 * This is done to limit possible view states. See [FlamingoComponent.ds] for more on that.
 * @param theDemos fully qualified class names of fragments annotated with annotations that are
 * annotated with [com.flamingo.playground.FlamingoComponentDemoType]. See all of them in
 * the [DemoTypes.kt]. Can't be empty.
 * Order matters, it defines order of the demos in the ui.
 * By the way, 'the' is in the name because Android Studio's "Quick Documentation" popup
 * alphabetically orders params, and [docs] is getting stretched by a lot.
 *
 * @see com.flamingo.playground.FlamingoComponentTest
 */
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
public annotation class FlamingoComponent(
    val displayName: String,
    @Language("HTML")
    val docs: String,
    val figmaUrl: String,
    val preview: String,
    val permittedXmlAttributes: Array<String> = [],
    val theDemos: Array<String>,
)
