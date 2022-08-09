package com.epam.learn.microservices.fundamentals.storage.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(proxyBeanMethods = false)
class ResourceServiceRunner

fun main(args: Array<String>) {
    runApplication<ResourceServiceRunner>(*args)
}
