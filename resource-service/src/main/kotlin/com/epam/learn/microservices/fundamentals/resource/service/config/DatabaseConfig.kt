package com.epam.learn.microservices.fundamentals.resource.service.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration(proxyBeanMethods = false)
@EnableJdbcRepositories(
    basePackages = [
        "com.epam.learn.microservices.fundamentals.resource.service.data.repository"
    ]
)
@EnableTransactionManagement
class DatabaseConfig
