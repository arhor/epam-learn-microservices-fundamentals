package com.epam.learn.microservices.fundamentals.resource.processor.client.impl

import com.epam.learn.microservices.fundamentals.resource.processor.client.ResourceServiceClient
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ResourceServiceClientImpl(private val restTemplate: RestTemplate) : ResourceServiceClient {

}
