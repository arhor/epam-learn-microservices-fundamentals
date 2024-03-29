package com.epam.learn.microservices.fundamentals.storage.service.data.repository

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.storage.service.data.StorageType
import com.epam.learn.microservices.fundamentals.storage.service.data.model.Storage
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository

@LogExecution
interface StorageRepository : CrudRepository<Storage, Long> {

    @Query("SELECT * FROM storages WHERE type = :type")
    fun findAllByType(type: StorageType): List<Storage>

    @Query("SELECT * FROM storages LIMIT 1")
    fun findAny(): List<Storage>

    @Query("SELECT * FROM storages WHERE type = :type LIMIT 1")
    fun findSingleByType(type: StorageType): List<Storage>

    fun existsByName(name: String): Boolean

    fun existsByNameAndType(name: String, type: StorageType): Boolean
}
