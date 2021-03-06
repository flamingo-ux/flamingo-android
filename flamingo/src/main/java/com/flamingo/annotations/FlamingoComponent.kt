package com.flamingo.annotations

import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import com.flamingo.Flamingo
import com.flamingo.InternalComponents

/**
 * This annotation is used to mark all @[Composable] functions which are design system components.
 * KSP-based annotation processor called __crab__ collects all such annotation usages at the build
 * time and transform them into [FlamingoRegistry] class.
 *
 * If there are `@sample`s in KDocs of the annotated function, and [extractKDocs] is true, then
 * [RegistryGenerator] will resolve fully qualified names of specified sample and, if `no code` is
 * not specified, add source code of the file that contains a sample to the [FlamingoRegistry].
 * Also, if `no preview` is not specified, [RegistryGenerator] will add a composable lambda to
 * [FlamingoRegistry], which calls the sample function with no arguments.

 * If `no code no preview` is specified, sample will not be included in [FlamingoRegistry] at all.

 * __NOTE:__ All samples MUST BE @[Composable] functions. Else, they MUST specify
 * `no code no preview`
 *
 * If a function has [InternalComponents] receiver, annotation processor adds a corresponding
 * property to the FlamingoRegistry.
 *
 * For more info about annotation processor,
 * see [documentation](https://todo.ru/x/YA6oQwE).
 *
 * @param displayName it will be used on the component's details page. CAN be empty. If
 * [String.isBlank], name of the function will be used
 *
 * @param preview fully qualified name of the composable function, accessible from
 * `flamingo-playground` module. Function MUST have no arguments. CANNOT be empty
 *
 * @param figma url. MUST start with `https://www.todo.com/` or `https://todo.com/`. CANNOT be
 * empty (except when the component is internal (see [InternalComponents])). Note, that designs,
 * located at this url CAN BE not up-to-date with the implementation. Relevant info is located in
 * [specification] instead.
 *
 * @param specification url of the specification of this flamingo component. MUST start with
 * `https://`. CANNOT be empty (except when the component is internal (see [InternalComponents]))
 *
 * @param viewImplementation fully qualified name of the Android View-based impl. CAN be empty
 *
 * @param theaterPackage fully qualified name of [TheaterPackage]. CAN be empty. See
 * [Theater docs](todo) for more info.
 *
 * @param demo an array of fully qualified names of [Fragment]s that demonstrate usage of this
 * flamingo component. CAN be empty only for components that have [viewImplementation], because it
 * is needed to create new demos for those components, but time for this task is not present at the
 * moment
 *
 * @param supportsWhiteMode if true, component supports [Flamingo.LocalWhiteMode]
 *
 * @param extractKDocs if true, KDocs of this function will be included in [FlamingoRegistry]
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
public annotation class FlamingoComponent(
    public val displayName: String = "",
    public val preview: String,
    public val figma: String,
    public val specification: String,
    public val viewImplementation: String = "",
    public val theaterPackage: String = "",
    public val demo: Array<String>,
    public val supportsWhiteMode: Boolean,
    public val extractKDocs: Boolean = true,
)
