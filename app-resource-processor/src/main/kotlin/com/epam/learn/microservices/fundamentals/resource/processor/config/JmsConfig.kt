package com.epam.learn.microservices.fundamentals.resource.processor.config

import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import org.springframework.jms.support.destination.DestinationResolver
import org.springframework.jms.support.destination.DynamicDestinationResolver

@Import(JmsAutoConfiguration::class)
@Configuration(proxyBeanMethods = false)
class JmsConfig {

    @Bean
    fun dynamicDestinationResolver(): DestinationResolver {
        return DynamicDestinationResolver()
    }

    @Bean
    fun jacksonJmsMessageConverter(): MessageConverter {
        return MappingJackson2MessageConverter().apply {
            setTargetType(MessageType.TEXT)
            setTypeIdPropertyName("_type")
        }
    }
}
