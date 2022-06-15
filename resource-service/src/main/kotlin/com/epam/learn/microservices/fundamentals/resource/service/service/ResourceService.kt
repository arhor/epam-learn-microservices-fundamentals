package com.epam.learn.microservices.fundamentals.resource.service.service

import com.epam.learn.microservices.fundamentals.resource.service.data.model.ResourceMeta
import java.io.InputStream

interface ResourceService {

    fun saveResource(filename: String, data: ByteArray): ResourceMeta

    fun findResource(id: Long): Pair<ResourceMeta, InputStream>

    fun deleteResources(ids: Iterable<Long>): Iterable<Long>
}
