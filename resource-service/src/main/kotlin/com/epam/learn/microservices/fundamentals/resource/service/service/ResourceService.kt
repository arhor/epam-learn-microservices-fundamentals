package com.epam.learn.microservices.fundamentals.resource.service.service

import com.epam.learn.microservices.fundamentals.resource.service.data.model.ResourceMeta
import com.epam.learn.microservices.fundamentals.resource.service.service.dto.ResourceDTO
import java.io.InputStream

interface ResourceService {

    fun saveResource(filename: String, data: ByteArray): Long

    fun saveResource(resource: ResourceDTO): Long

    fun findResource(id: Long): ResourceDTO

    fun deleteResources(ids: Iterable<Long>): Iterable<Long>
}
