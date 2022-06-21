package com.epam.learn.microservices.fundamentals.resource.service.data.repository

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.service.data.model.Resource
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
@LogExecution
interface ResourceRepository : CrudRepository<Resource, Long> {
    fun existsByFilename(filename: String): Boolean

    @Query(
        """
        SELECT r.*
        FROM resources r
        WHERE r.status = 'NONE'
        ORDER BY r.created DESC
        LIMIT 50 FOR UPDATE SKIP LOCKED
        """
    )
    fun findUnprocessedResources(): List<Resource>

    @Query(
        """
        SELECT r.id
        FROM resources r
        WHERE r.status = 'PENDING'
          AND r.updated <= :deadline
        FOR UPDATE SKIP LOCKED
        """
    )
    fun findOutdatedPendingResourcesIds(@Param("deadline") deadline: LocalDateTime): List<Long>

    @Modifying
    @Query("UPDATE resources SET status = :status WHERE id = :id")
    fun updateResourceStatus(@Param("status") status: Resource.ProcessingStatus, id: Long)

    @Modifying
    @Query("UPDATE resources SET status = :status WHERE id IN (:ids)")
    fun updateResourcesStatus(@Param("status") status: Resource.ProcessingStatus, @Param("ids") ids: List<Long>): Int
}
