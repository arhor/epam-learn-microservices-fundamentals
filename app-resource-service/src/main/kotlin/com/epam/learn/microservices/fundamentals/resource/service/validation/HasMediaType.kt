package com.epam.learn.microservices.fundamentals.resource.service.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [/* resources/META-INF/service/javax.validation.ConstraintValidator */])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class HasMediaType(
    val value: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val message: String = "{com.epam.learn.microservices.fundamentals.resource.service.validation.HasMediaType.message}"
)
