package com.epam.learn.microservices.fundamentals.resource.processor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication(proxyBeanMethods = false)
@ConfigurationPropertiesScan("com.epam.learn.microservices.fundamentals.resource.processor.config")
class ResourceProcessorRunner

fun main(args: Array<String>) {
    runApplication<ResourceProcessorRunner>(*args)
}
