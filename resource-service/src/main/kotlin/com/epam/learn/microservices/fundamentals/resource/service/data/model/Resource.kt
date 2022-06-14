package com.epam.learn.microservices.fundamentals.resource.service.data.model

import org.springframework.data.annotation.Id

class Resource(
    @Id
    var id: Long? = null
)
