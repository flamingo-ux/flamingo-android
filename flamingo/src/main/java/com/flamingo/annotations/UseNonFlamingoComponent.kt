package com.flamingo.annotations

import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR
import kotlin.annotation.AnnotationTarget.EXPRESSION
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.FILE
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.LOCAL_VARIABLE
import kotlin.annotation.AnnotationTarget.PROPERTY
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.annotation.AnnotationTarget.TYPEALIAS
import kotlin.annotation.AnnotationTarget.TYPE_PARAMETER
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

/**
 * [com.flamingo.lint.WrongComponentAlternativeDetector.ISSUE] can be suppressed __only__
 * with this annotation, because it is important to provide an explicit [explanation] for each
 * violation of this lint check.
 *
 * [More info](https://confluence.companyname.ru/x/UwcyeQE)
 * @param explanation MUST NOT be blank. Detailed reason for a violation of the lint check.
 * @see com.flamingo.annotations.UsedInsteadOf
 */
@MustBeDocumented
@Target(CLASS, ANNOTATION_CLASS, TYPE_PARAMETER, PROPERTY, FIELD, LOCAL_VARIABLE, VALUE_PARAMETER,
    CONSTRUCTOR, FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER, TYPE, EXPRESSION, FILE, TYPEALIAS)
@Retention(AnnotationRetention.SOURCE)
public annotation class UseNonFlamingoComponent(val explanation: String)
