package com.epam.learn.microservices.fundamentals.resource.service.data.repository

import com.epam.learn.microservices.fundamentals.resource.service.data.model.Resource
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ResourceRepository : CrudRepository<Resource, Long>
