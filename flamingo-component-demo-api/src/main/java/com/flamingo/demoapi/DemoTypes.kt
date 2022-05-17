package com.flamingo.demoapi

import androidx.fragment.app.Fragment
import com.flamingo.annotations.view.FlamingoComponent

/**
 * A type of design component demo.
 *
 * Annotations that are annotated with [FlamingoComponentDemoType] can be applied to [Fragment]s
 * that are listed in [FlamingoComponent.theDemos]. Then they will be automagically✨ listed as
 * demos in the [com.flamingo.playground.gallery.ViewComponentDetailsFragment] and
 * [com.flamingo.playground.gallery.ComponentDetailsFragment].
 *
 * Multiple annotations can be applied on the same demo [Fragment].
 *
 * Use [FlamingoComponentDemoName] to specify a custom name, as opposed to the default one
 * ("Demo 1", "Demo 2", "Demo 3", etc)
 *
 * @sample StatesPlayroomDemo
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
public annotation class FlamingoComponentDemoType(val typeName: String)

/**
 * Replaces default demo name ("Demo 1", "Demo 2", "Demo 3", etc.) in the demos list with [name].
 */
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
public annotation class FlamingoComponentDemoName(val name: String)

/**
 * A fragment that demonstrates one or many design components.
 */
@Target(AnnotationTarget.CLASS)
@FlamingoComponentDemoType("")
@MustBeDocumented
public annotation class FlamingoComponentDemo

/**
 * A fragment that allows to set all parameters and call all methods of the component.
 * @sample com.flamingo.playground.components.AlertMessageStatesPlayroom
 */
@Target(AnnotationTarget.CLASS)
@FlamingoComponentDemoType("States Playroom")
@MustBeDocumented
public annotation class StatesPlayroomDemo

/**
 * A fragment that displays list of one component in all possible states.
 */
@Target(AnnotationTarget.CLASS)
@FlamingoComponentDemoType("All Possible States")
@MustBeDocumented
public annotation class AllPossibleStatesDemo

/**
 * A fragment that demonstrates a typical usage of the component, from kotlin code and xml.
 * @sample com.flamingo.playground.components.AlertMessageStatesPlayroom
 */
@Target(AnnotationTarget.CLASS)
@FlamingoComponentDemoType("Typical Usage")
@MustBeDocumented
public annotation class TypicalUsageDemo
