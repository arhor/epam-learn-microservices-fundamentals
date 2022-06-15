package com.epam.learn.microservices.fundamentals.resource.service.validation

import org.springframework.web.multipart.MultipartFile
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

@MustBeDocumented
@Constraint(validatedBy = [HasMediaType.Validator::class])
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class HasMediaType(
    /**
     * Required MediaType for the MultipartFile instance under validation.
     */
    val value: String,

    /**
     * The error message displayed in case of validation failure.
     */
    val message: String = "{error.validation.wrong.media-type}"
) {

    class Validator : ConstraintValidator<HasMediaType, MultipartFile> {

        private lateinit var requiredMediaType: String

        override fun initialize(constraintAnnotation: HasMediaType) {
            requiredMediaType = constraintAnnotation.value
        }

        override fun isValid(value: MultipartFile?, context: ConstraintValidatorContext): Boolean {
            return value == null
                || value.contentType == requiredMediaType
        }
    }
}
