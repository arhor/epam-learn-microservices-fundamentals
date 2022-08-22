package com.epam.learn.microservices.fundamentals.resource.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication(proxyBeanMethods = false)
@ConfigurationPropertiesScan("com.epam.learn.microservices.fundamentals.resource.service.config")
class ResourceServiceRunner

fun main(args: Array<String>) {
    runApplication<ResourceServiceRunner>(*args)
}
