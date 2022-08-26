package com.flamingo.annotations

/**
 * Flamingo components can be marked with [UsedInsteadOf] to indicate that [wrongComponentName]
 * components should not be used in the app. Instead, this flamingo component should be used.
 *
 * Also, there is a lint check [com.flamingo.lint.WrongComponentAlternativeDetector] that
 * forbids usage of [wrongComponentName] components. Details on how it works can be found in KDocs
 * of [com.flamingo.lint.WrongComponentAlternativeDetector]
 *
 * [More info](https://confluence.companyname.ru/x/UwcyeQE)
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
internal annotation class UsedInsteadOf(vararg val wrongComponentName: String)
