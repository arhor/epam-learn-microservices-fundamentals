package com.epam.learn.microservices.fundamentals.resource.service.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("configuration.aws")
data class AWSProps(
    val url: String,
    val region: String,
    val accessKey: String,
    val secretKey: String,
)
