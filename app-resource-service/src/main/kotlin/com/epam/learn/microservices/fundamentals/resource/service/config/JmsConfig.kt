package com.epam.learn.microservices.fundamentals.resource.service.config

import com.epam.learn.microservices.fundamentals.resource.service.config.props.SQSProps
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import org.springframework.jms.support.destination.DynamicDestinationResolver
import javax.jms.ConnectionFactory
import javax.jms.Session


@EnableJms
@Configuration(proxyBeanMethods = false)
class JmsConfig(private val sqsProps: SQSProps) {

    @Bean
    fun jmsListenerContainerFactory(connectionFactory: ConnectionFactory): JmsListenerContainerFactory<*> {
        return DefaultJmsListenerContainerFactory().apply {
            setConnectionFactory(connectionFactory)
            setDestinationResolver(DynamicDestinationResolver())
            setConcurrency("3-10")
            setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE)
        }
    }

    @Bean
    fun defaultJmsTemplate(connectionFactory: ConnectionFactory): JmsTemplate {
        return JmsTemplate(connectionFactory).apply {
            defaultDestinationName = sqsProps.queue
        }
    }

    @Bean
    fun jacksonJmsMessageConverter(): MessageConverter {
        return MappingJackson2MessageConverter().apply {
            setTargetType(MessageType.TEXT)
            setTypeIdPropertyName("_type")
        }
    }
}
