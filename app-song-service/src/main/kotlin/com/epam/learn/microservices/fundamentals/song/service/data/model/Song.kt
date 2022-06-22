package com.epam.learn.microservices.fundamentals.song.service.data.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Duration
import java.time.Year

@Table("songs")
class Song(
    @Id
    @Column("id")
    var id: Long? = null,

    @Column("name")
    var name: String? = null,

    @Column("year")
    var year: String? = null,

    @Column("album")
    var album: String? = null,

    @Column("artist")
    var artist: String? = null,

    @Column("length")
    var length: String? = null,

    @Column("resource_id")
    var resourceId: Long,
)
