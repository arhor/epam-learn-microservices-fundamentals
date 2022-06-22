package com.epam.learn.microservices.fundamentals.resource.processor.client.error

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.Series.CLIENT_ERROR
import org.springframework.http.HttpStatus.Series.SERVER_ERROR
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler
import java.lang.invoke.MethodHandles

@Component
class RestTemplateResponseErrorHandler : ResponseErrorHandler {

    override fun hasError(response: ClientHttpResponse): Boolean {
        response.statusCode.series().let {
            return it == CLIENT_ERROR
                || it == SERVER_ERROR
        }
    }

    override fun handleError(response: ClientHttpResponse) {
        when (response.statusCode.series()) {
            SERVER_ERROR -> {
                log.error("Server error occurred - status: {}, body: {}", response.statusCode, response.body)
            }
            CLIENT_ERROR -> {
                log.error("Client error occurred - status: {}, body: {}", response.statusCode, response.body)
            }
            else -> {
                /* no-op */
            }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
