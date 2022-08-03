package com.epam.learn.microservices.fundamentals.resource.processor.client

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@LogExecution
@Retry(name = "resource-service-client")
@FeignClient(name = "resource-service", path = "/api/resources")
interface ResourceServiceClient {

    @GetMapping("/{id}")
    fun fetchResourceBinaryData(@PathVariable id: Long): ResponseEntity<Resource>
}
