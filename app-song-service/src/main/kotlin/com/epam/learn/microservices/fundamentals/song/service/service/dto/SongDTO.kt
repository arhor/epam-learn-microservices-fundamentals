package com.epam.learn.microservices.fundamentals.song.service.service.dto

data class SongDTO(
    val name: String?,
    val year: String?,
    val album: String?,
    val artist: String?,
    val length: String?,
    val resourceId: Long,
)
