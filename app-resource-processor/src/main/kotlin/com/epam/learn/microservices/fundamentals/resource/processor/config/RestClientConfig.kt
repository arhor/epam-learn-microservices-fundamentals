package com.epam.learn.microservices.fundamentals.resource.processor.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate

@Configuration(proxyBeanMethods = false)
@Import(RestTemplateAutoConfiguration::class)
class RestClientConfig {

    @Bean
    @ConditionalOnMissingBean
    fun defaultRestTemplate(builder: RestTemplateBuilder, errorHandler: ResponseErrorHandler): RestTemplate {
        return builder
            .errorHandler(errorHandler)
            .requestFactory(::HttpComponentsClientHttpRequestFactory)
            .build()
    }
}
