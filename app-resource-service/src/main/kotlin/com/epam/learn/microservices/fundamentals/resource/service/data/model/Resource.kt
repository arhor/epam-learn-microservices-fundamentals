package com.epam.learn.microservices.fundamentals.resource.service.data.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Immutable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Immutable
@Table("resources")
data class Resource(
    @Id
    @Column
    val id: Long? = null,

    @Column
    val filename: String,

    @Column
    val storageId: Long,
)
