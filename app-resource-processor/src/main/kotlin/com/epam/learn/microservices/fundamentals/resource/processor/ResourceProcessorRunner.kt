package com.epam.learn.microservices.fundamentals.resource.processor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication(proxyBeanMethods = false)
class ResourceProcessorRunner

fun main(args: Array<String>) {
    runApplication<ResourceProcessorRunner>(*args)
}
