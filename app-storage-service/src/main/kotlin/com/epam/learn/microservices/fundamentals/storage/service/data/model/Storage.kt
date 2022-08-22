package com.epam.learn.microservices.fundamentals.storage.service.data.model

import com.epam.learn.microservices.fundamentals.storage.service.data.StorageType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Immutable
import org.springframework.data.relational.core.mapping.Table

/**
 * @param id   unique identifier
 * @param name AWS S3 bucket name
 * @param type storage type
 */
@Immutable
@Table("storages")
data class Storage(
    @Id
    val id: Long? = null,
    val name: String,
    val type: StorageType,
)
