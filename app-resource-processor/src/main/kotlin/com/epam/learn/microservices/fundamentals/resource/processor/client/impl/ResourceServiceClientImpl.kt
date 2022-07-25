package com.epam.learn.microservices.fundamentals.resource.processor.client.impl

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.processor.client.ResourceServiceClient
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.InputStream

@Service
@LogExecution
@Retry(name = "resource-service-client")
class ResourceServiceClientImpl(private val http: RestTemplate) : ResourceServiceClient {

    @Value("\${configuration.resource-service-url}")
    private lateinit var baseURL: String

    override fun fetchResourceBinaryData(id: Long): InputStream {
        return http.getForObject("$baseURL/resources/{id}", Resource::class.java, id)?.inputStream
            ?: throw IllegalStateException("Response body cannot be null")
    }
}
