package com.epam.learn.microservices.fundamentals.song.service.data.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Immutable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Immutable
@Table("songs")
data class Song(
    @Id
    @Column
    val id: Long? = null,

    @Column
    val name: String? = null,

    @Column
    val year: String? = null,

    @Column
    val album: String? = null,

    @Column
    val artist: String? = null,

    @Column
    val length: String? = null,

    @Column
    val resourceId: Long,
)
