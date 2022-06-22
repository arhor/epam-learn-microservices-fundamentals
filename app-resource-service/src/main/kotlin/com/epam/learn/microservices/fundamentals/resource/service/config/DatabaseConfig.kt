package com.epam.learn.microservices.fundamentals.resource.service.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.time.Clock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Configuration(proxyBeanMethods = false)
@EnableJdbcAuditing(modifyOnCreate = false, dateTimeProviderRef = "dateTimeProviderCurrentUTC")
@EnableJdbcRepositories("com.epam.learn.microservices.fundamentals.resource.service.data.repository")
@EnableTransactionManagement
class DatabaseConfig {

    @Bean
    fun dateTimeProviderCurrentUTC() = DateTimeProvider {
        LocalDateTime
            .now(Clock.systemUTC())
            .truncatedTo(ChronoUnit.MILLIS)
            .let { Optional.of(it) }
    }
}
