package com.epam.learn.microservices.fundamentals.resource.service.data.repository

import com.epam.learn.microservices.fundamentals.resource.service.data.model.Resource
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface ResourceRepository : CrudRepository<Resource, Long> {
    fun existsByFilename(filename: String): Boolean

    @Query(
        """
        SELECT r.filename
        FROM resources r
        WHERE r.status = 'NONE'
        ORDER BY r.created DESC
        LIMIT 1 FOR UPDATE SKIP LOCKED
        """
    )
    fun findUnprocessedResourceFilename(): String?

    @Query(
        """
        SELECT r.filename
        FROM resources r
        WHERE r.status = 'PENDING'
          AND r.updated >= :deadline
        FOR UPDATE SKIP LOCKED
        """
    )
    fun findOutdatedPendingResourcesFilenames(deadline: LocalDateTime): List<String>

    @Modifying
    @Query("UPDATE resources SET status = :status WHERE filename = :filename")
    fun updateResourceStatus(status: Resource.ProcessingStatus, filename: String)

    @Modifying
    @Query("UPDATE resources SET status = :status WHERE filename IN :filenames")
    fun updateResourcesStatus(status: Resource.ProcessingStatus, filenames: List<String>): Int
}
