package com.epam.learn.microservices.fundamentals.resource.service.data.repository

import com.epam.learn.microservices.fundamentals.resource.service.data.model.ResourceMeta
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ResourceMetaRepository : CrudRepository<ResourceMeta, Long> {
    fun existsByFilename(filename: String): Boolean
}
