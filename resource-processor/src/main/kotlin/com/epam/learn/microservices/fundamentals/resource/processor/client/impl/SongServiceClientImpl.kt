package com.epam.learn.microservices.fundamentals.resource.processor.client.impl

import com.epam.learn.microservices.fundamentals.resource.processor.client.SongServiceClient
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SongServiceClientImpl(private val restTemplate: RestTemplate) : SongServiceClient {

}
