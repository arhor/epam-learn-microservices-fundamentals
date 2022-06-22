package com.epam.learn.microservices.fundamentals.resource.processor.client.impl

import com.epam.learn.microservices.fundamentals.dto.IdListDTO
import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.processor.client.ResourceServiceClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
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

    override fun fetchUnprocessedResourceIds(): List<Long> {
        val exchange =
            restTemplate.exchange(
                "$baseURL/unprocessed",
                HttpMethod.GET,
                null,
                LongIdListDto
            )

        return exchange.body!!.ids
    }

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


    override fun updateResourceStatus(id: Long, status: String) {
        restTemplate.patchForObject(
            "$baseURL/$id",
            mapOf("status" to status),
            String::class.java
        )
    }

    private object LongIdListDto : ParameterizedTypeReference<IdListDTO<Long>>()
}
