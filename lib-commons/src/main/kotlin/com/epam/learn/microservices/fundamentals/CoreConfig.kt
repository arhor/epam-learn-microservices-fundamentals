package com.epam.learn.microservices.fundamentals

import com.epam.learn.microservices.fundamentals.logging.LoggingAspect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration(proxyBeanMethods = false)
class CoreConfig {

    @Bean
    @Profile("dev")
    fun loggingAspect() = LoggingAspect()
}
