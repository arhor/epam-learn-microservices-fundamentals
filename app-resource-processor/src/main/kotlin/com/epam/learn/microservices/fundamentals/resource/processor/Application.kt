package com.epam.learn.microservices.fundamentals.resource.processor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication(proxyBeanMethods = false)
@ConfigurationPropertiesScan("com.epam.learn.microservices.fundamentals.resource.processor.config")
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
