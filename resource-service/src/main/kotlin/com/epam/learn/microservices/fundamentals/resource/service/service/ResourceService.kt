package com.epam.learn.microservices.fundamentals.resource.service.service

import com.epam.learn.microservices.fundamentals.resource.service.service.dto.ResourceDTO

interface ResourceService {

    fun saveResource(filename: String, data: ByteArray): Long

    fun saveResource(resource: ResourceDTO): Long

    fun getResource(id: Long): ResourceDTO

    fun getUnprocessedResource(): ResourceDTO

    fun resetOutdatedPendingResourcesStatus(): Int

    fun deleteResources(ids: Iterable<Long>): Iterable<Long>
}
