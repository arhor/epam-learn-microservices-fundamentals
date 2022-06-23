package com.epam.learn.microservices.fundamentals.resource.processor.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("configuration.aws.sqs")
data class SQSProps(
    val queue: String,
)
