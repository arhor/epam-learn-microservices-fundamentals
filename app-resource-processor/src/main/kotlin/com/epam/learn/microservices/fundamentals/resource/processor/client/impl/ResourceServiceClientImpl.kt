package com.epam.learn.microservices.fundamentals.resource.processor.client.impl

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.processor.client.ResourceServiceClient
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.InputStream

@Service
@LogExecution
class ResourceServiceClientImpl(
    private val restTemplate: RestTemplate,
    @Value("\${configuration.resource-service-url}")
    private val baseURL: String
) : ResourceServiceClient {

    @Retry(name = "fetch-resource-binary-data")
    override fun fetchResourceBinaryData(id: Long): InputStream {
        val response =
            restTemplate.exchange(
                "$baseURL/$id",
                HttpMethod.GET,
                null,
                Resource::class.java
            )

        return response.body!!.inputStream
    }
}
