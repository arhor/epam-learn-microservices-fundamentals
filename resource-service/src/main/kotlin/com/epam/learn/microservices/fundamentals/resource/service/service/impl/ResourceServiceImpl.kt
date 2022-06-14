package com.epam.learn.microservices.fundamentals.resource.service.service.impl

import com.epam.learn.microservices.fundamentals.resource.service.data.repository.ResourceRepository
import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceService
import org.springframework.stereotype.Service

@Service
class ResourceServiceImpl(private val repository: ResourceRepository) : ResourceService {

}
