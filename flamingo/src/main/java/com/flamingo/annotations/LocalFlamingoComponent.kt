package com.flamingo.annotations

import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import com.flamingo.Flamingo
import com.flamingo.Team
import kotlin.reflect.KClass

/**
 * This annotation is used to mark all @[Composable] functions which are
 * [local flamingo components](https://todo.ru/x/M5moXQE).
 *
 * Local flamingo components are meant to be created and maintained by feature-developers in their
 * respective feature-modules. Some, however, can be extracted into common gradle modules an can be
 * used across the project.
 *
 * In the future, local flamingo components will appear in the _components gallery_ of the project.
 *
 * @param team a class that is annotated with [TeamMarker] and implements [Team]. Represents, which
 * team is responsible for this component.
 * @param displayName it will be used on the component's details page. CAN be empty. If
 * [String.isBlank], name of the function will be used.
 * @param preview fully qualified name of the composable function, accessible from
 * other modules. Function MUST have no arguments. CANNOT be empty.
 * @param figma url. MUST start with `https://www.todo.com/` or `https://todo.com/`. CAN be empty.
 * @param specification url of the specification of this component. MUST start with
 * `https://`. CAN be empty.
 * @param demo an array of fully qualified names of [Fragment]s that demonstrate usage of this
 * flamingo component
 * @param supportsWhiteMode if true, component supports [Flamingo.LocalWhiteMode]
 */
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
@Suppress("LongParameterList")
@Target(AnnotationTarget.FUNCTION)
public annotation class LocalFlamingoComponent(
    public val preview: String,
    public val team: KClass<out Team>,
    public val displayName: String = "",
    public val figma: String = "",
    public val specification: String = "",
    public val demo: Array<String> = [],
    public val supportsWhiteMode: Boolean = false,
)
