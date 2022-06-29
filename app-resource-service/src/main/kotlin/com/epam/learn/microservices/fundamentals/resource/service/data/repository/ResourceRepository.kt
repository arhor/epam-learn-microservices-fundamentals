package com.epam.learn.microservices.fundamentals.resource.service.data.repository

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.service.data.model.Resource
import org.springframework.data.repository.CrudRepository

@LogExecution
interface ResourceRepository : CrudRepository<Resource, Long> {

    fun existsByFilename(filename: String): Boolean
}
