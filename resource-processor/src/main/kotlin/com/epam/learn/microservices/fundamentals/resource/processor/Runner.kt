package com.epam.learn.microservices.fundamentals.resource.processor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(proxyBeanMethods = false)
class Runner

fun main(args: Array<String>) {
    runApplication<Runner>(*args)
}
