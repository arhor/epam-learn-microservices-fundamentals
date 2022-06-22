package com.epam.learn.microservices.fundamentals.resource.service.data.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("resources")
class Resource {

    @Id
    @Column("id")
    var id: Long? = null

    @Column("filename")
    var filename: String? = null

    @Column("status")
    var status: ProcessingStatus? = null

    @CreatedDate
    @Column("created")
    var created: LocalDateTime? = null

    @LastModifiedDate
    @Column("updated")
    var updated: LocalDateTime? = null

    enum class ProcessingStatus {
        /**
         * Indicates that the resource newly uploaded and is not processed yet.
         */
        NONE,

        /**
         * Indicates that the resource data is processing at the moment.
         */
        PENDING,

        /**
         * Indicated that the resource data is already processed.
         */
        COMPLETE
    }
}
