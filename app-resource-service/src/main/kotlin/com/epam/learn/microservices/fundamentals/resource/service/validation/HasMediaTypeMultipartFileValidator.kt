package com.epam.learn.microservices.fundamentals.resource.service.validation

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import org.springframework.web.multipart.MultipartFile
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class HasMediaTypeMultipartFileValidator : ConstraintValidator<HasMediaType, MultipartFile> {

    private lateinit var requiredMediaType: String

    override fun initialize(constraintAnnotation: HasMediaType) {
        requiredMediaType = constraintAnnotation.value
    }

    override fun isValid(value: MultipartFile?, context: ConstraintValidatorContext): Boolean {
        if ((value == null) || (requiredMediaType == value.contentType)) {
            return true
        }
        context.unwrap(HibernateConstraintValidatorContext::class.java)
            .addMessageParameter("0", requiredMediaType)
            .addMessageParameter("1", value.contentType)

        return false
    }
}
