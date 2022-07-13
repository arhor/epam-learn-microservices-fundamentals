package com.epam.learn.microservices.fundamentals.song.service.data.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("songs")
data class Song(
    @Id
    @Column("id")
    val id: Long? = null,

    @Column("name")
    val name: String? = null,

    @Column("year")
    val year: String? = null,

    @Column("album")
    val album: String? = null,

    @Column("artist")
    val artist: String? = null,

    @Column("length")
    val length: String? = null,

    @Column("resource_id")
    val resourceId: Long,
)
