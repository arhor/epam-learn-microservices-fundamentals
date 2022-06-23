package com.epam.learn.microservices.fundamentals.resource.processor.listener

import org.slf4j.LoggerFactory
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import java.lang.invoke.MethodHandles

@Component
class ResourceCreatedEventListener {

    @JmsListener(destination = "resource-created-events")
    fun processCreatedResource(message: String) {
        log.info("Received message: {}", message)
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
