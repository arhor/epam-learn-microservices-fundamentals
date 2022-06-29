package com.epam.learn.microservices.fundamentals.resource.service.data.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("resources")
class Resource {

    @Id
    @Column("id")
    var id: Long? = null

    @Column("filename")
    var filename: String? = null
}
