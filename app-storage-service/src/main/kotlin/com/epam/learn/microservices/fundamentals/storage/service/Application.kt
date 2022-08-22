package com.epam.learn.microservices.fundamentals.storage.service

import com.epam.learn.microservices.fundamentals.storage.service.service.StorageService
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(proxyBeanMethods = false)
@ConfigurationPropertiesScan("com.epam.learn.microservices.fundamentals.storage.service.config")
class ResourceServiceRunner {

    @Bean
    fun applicationRunner(storageService: StorageService) = ApplicationRunner {
        storageService.createDefaultStorages()
    }
}

fun main(args: Array<String>) {
    runApplication<ResourceServiceRunner>(*args)
}
