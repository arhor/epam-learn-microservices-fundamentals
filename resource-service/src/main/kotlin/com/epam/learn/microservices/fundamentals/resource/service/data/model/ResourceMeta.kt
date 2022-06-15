package com.epam.learn.microservices.fundamentals.resource.service.data.model

import org.springframework.data.annotation.Id

data class ResourceMeta(
    @Id
    var id: Long? = null,
    var filename: String,
    var uploaded: Boolean,
    var length: Long,
)
