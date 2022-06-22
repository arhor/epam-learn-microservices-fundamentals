package com.epam.learn.microservices.fundamentals.resource.service.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("configuration.aws.s3")
data class S3Props(
    val bucket: String,
)
