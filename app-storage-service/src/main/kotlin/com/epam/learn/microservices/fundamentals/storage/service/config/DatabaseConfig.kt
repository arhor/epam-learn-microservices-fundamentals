package com.epam.learn.microservices.fundamentals.storage.service.config

import com.epam.learn.microservices.fundamentals.storage.service.data.repository.RepositoryPackage
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration(proxyBeanMethods = false)
@EnableJdbcRepositories(basePackageClasses = [RepositoryPackage::class])
@EnableTransactionManagement
class DatabaseConfig
