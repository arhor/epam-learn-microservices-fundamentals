package com.epam.learn.microservices.fundamentals.storage.service.data.repository

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.storage.service.data.model.Storage
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository

@LogExecution
interface StorageRepository : CrudRepository<Storage, Long> {

    @Modifying
    @Query("DELETE FROM storages WHERE id IN :ids RETURNING id")
    fun deleteAllByIdReturningDeletedIds(ids: Iterable<Long>): List<Long>
}
