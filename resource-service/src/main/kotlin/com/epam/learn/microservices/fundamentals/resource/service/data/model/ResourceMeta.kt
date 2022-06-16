package com.epam.learn.microservices.fundamentals.resource.service.data.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("resources")
data class ResourceMeta(
    @Id
    var id: Long? = null,
    var filename: String,
)
