package com.epam.learn.microservices.fundamentals.resource.processor.config

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@Configuration(proxyBeanMethods = false)
class SchedulerConfig {
}
