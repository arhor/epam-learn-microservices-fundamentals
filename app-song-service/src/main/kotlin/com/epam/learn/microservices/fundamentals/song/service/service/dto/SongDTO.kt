package com.epam.learn.microservices.fundamentals.song.service.service.dto

data class SongDTO(
    val name: String? = null,
    val year: String? = null,
    val album: String? = null,
    val artist: String? = null,
    val length: String? = null,
    val resourceId: Long,
)
